package com.ticketService.serviceDaoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.constants.TheaterEnum;
import com.ticketService.domain.TheaterLevel;
import com.ticketService.helper.SeatBookingHelper;
import com.ticketService.interfaces.dao.ITheatreDetailDAO;

/**
 * Load Main level details
 * 
 * @author ajunaga
 *
 */
public class MainLevelImpl implements ITheatreDetailDAO {

	@Autowired
	@Qualifier("seatBookingHelper")
	private SeatBookingHelper seatBookingHelper;

	@Autowired
	@Qualifier("main")
	private TheaterLevel mainLevel;

	/*
	 * populate main level details. It can be later configured to a populate it through service interface
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ticketService.interfaces.dao.ITheatreDetailDAO#populateTheaterDetails()
	 */
	@Override
	public TheaterLevel populateTheaterDetails() {
		mainLevel = new TheaterLevel(TheaterEnum.MAIN.getValue(), TheaterEnum.MAIN.name(), 75f, 20, 100);
	//	mainLevel = new TheaterLevel(TheaterEnum.MAIN.getValue(), TheaterEnum.MAIN.name(), 75f, 4, 5);
		seatBookingHelper.populateSeatDetails(mainLevel);
		return mainLevel;
	}

}
