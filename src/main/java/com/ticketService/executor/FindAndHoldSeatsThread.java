package com.ticketService.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.domain.SeatHold;
import com.ticketService.domain.Theater;
import com.ticketService.domain.UserOrder;
import com.ticketService.interfaces.serviceDao.ITicketService;
import com.ticketService.syncOperations.SeatSynchronizeOperation;

/**
 * Runnable thread for multiple customer request to find and hold seats
 * 
 * @author ajunaga
 *
 */
public class FindAndHoldSeatsThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(FindAndHoldSeatsThread.class.getSimpleName());

	private BlockingQueue<UserOrder> findAndHoldSeatsQueue;

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
	@Qualifier("seatHoldTimeExpireThread")
	private SeatHoldTimeExpireThread seatHoldTimeExpireThread;

	@Autowired
	@Qualifier("ticketHoldExpireTime")
	private Integer ticketHoldExpireTime;

	@Override
	public void run() {

		Thread.currentThread().setName("FindAndHoldSeatsThread::" + threadNumber);
		String threadName = Thread.currentThread().getName();

		logger.debug("Enter FindAndHoldSeatsThread run() for thread:" + threadName);

		while (!findAndHoldSeatsQueue.isEmpty()) {

			logger.debug("User request:findAndHoldSeatsQueue size in FindAndHoldSeatsThread-class:"
					+ findAndHoldSeatsQueue.size());

			UserOrder order = null;
			try {
				order = findAndHoldSeatsQueue.poll();
				if (order != null) {
					SeatHold seatHold = iTicketService.findAndHoldSeats(order.getNumSeats(), order.getMinLevel(),
							order.getMaxLevel(), order.getCustomerEmail());

					if (seatHold != null) {
						/*
						 * Is user findAndHold seat request is successful, a seatHold object will be created and a
						 * scheduled executor service will be executed as configured value ticketHoldExpiry. Which means
						 * after expiry time interval(e.g. after 20 seconds) hold status on the seat will be either
						 * confirmed or cancelled. This will be decided in SeatHoldTimeExpireThread.java class
						 */
						seatHoldTimeExpireThread.setSeatHold(seatHold);
						ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
						seatHoldTimeExpireThread.setScheduledThreadPoolExecutor(scheduledThreadPoolExecutor);

						/* futureObj contains value whether seatHold operation SUCCEED or FAILED */
						Future<Boolean> futureObj = scheduledThreadPoolExecutor.schedule(seatHoldTimeExpireThread,
								ticketHoldExpireTime, TimeUnit.SECONDS);

						boolean isReserved = false;
						try {
							isReserved = futureObj.get();
						} catch (InterruptedException | ExecutionException e) {
							logger.error("error while getting future object for SeatHoldTimeExpireThread", e);
						}

						logger.debug("task submitted to seatHoldExpireService for seatHoldId:"
								+ seatHold.getSeatHoldId() + ". Status:" + isReserved);
					}
				}
			} catch (Exception e) {
				logger.error(threadName + " --> error processing userOrder:" + order, e);
			}
		}

		logger.debug("Exit FindAndHoldSeatsThread run() for thread:" + threadName);
	}

	public BlockingQueue<UserOrder> getFindAndHoldSeatsQueue() {
		return findAndHoldSeatsQueue;
	}

	public void setFindAndHoldSeatsQueue(BlockingQueue<UserOrder> findAndHoldSeatsQueue) {
		this.findAndHoldSeatsQueue = findAndHoldSeatsQueue;
	}

	public int getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

}
