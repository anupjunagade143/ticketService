package com.ticketService.executor;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.constants.TheaterConstants;
import com.ticketService.domain.CustomerConfirmationOrder;
import com.ticketService.domain.Theater;
import com.ticketService.domain.UserOrder;
import com.ticketService.interfaces.serviceDao.ITicketService;
import com.ticketService.serviceDaoImpl.PostReservationHelper;
import com.ticketService.syncOperations.SeatSynchronizeOperation;

/**
 * Customer seat request is confirmed and will be processed here,later generating a confirmation email
 * 
 * @author ajunaga
 *
 */
public class ReserveSeatsThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(ReserveSeatsThread.class.getSimpleName());

	private BlockingQueue<UserOrder> reserveSeatsQueue;

	private int threadNumber = 0;

	@Autowired
	@Qualifier("theater")
	private Theater theater;

	@Autowired
	@Qualifier("iTicketService")
	private ITicketService iTicketService;

	@Autowired
	@Qualifier("seatSynchronizeOperation")
	private SeatSynchronizeOperation seatSynchronizeOperation;

	@Autowired
	@Qualifier("postReservationHelper")
	private PostReservationHelper postReservationHelper;

	@Override
	public void run() {

		Thread.currentThread().setName("ReserveSeatsThread::" + threadNumber);
		String threadName = Thread.currentThread().getName();

		logger.debug("Enter ReserveSeatsThread run() for thread:" + threadName);

		while (!TheaterConstants.IS_FIND_AND_HOLD_SEATS_POOL_EXECUTOR_COMPLETE || !reserveSeatsQueue.isEmpty()) {
			UserOrder order = null;
			try {
				/* reserveSeatsQueue contains seatHold request object */
				order = reserveSeatsQueue.poll();
				if (order != null) {
					seatSynchronizeOperation.reserveSeats(order.getSeatList(), order.getSeatHoldId());
					String successMsg = iTicketService.reserveSeats(order.getSeatHoldId(), order.getCustomerEmail());
					CustomerConfirmationOrder confirmOrder = postReservationHelper.composeEmail(order.getSeatHoldId(),
							order.getSeatList(), successMsg, order.getCustomerEmail());
					postReservationHelper.sendEmail(confirmOrder);
					TheaterConstants.CONFIRM_SEAT_RESERVATION_COUNT.addAndGet(1);
				}
			} catch (Exception e) {
				logger.error(threadName + " --> error processing confirmed userOrder:" + order, e);
			}
		}

		logger.debug("Exit ReserveSeatsThread run() for thread:" + threadName);
	}

	public BlockingQueue<UserOrder> getReserveSeatsQueue() {
		return reserveSeatsQueue;
	}

	public void setReserveSeatsQueue(BlockingQueue<UserOrder> reserveSeatsQueue) {
		this.reserveSeatsQueue = reserveSeatsQueue;
	}

	public int getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}
}
