package com.ticketService.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.constants.TheaterConstants;
import com.ticketService.dataGenerator.CommonRandomUtils;
import com.ticketService.domain.SeatHold;
import com.ticketService.domain.UserOrder;
import com.ticketService.syncOperations.SeatSynchronizeOperation;

/**
 * Seat which are on hold need to be processed further for final confirmation ,where user will either ACCEPT or REJECT
 * it. Seats can be put on hold for specific time period [ticket.hold.expire.time property].
 * 
 * @author ajunaga
 *
 */
public class SeatHoldTimeExpireThread implements Callable<Boolean> {

	private static final Logger logger = LoggerFactory.getLogger(SeatHoldTimeExpireThread.class.getSimpleName());

	private BlockingQueue<UserOrder> reserveSeatsQueue;

	@Autowired
	@Qualifier("commonRandomUtils")
	private CommonRandomUtils commonRandomUtils;

	private SeatHold seatHold;

	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

	@Autowired
	@Qualifier("seatSynchronizeOperation")
	private SeatSynchronizeOperation seatSynchronizeOperation;

	@Override
	public Boolean call() {
		boolean isReserved = false;
		/*
		 * below is just a random value to decide whether user is interested in confirming the seats further or user
		 * wants to cancel the transaction. If User is interested, it will be considered as final booking and will be
		 * processed ahead. If user rejects, then onHold Status be updated to false for those seats
		 */
		boolean userWantToConfirmSeat = commonRandomUtils.generateRandomBoolean();
		logger.debug("SeatHoldId:" + seatHold.getSeatHoldId() + " --> seatHoldStatus:" + userWantToConfirmSeat);
		if (userWantToConfirmSeat) {
			UserOrder confirmOrder = new UserOrder(seatHold.getCustomerEmail(), seatHold.getSeatHoldId(),
					seatHold.getSeatList());
			/*
			 * If order is confirmed, it will further be processed for final reservation to update status isBooked =
			 * true
			 */
			reserveSeatsQueue.add(confirmOrder);
			logger.debug("reservation confirmed and in queue for processing:" + confirmOrder.toString());
			logger.debug("Reservation in final process: reserveSeatsQueue size in SeatHoldTimeExpireThread :"
					+ reserveSeatsQueue.size());
			isReserved = true;
		} else {
			/* If seats are not finally confirmed , then the seat status will be set as isOnHold = false */
			seatSynchronizeOperation.expireTicketSeatHold(seatHold);
			TheaterConstants.REJECT_SEAT_RESERVATION_COUNT.addAndGet(1);
		}
		scheduledThreadPoolExecutor.shutdown();
		scheduledThreadPoolExecutor.shutdownNow();
		return isReserved;
	}

	public SeatHold getSeatHold() {
		return seatHold;
	}

	public void setSeatHold(SeatHold seatHold) {
		this.seatHold = seatHold;
	}

	public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() {
		return scheduledThreadPoolExecutor;
	}

	public void setScheduledThreadPoolExecutor(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
		this.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;
	}

	public BlockingQueue<UserOrder> getReserveSeatsQueue() {
		return reserveSeatsQueue;
	}

	public void setReserveSeatsQueue(BlockingQueue<UserOrder> reserveSeatsQueue) {
		this.reserveSeatsQueue = reserveSeatsQueue;
	}

}
