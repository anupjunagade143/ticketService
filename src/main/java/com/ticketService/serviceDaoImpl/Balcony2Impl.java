package com.ticketService.serviceDaoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.constants.TheaterEnum;
import com.ticketService.domain.TheaterLevel;
import com.ticketService.helper.SeatBookingHelper;
import com.ticketService.interfaces.dao.ITheatreDetailDAO;

/**
 * Load Balcony2 level details
 * 
 * @author ajunaga
 *
 */
public class Balcony2Impl implements ITheatreDetailDAO {

	@Autowired
	@Qualifier("seatBookingHelper")
	private SeatBookingHelper seatBookingHelper;

	@Autowired
	@Qualifier("balcony2")
	private TheaterLevel balcony2;

	/*
	 * populate balcony details. It can be later configured to a populate it through service interface
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ticketService.interfaces.dao.ITheatreDetailDAO#populateTheaterDetails()
	 */
	@Override
	public TheaterLevel populateTheaterDetails() {
		balcony2 = new TheaterLevel(TheaterEnum.BALCONY2.getValue(), TheaterEnum.BALCONY2.name(), 40f, 15, 100);
	//	balcony2 = new TheaterLevel(TheaterEnum.BALCONY2.getValue(), TheaterEnum.BALCONY2.name(), 40f, 3, 8);
		seatBookingHelper.populateSeatDetails(balcony2);
		return balcony2;
	}

}
