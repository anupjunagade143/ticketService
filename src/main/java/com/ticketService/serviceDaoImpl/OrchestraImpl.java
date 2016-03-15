package com.ticketService.serviceDaoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.constants.TheaterEnum;
import com.ticketService.domain.TheaterLevel;
import com.ticketService.helper.SeatBookingHelper;
import com.ticketService.interfaces.dao.ITheatreDetailDAO;

/**
 * Load Orchestra level details
 * 
 * @author ajunaga
 *
 */
public class OrchestraImpl implements ITheatreDetailDAO {

	@Autowired
	@Qualifier("seatBookingHelper")
	private SeatBookingHelper seatBookingHelper;

	@Autowired
	@Qualifier("orchestra")
	private TheaterLevel orchestra;

	/*
	 * populate orchestra details. It can be later configured to a populate it through service interface
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ticketService.interfaces.dao.ITheatreDetailDAO#populateTheaterDetails()
	 */
	@Override
	public TheaterLevel populateTheaterDetails() {
		orchestra = new TheaterLevel(TheaterEnum.ORCHESTRA.getValue(), TheaterEnum.ORCHESTRA.name(), 100f, 25, 50);
	//	orchestra = new TheaterLevel(TheaterEnum.ORCHESTRA.getValue(), TheaterEnum.ORCHESTRA.name(), 100f, 6, 3);
		seatBookingHelper.populateSeatDetails(orchestra);
		return orchestra;
	}

}
