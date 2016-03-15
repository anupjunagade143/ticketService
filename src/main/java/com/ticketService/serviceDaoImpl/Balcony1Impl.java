package com.ticketService.serviceDaoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.constants.TheaterEnum;
import com.ticketService.domain.TheaterLevel;
import com.ticketService.helper.SeatBookingHelper;
import com.ticketService.interfaces.dao.ITheatreDetailDAO;

/**
 * Load Balcony1 level details
 * 
 * @author ajunaga
 *
 */
public class Balcony1Impl implements ITheatreDetailDAO {

	@Autowired
	@Qualifier("seatBookingHelper")
	private SeatBookingHelper seatBookingHelper;

	@Autowired
	@Qualifier("balcony1")
	private TheaterLevel balcony1;

	/*
	 * populate balcony details. It can be later configured to a populate it through service interface
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ticketService.interfaces.dao.ITheatreDetailDAO#populateTheaterDetails()
	 */
	@Override
	public TheaterLevel populateTheaterDetails() {
		balcony1 = new TheaterLevel(TheaterEnum.BALCONY1.getValue(), TheaterEnum.BALCONY1.name(), 50f, 15, 100);
	//	balcony1 = new TheaterLevel(TheaterEnum.BALCONY1.getValue(), TheaterEnum.BALCONY1.name(), 50f, 2, 7);
		seatBookingHelper.populateSeatDetails(balcony1);
		return balcony1;
	}

}
