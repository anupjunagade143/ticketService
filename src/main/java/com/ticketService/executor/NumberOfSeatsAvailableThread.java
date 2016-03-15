package com.ticketService.executor;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.constants.TheaterConstants;
import com.ticketService.dataGenerator.CommonRandomUtils;
import com.ticketService.domain.Optional;
import com.ticketService.domain.Theater;
import com.ticketService.domain.UserOrder;
import com.ticketService.interfaces.serviceDao.ITicketService;

/**
 * Runnable thread to work on multiple customer request to calculate number of available seats
 * 
 * @author ajunaga
 *
 */
public class NumberOfSeatsAvailableThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(NumberOfSeatsAvailableThread.class.getSimpleName());

	private BlockingQueue<UserOrder> numberOfSeatsAvailableQueue;

	private int threadNumber = 0;

	@Autowired
	@Qualifier("theater")
	private Theater theater;

	@Autowired
	@Qualifier("iTicketService")
	private ITicketService iTicketService;

	@Autowired
	@Qualifier("commonRandomUtils")
	private CommonRandomUtils commonRandomUtils;

	@Autowired
	@Qualifier("maxTheaterLevel")
	private Integer maxTheaterLevel;

	@Override
	public void run() {

		Thread.currentThread().setName("NumberOfSeatsAvailableThread::" + threadNumber);
		String threadName = Thread.currentThread().getName();

		logger.debug("Enter NumberOfSeatsAvailableThread run() for thread:" + threadName);

		UserOrder seatAvailabilityRequest = null;
		try {
			logger.debug("User request:numberOfSeatsAvailableQueue size:" + numberOfSeatsAvailableQueue.size());
			seatAvailabilityRequest = numberOfSeatsAvailableQueue.poll();

			if (seatAvailabilityRequest != null) {
				/* random logic to generate level to search. A dummy level is generated to generate test scenario */
				int searchLevel = commonRandomUtils.getRandomLevel(TheaterConstants.DEFAULT_LEVEL, maxTheaterLevel);

				Optional<Integer> level = new Optional<Integer>(searchLevel);

				logger.debug(new StringBuilder().append(" ->Finding seats at level:").append(searchLevel).append(
						" for customer:").append(seatAvailabilityRequest.getCustomerEmail()).toString());

				int totalSeatsAvailable = iTicketService.numSeatsAvailable(level);

				logger.info(new StringBuilder().append("\n\n->Level-id:").append(searchLevel).append(
						"\nnumSeatsAvailable: ").append(totalSeatsAvailable).toString());
			}
		} catch (Exception e) {
			logger.error(threadName + " --> error processing userOrder:" + seatAvailabilityRequest, e);
		}

		logger.debug("Exit NumberOfSeatsAvailableThread run() for thread:" + threadName);
	}

	public BlockingQueue<UserOrder> getNumberOfSeatsAvailableQueue() {
		return numberOfSeatsAvailableQueue;
	}

	public void setNumberOfSeatsAvailableQueue(BlockingQueue<UserOrder> numberOfSeatsAvailableQueue) {
		this.numberOfSeatsAvailableQueue = numberOfSeatsAvailableQueue;
	}

	public int getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

}
