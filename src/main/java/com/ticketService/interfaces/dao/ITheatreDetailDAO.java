package com.ticketService.interfaces.dao;

import com.ticketService.domain.TheaterLevel;

/**
 * Populate theater details for every level like balcony, orchestra etc
 * 
 * @author ajunaga
 *
 */
public interface ITheatreDetailDAO {

	public TheaterLevel populateTheaterDetails();
}
