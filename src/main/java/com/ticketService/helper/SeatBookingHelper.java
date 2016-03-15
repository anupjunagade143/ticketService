package com.ticketService.helper;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ticketService.constants.TheaterConstants;
import com.ticketService.domain.Seat;
import com.ticketService.domain.TheaterLevel;

/**
 * This class will populate static details for every seat located at every level
 * 
 * @author ajunaga
 *
 */
public class SeatBookingHelper {

	private static final Logger logger = LoggerFactory.getLogger(SeatBookingHelper.class.getSimpleName());

	/**
	 * This will load static data specific to a theater like level details, seat details , total seat count
	 * 
	 * @param theaterLevel
	 */
	public void populateSeatDetails(TheaterLevel theaterLevel) {
		List<Seat> seatList;
		seatList = loadBasicSeatDetail(theaterLevel);
		theaterLevel.addAllSeatList(seatList);
		TheaterConstants.TOTAL_SEATS_IN_THEATER += seatList.size();
		logger.info("Level-id:" + theaterLevel.getLevelId() + ". Total seats:" + seatList.size());
	}

	/**
	 * Will build a details specific to a seat like seat-number, row-number.
	 * 
	 * @param theaterLevel
	 * @return
	 */
	private List<Seat> loadBasicSeatDetail(TheaterLevel theaterLevel) {
		List<Seat> seatList = new LinkedList<Seat>();

		int seatIncr = 1;
		int seatCounter = 1;
		int rowCounter = 1;
		int totalSeatsInRow = theaterLevel.getSeats();
		theaterLevel.getTotalSeats();
		theaterLevel.getFreeSeats();

		while (seatCounter < theaterLevel.getTotalSeats()) {
			Seat seat = new Seat(theaterLevel.getLevelId(), rowCounter, seatCounter);
			seatList.add(seat);
			if (seatCounter == totalSeatsInRow) {
				seatCounter = 1;
				rowCounter++;
			} else {
				seatCounter++;
			}

			if (seatIncr == theaterLevel.getTotalSeats()) {
				break;
			}
			seatIncr++;
		}
		return seatList;
	}

}
