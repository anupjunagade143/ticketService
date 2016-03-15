package com.ticketService.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.ticketService.constants.TheaterConstants;

/**
 * Load ThreadPoolExecutors for various tasks, configure runnable class in pool-executor, later call shutdown method to
 * close executors when all tasks are processed
 * 
 * @author ajunaga
 *
 */
public class ServiceThreadPoolExecutor implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(ServiceThreadPoolExecutor.class.getSimpleName());

	private ConfigurableApplicationContext context;

	private ScheduledExecutorService numberOfSeatsAvailablePoolExecutor;

	private ThreadPoolExecutor findAndHoldSeatsPoolExecutor;

	private ThreadPoolExecutor reserveSeatsPoolExecutor;

	@Autowired
	@Qualifier("numberSeatsAvailableThreadCount")
	private Integer numberSeatsAvailableThreadCount;

	@Autowired
	@Qualifier("findAndHoldSeatsThreadCount")
	private Integer findAndHoldSeatsThreadCount;

	@Autowired
	@Qualifier("reserveSeatsThreadCount")
	private Integer reserveSeatsThreadCount;

	@Autowired
	@Qualifier("ticketHoldExpireTime")
	private Integer ticketHoldExpireTime;

	@Autowired
	@Qualifier("availableSeatCountInterval")
	private Integer availableSeatCountInterval;

	/**
	 * Initialize threadPoolexecutors and scheduledThreadPoolExecutor to process User-orders to book ticket, inquiry on
	 * seat availability and final reserve the seats for user by sending a email
	 */
	public void initializeThreadPoolExecutors() {
		logger.debug("initializing thread pool executor");

		/*
		 * This pool will execute threads to process seat availability request . Seat availability is an scheduled
		 * executor service
		 */
		numberOfSeatsAvailablePoolExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(numberSeatsAvailableThreadCount);

		/* This pool will execute threads to find and hold seat request */
		findAndHoldSeatsPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(findAndHoldSeatsThreadCount);

		/*
		 * This pool will execute threads which will be final confirmation stage and once booked , an confirmation
		 * notification will be generated
		 */
		reserveSeatsPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(reserveSeatsThreadCount);

		for (int pool = 0; pool < numberSeatsAvailableThreadCount; pool++) {
			NumberOfSeatsAvailableThread numberOfSeatsAvailableThread = (NumberOfSeatsAvailableThread) context.getBean("numberOfSeatsAvailableThread");
			numberOfSeatsAvailableThread.setThreadNumber(pool);
			numberOfSeatsAvailablePoolExecutor.scheduleAtFixedRate(numberOfSeatsAvailableThread, 0,
					availableSeatCountInterval, TimeUnit.SECONDS);
		}

		for (int pool = 0; pool < findAndHoldSeatsThreadCount; pool++) {
			FindAndHoldSeatsThread findAndHoldSeatsThread = (FindAndHoldSeatsThread) context.getBean("findAndHoldSeatsThread");
			findAndHoldSeatsThread.setThreadNumber(pool);
			findAndHoldSeatsPoolExecutor.execute(findAndHoldSeatsThread);
		}

		for (int pool = 0; pool < reserveSeatsThreadCount; pool++) {
			ReserveSeatsThread reserveSeatsThread = (ReserveSeatsThread) context.getBean("reserveSeatsThread");
			reserveSeatsThread.setThreadNumber(pool);
			reserveSeatsPoolExecutor.execute(reserveSeatsThread);
		}

		logger.debug("thread pool executor loaded successfully");

	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = (ConfigurableApplicationContext) context;
	}

	/**
	 * shutdown the threadpool executors once tasks are completed
	 */
	public void shutdown() {
		try {

			logger.debug("Start: shutdown findAndHoldSeatsPoolExecutor");
			findAndHoldSeatsPoolExecutor.shutdown();
			findAndHoldSeatsPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			findAndHoldSeatsPoolExecutor.shutdownNow();
			logger.debug("End: shutdown findAndHoldSeatsPoolExecutor");
			TheaterConstants.IS_FIND_AND_HOLD_SEATS_POOL_EXECUTOR_COMPLETE = true;

			logger.debug("Start: shutdown reserveSeatsPoolExecutor");
			reserveSeatsPoolExecutor.shutdown();
			reserveSeatsPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			reserveSeatsPoolExecutor.shutdownNow();
			logger.debug("End: shutdown reserveSeatsPoolExecutor");
			TheaterConstants.IS_RESERVE_SEAT_POOL_EXECUTOR_COMPLETE = true;

			logger.debug("Start: shutdown numberOfSeatsAvailablePoolExecutor");
			numberOfSeatsAvailablePoolExecutor.shutdown();
			numberOfSeatsAvailablePoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			numberOfSeatsAvailablePoolExecutor.shutdownNow();
			logger.debug("End: shutdown numberOfSeatsAvailablePoolExecutor");
			TheaterConstants.IS_NUM_SEATS_AVAIL_POOL_EXECUTOR_COMPLETE = true;

		} catch (InterruptedException e) {
			logger.error("error while shutting down thread pool executor", e);
		}
	}
}
