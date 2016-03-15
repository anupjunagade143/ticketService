package com.ticketService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ticketService.common.ExecuteTicketService;
import com.ticketService.constants.TheaterConstants;

/**
 * TestNG unit test cases to execute entire TicketService App..
 * 
 * @author ajunaga
 *
 */

// TestNG COMMAND:: mvn -Dtest=JunitTicketServiceApp test -Dlogback.configurationFile=ticket-service-logback.xml

public class JunitTicketServiceApp {

	private static final Logger logger = LoggerFactory.getLogger(JunitTicketServiceApp.class.getSimpleName());

	private ConfigurableApplicationContext context;

	ExecuteTicketService execTicketService = new ExecuteTicketService();

	/**
	 * As many user request are processed in multi-threaded way, some of the requests will be able to findAndHold seats
	 * and can finally book it, however some of the user request will be rejected/denied/cancelled. In such case test
	 * case should ensure that all user requests were processed.
	 * 
	 * Basically, All_USER_REQUEST = CONFIRM_SEAT_RESERVATION_COUNT + REJECT_SEAT_RESERVATION_COUNT;
	 * 
	 * NOTE:- While executing please be patient, since this test case will take around 1 minute to complete
	 */
	@Test
	public void validateSeatOrderStatistics() {
		execTicketService.executeTicketService("ticketService-context.xml");
		context = execTicketService.getContext();
		int totalSeatOrderCount = (Integer) context.getBean("randomOrderCount");
		Integer totalConfirmedOrderCount = TheaterConstants.CONFIRM_SEAT_RESERVATION_COUNT.intValue();
		Integer totalRejectedOrderCount = TheaterConstants.REJECT_SEAT_RESERVATION_COUNT.intValue();
		Assert.assertEquals(totalSeatOrderCount, (totalConfirmedOrderCount + totalRejectedOrderCount));
		logger.debug("test-case execution complete for validateSeatOrderStatistics()");
	}
}
