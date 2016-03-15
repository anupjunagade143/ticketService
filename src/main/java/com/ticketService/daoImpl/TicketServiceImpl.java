package com.ticketService.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.constants.TheaterConstants;
import com.ticketService.domain.Optional;
import com.ticketService.domain.Seat;
import com.ticketService.domain.SeatHold;
import com.ticketService.interfaces.serviceDao.ITicketService;
import com.ticketService.syncOperations.SeatSynchronizeOperation;

/**
 * Implementing all operations like calculating available seats, find and hold seats, reserve final seats for User
 * 
 * @author ajunaga
 *
 */
public class TicketServiceImpl implements ITicketService {

	private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class.getSimpleName());

	@Autowired
	@Qualifier("seatSynchronizeOperation")
	private SeatSynchronizeOperation seatSynchronizeOperation;

	/*
	 * Calculate the number of seats available as per theater level
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ticketService.interfaces.serviceDao.ITicketService#numSeatsAvailable(com.ticketService.domain.Optional)
	 */
	@Override
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		int level = venueLevel.getLevel();
		int availableSeats = 0;
		if (level == TheaterConstants.DEFAULT_LEVEL) {
			availableSeats = seatSynchronizeOperation.calculateAvailableSeatsInAllLevel();
		} else {
			availableSeats = seatSynchronizeOperation.calculateAvailableSeatsInSelectedLevel(level);
		}
		logger.debug("available seats:" + availableSeats + "  at venue-level:" + venueLevel);
		return availableSeats;
	}

	/*
	 * Find and hold the seats as per user input within min-level and max-level (non-Javadoc)
	 * 
	 * @see com.ticketService.interfaces.serviceDao.ITicketService#findAndHoldSeats(int,
	 * com.ticketService.domain.Optional, com.ticketService.domain.Optional, java.lang.String)
	 */
	@Override
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {

		SeatHold seatHold = null;
		List<Seat> holdSeatList = new ArrayList<Seat>();
		int holdSeatCount = 0;
		if (minLevel.getLevel() <= maxLevel.getLevel()) {
			/* if someone has preference for orchestra and if not then balcony2 */
			for (int level = minLevel.getLevel(); level <= maxLevel.getLevel(); level++) {
				holdSeatList.addAll(seatSynchronizeOperation.findAndHoldSeats(level, numSeats));
				holdSeatCount += holdSeatList.size();
				if (holdSeatCount == numSeats) {
					break;
				}
			}
		} else {
			/* if someone has preference for balcony2 and if not then orchestra */
			for (int level = minLevel.getLevel(); level >= maxLevel.getLevel(); level--) {
				holdSeatList.addAll(seatSynchronizeOperation.findAndHoldSeats(level, numSeats));
				holdSeatCount += holdSeatList.size();
				if (holdSeatCount == numSeats) {
					break;
				}
			}
		}
		if (CollectionUtils.isEmpty(holdSeatList)) {
			TheaterConstants.REJECT_SEAT_RESERVATION_COUNT.addAndGet(1);
			StringBuilder failMsgBuilder = new StringBuilder();
			failMsgBuilder.append("Sorry.Currently No seats available\n");
			failMsgBuilder.append("customer-emailId:").append(customerEmail);
			failMsgBuilder.append("Order detail:\n");
			failMsgBuilder.append("Seat-request:").append(numSeats);
			failMsgBuilder.append("MinLevel:").append(minLevel);
			failMsgBuilder.append("MaxLevel:").append(maxLevel);
			logger.info(failMsgBuilder.toString());
		} else {
			seatHold = populateSeatHoldObject(holdSeatList, customerEmail);
		}
		return seatHold;
	}

	/*
	 * Create a success message when seat is reserved.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ticketService.interfaces.serviceDao.ITicketService#reserveSeats(int, java.lang.String)
	 */
	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		StringBuilder successMsg = new StringBuilder();
		successMsg.append("Seat booked successfully:").append("\n").append("Seat Confirmation/Hold-Id:").append(
				seatHoldId);
		return successMsg.toString();
	}

	/**
	 * populate the seatHold object which will be later used for confirm/reject the seat booking
	 * 
	 * @param holdSeatList
	 * @param customerEmail
	 * @return
	 */
	private SeatHold populateSeatHoldObject(List<Seat> holdSeatList, String customerEmail) {
		SeatHold seatHold = new SeatHold();
		seatHold.setSeatList(holdSeatList);
		seatHold.setCustomerEmail(customerEmail);
		seatHold.setSeatHoldId(TheaterConstants.SEAT_HOLD_ID.addAndGet(1));
		return seatHold;
	}
}
