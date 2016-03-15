package com.ticketService.dataGenerator;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.domain.Optional;
import com.ticketService.domain.UserOrder;

/**
 * Generate dummy user order to book seats, seats availability
 * 
 * @author ajunaga
 *
 */
public class RandomDataGenerator implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(RandomDataGenerator.class.getSimpleName());

	@Autowired
	@Qualifier("commonRandomUtils")
	private CommonRandomUtils commonRandomUtils;

	@Autowired
	@Qualifier("randomOrderCount")
	private Integer randomOrderCount;

	private BlockingQueue<UserOrder> numberOfSeatsAvailableQueue;
	private BlockingQueue<UserOrder> findAndHoldSeatsQueue;

	@Autowired
	@Qualifier("minBookingPerCustomer")
	private Integer minBookingPerCustomer;

	@Autowired
	@Qualifier("maxBookingPerCustomer")
	private Integer maxBookingPerCustomer;

	@Autowired
	@Qualifier("minTheaterLevel")
	private Integer minTheaterLevel;

	@Autowired
	@Qualifier("maxTheaterLevel")
	private Integer maxTheaterLevel;

	@Override
	public void afterPropertiesSet() throws Exception {
		generateUserOrders();
	}

	/**
	 * Generate dummy user orders
	 */
	private void generateUserOrders() {
		generateSeatAvailabilityOrder();
		generateFindAndHoldOrder();
	}

	/**
	 * Add the user orders to linkedBlockingQueue which will be processed later
	 */
	private void generateFindAndHoldOrder() {
		for (int orderCnt = 0; orderCnt < randomOrderCount; orderCnt++) {
			findAndHoldSeatsQueue.add(getOrderObject());
		}
		logger.debug("User request:findAndHoldSeatsQueue size:" + findAndHoldSeatsQueue.size());
	}

	/**
	 * Add the user orders to linkedBlockingQueue which will be processed later
	 */
	private void generateSeatAvailabilityOrder() {
		for (int orderCnt = 0; orderCnt < randomOrderCount; orderCnt++) {
			numberOfSeatsAvailableQueue.add(getOrderObject());
		}
		logger.debug("User request:numberOfSeatsAvailableQueue size:" + numberOfSeatsAvailableQueue.size());
	}

	/**
	 * Populate UserOrder dummy request object
	 * 
	 * @return
	 */
	private UserOrder getOrderObject() {
		String custuomerName = commonRandomUtils.getRandomName();
		String emailId = commonRandomUtils.getRandomEmailId();
		Optional<Integer> minLevel = commonRandomUtils.getRandomLevelId(minTheaterLevel, maxTheaterLevel);
		Optional<Integer> maxLevel = commonRandomUtils.getRandomLevelId(minTheaterLevel, maxTheaterLevel);
		int numSeats = commonRandomUtils.getRandomBookingCount(minBookingPerCustomer, maxBookingPerCustomer);

		UserOrder order = new UserOrder(custuomerName, emailId, minLevel, maxLevel, numSeats);
		return order;
	}

	public BlockingQueue<UserOrder> getNumberOfSeatsAvailableQueue() {
		return numberOfSeatsAvailableQueue;
	}

	public void setNumberOfSeatsAvailableQueue(BlockingQueue<UserOrder> numberOfSeatsAvailableQueue) {
		this.numberOfSeatsAvailableQueue = numberOfSeatsAvailableQueue;
	}

	public BlockingQueue<UserOrder> getFindAndHoldSeatsQueue() {
		return findAndHoldSeatsQueue;
	}

	public void setFindAndHoldSeatsQueue(BlockingQueue<UserOrder> findAndHoldSeatsQueue) {
		this.findAndHoldSeatsQueue = findAndHoldSeatsQueue;
	}

}
