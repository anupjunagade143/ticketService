package com.ticketService.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ticketService.constants.TheaterConstants;
import com.ticketService.executor.ServiceThreadPoolExecutor;
import com.ticketService.writer.OrderReportWriter;

/**
 * Main class
 * 
 * @author ajunaga
 *
 */
public class ExecuteTicketService {

	private static final Logger logger = LoggerFactory.getLogger(ExecuteTicketService.class.getSimpleName());
	private ConfigurableApplicationContext context = null;

	public static void main(String[] args) {

		ExecuteTicketService service = new ExecuteTicketService();

		if (args.length < 1) {
			logger.error("At least 1 argument is required: context file name is required");
			System.exit(1);
		}

		String contextFile = args[0];
		service.executeTicketService(contextFile);
	}

	/*
	 * execute context load, thread pool
	 */
	public void executeTicketService(String contextFile) {
		initializeContext(contextFile);
		ServiceThreadPoolExecutor serviceThreadPoolExecutor = (ServiceThreadPoolExecutor) context.getBean("serviceThreadPoolExecutor");
		serviceThreadPoolExecutor.initializeThreadPoolExecutors();
		serviceThreadPoolExecutor.shutdown();
		logger.info("All orders processed");
		OrderReportWriter orderReportWriter = (OrderReportWriter) context.getBean("orderReportWriter");
		logger.info("Report generation for Customer Seat booking");
		orderReportWriter.writeCustomerOrderReport();

		logger.info("Total Dummy order count:" + context.getBean("randomOrderCount"));
		logger.info("Total confirmed orders:" + TheaterConstants.CONFIRM_SEAT_RESERVATION_COUNT);
		logger.info("Total rejected orders:" + TheaterConstants.REJECT_SEAT_RESERVATION_COUNT);

		logger.info("Seats booked at Orchestra level:" + TheaterConstants.TOTAL_ORCHESTRA_LEVEL_SEAT_BOOKED_COUNT);
		logger.info("Seats booked at Main level:" + TheaterConstants.TOTAL_MAIN_LEVEL_SEAT_BOOKED_COUNT);
		logger.info("Seats booked at Balcony1 level:" + TheaterConstants.TOTAL_BALCONY1_LEVEL_SEAT_BOOKED_COUNT);
		logger.info("Seats booked at Balcony2 level:" + TheaterConstants.TOTAL_BALCONY2_LEVEL_SEAT_BOOKED_COUNT);
		logger.info("Seats booked in whole Theater:" + TheaterConstants.TOTAL_SEATS_BOOKED_IN_THEATER_COUNT);
	}

	/**
	 * Load Spring context file
	 * 
	 * @param contextFile
	 */
	public void initializeContext(String contextFile) {
		try {
			this.context = new ClassPathXmlApplicationContext(contextFile);
			this.context.getAutowireCapableBeanFactory().autowireBeanProperties(this,
					AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
			logger.info("spring context loaded successfully");
		} catch (Exception e) {
			logger.error("exception loading context file:", e);
			System.exit(1);
		}
	}

	public ConfigurableApplicationContext getContext() {
		return context;
	}
}
