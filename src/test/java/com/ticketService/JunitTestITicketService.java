package com.ticketService;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ticketService.common.ExecuteTicketService;
import com.ticketService.constants.TheaterConstants;
import com.ticketService.domain.Optional;
import com.ticketService.domain.SeatHold;
import com.ticketService.domain.Theater;
import com.ticketService.interfaces.serviceDao.ITicketService;
import com.ticketService.syncOperations.SeatSynchronizeOperation;

/**
 * TestNG to test methods implemented in ITicketService interface. Test cases are executed in a predefined order to
 * address multiple scenarios like num-of-seats-available, findAndBookSeats, reserveSeats ,
 * seatAvailablePostConfirmReservation etc.
 * 
 * @author ajunaga
 *
 */

// TestNG COMMAND:: mvn -Dtest=JunitTestITicketService test -Dlogback.configurationFile=ticket-service-logback.xml

public class JunitTestITicketService {

	private static final Logger logger = LoggerFactory.getLogger(JunitTestITicketService.class.getSimpleName());

	private ExecuteTicketService execTicketService = new ExecuteTicketService();

	private int TOTAL_EXPECTED_SEAT_IN_THEATER = 6250;
	private int TOTAL_EXPECTED_SEAT_IN_THEATER_EXCLUDE_ONHOLD = 6248;

	private static ConfigurableApplicationContext context = null;

	private static Theater theater;
	private static ITicketService iTicketService;
	private static SeatSynchronizeOperation seatSynchronizeOperation;

	private static SeatHold seatHold;

	/**
	 * Test case to ensure whether spring context is loaded without error/exceptions
	 */
	@Test(priority = 1)
	public void loadApplicationContext() {
		boolean isContextLoaded = false;
		if (context == null) {
			execTicketService.initializeContext("ticketService-context.xml");
			context = execTicketService.getContext();
			iTicketService = (ITicketService) context.getBean("iTicketService");
			theater = (Theater) context.getBean("theater");
			seatSynchronizeOperation = (SeatSynchronizeOperation) context.getBean("seatSynchronizeOperation");
			isContextLoaded = true;
		}
		Assert.assertEquals(true, isContextLoaded);
		logger.debug("test-case execution complete for loadApplicationContext()");
	}

	/**
	 * Count the overall seat count in whole theater.
	 */
	@Test(priority = 2)
	public void numSeatsAvailableInitialCount() {
		int actulalSeatCountInTheater = iTicketService.numSeatsAvailable(getOptionalLevel(TheaterConstants.DEFAULT_LEVEL));
		Assert.assertEquals(TOTAL_EXPECTED_SEAT_IN_THEATER, actulalSeatCountInTheater);
		logger.debug("test-case execution complete for numSeatsAvailableInitialCount()");
	}

	/**
	 * To test this case, initially we will put some seats on hold and after that we will be find total available
	 * seats(exclude onHold and booked seats)
	 */
	@Test(priority = 3)
	public void numSeatsAvailableIgnoreOnHoldSeats() {
		updateSeatStatus(true);
		int actulalSeatCountAvailableIgnoringOnHoldSeats = iTicketService.numSeatsAvailable(getOptionalLevel(TheaterConstants.DEFAULT_LEVEL));
		Assert.assertEquals(TOTAL_EXPECTED_SEAT_IN_THEATER_EXCLUDE_ONHOLD, actulalSeatCountAvailableIgnoringOnHoldSeats);
		logger.debug("test-case execution complete for numSeatsAvailableIgnoreOnHoldSeats()");
	}

	/**
	 * This test case is executed after numSeatsAvailableIgnoreOnHoldSeats().
	 * 
	 * To test this test-case, we will cancel the onHold seats(i.e. onHold = false) , which means such seats are back in
	 * available seatList and can be booked by other users.
	 */
	@Test(priority = 4)
	public void numSeatsAvailableWhenOnHoldRequestIsCancelled() {
		updateSeatStatus(false);
		int actulalSeatCountInTheaterIgnoringOnHoldSeats = iTicketService.numSeatsAvailable(getOptionalLevel(TheaterConstants.DEFAULT_LEVEL));
		Assert.assertEquals(TOTAL_EXPECTED_SEAT_IN_THEATER, actulalSeatCountInTheaterIgnoringOnHoldSeats);
		logger.debug("test-case execution complete for numSeatsAvailableWhenOnHoldRequestIsCancelled()");
	}

	/**
	 * Test case to put some seats on hold.
	 */
	@Test(priority = 5)
	public void findAndHoldSeats() {
		int numOfSeats = 2;
		Optional<Integer> minLevel = getOptionalLevel(1);
		Optional<Integer> maxLevel = getOptionalLevel(2);
		seatHold = iTicketService.findAndHoldSeats(numOfSeats, minLevel, maxLevel, "sampleEmail@myGmail.com");
		boolean isOnHold = seatHold.getSeatList().get(0).isOnHold().get();
		Assert.assertEquals(true, isOnHold);
		logger.debug("test-case execution complete for findAndHoldSeats()");
	}

	/**
	 * This test case is executed after findAndHoldSeats()
	 * 
	 * Count total available seats(exclude onHold and booked seats)
	 */
	@Test(priority = 6)
	public void numOfSeatsAfterHoldOperation() {
		int actulalSeatCountAvailableIgnoringOnHoldSeats = iTicketService.numSeatsAvailable(getOptionalLevel(TheaterConstants.DEFAULT_LEVEL));
		Assert.assertEquals(TOTAL_EXPECTED_SEAT_IN_THEATER_EXCLUDE_ONHOLD, actulalSeatCountAvailableIgnoringOnHoldSeats);
		logger.debug("test-case execution complete for numOfSeatsAfterHoldOperation()");
	}

	/**
	 * Seat booking is finally confirmed , so seat status will be updated to "isBooked = true"
	 */
	@Test(priority = 7)
	public void reserveSeat() {
		seatSynchronizeOperation.reserveSeats(seatHold.getSeatList(), seatHold.getSeatHoldId());
		Assert.assertEquals(true, seatHold.getSeatList().get(0).isBooked().get());
		logger.debug("test-case execution complete for reserveSeat()");
	}

	/**
	 * Method to get optional theater-level
	 * 
	 * @param inputLevel
	 * @return
	 */
	private Optional<Integer> getOptionalLevel(int inputLevel) {
		Optional<Integer> level = null;
		if (inputLevel == TheaterConstants.DEFAULT_LEVEL) {
			level = new Optional<Integer>(TheaterConstants.DEFAULT_LEVEL);
		} else {
			level = new Optional<Integer>(inputLevel);
		}
		return level;
	}

	/**
	 * Method to update seat status for testing purpose.
	 * 
	 * @param isOnHold
	 */
	private void updateSeatStatus(boolean isOnHold) {
		theater.getOrchestra().getSeatList().get(0).setOnHold(new AtomicBoolean(isOnHold));
		theater.getBalcony1().getSeatList().get(0).setOnHold(new AtomicBoolean(isOnHold));
		logger.debug("test-case execution complete for updateSeatStatus()");
	}

}
