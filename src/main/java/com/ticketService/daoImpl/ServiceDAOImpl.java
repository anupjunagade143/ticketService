package com.ticketService.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.constants.TheaterConstants;
import com.ticketService.domain.Theater;
import com.ticketService.domain.TheaterLevel;
import com.ticketService.interfaces.dao.ITheatreDetailDAO;
import com.ticketService.interfaces.serviceDao.IServiceDAO;

/**
 * Service DAO implementation where data from multiple sources will be populated will populated to load Theater object
 * 
 * @author ajunaga
 *
 */
public class ServiceDAOImpl implements IServiceDAO {

	private static final Logger logger = LoggerFactory.getLogger(ServiceDAOImpl.class.getSimpleName());

	@Autowired
	@Qualifier("ITheatreBalcony1")
	private ITheatreDetailDAO ITheatreBalcony1;

	@Autowired
	@Qualifier("ITheatreBalcony2")
	private ITheatreDetailDAO ITheatreBalcony2;

	@Autowired
	@Qualifier("ITheatreMain")
	private ITheatreDetailDAO ITheatreMain;

	@Autowired
	@Qualifier("ITheatreOrchestra")
	private ITheatreDetailDAO ITheatreOrchestra;

	@Autowired
	@Qualifier("balcony1")
	private TheaterLevel balcony1;

	@Autowired
	@Qualifier("balcony2")
	private TheaterLevel balcony2;

	@Autowired
	@Qualifier("main")
	private TheaterLevel main;

	@Autowired
	@Qualifier("orchestra")
	private TheaterLevel orchestra;

	@Autowired
	@Qualifier("theater")
	private Theater theater;

	/*
	 * populate theater level details for balcony1,balcony2,main,orchestra
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ticketService.interfaces.serviceDao.IServiceDAO#populateTheaterDetails()
	 */
	@Override
	public void populateTheaterDetails() {
		logger.info("Loading theater seat details.....");
		balcony1 = ITheatreBalcony1.populateTheaterDetails();
		balcony2 = ITheatreBalcony2.populateTheaterDetails();
		main = ITheatreMain.populateTheaterDetails();
		orchestra = ITheatreOrchestra.populateTheaterDetails();

		theater.setBalcony1(balcony1);
		theater.setBalcony2(balcony2);
		theater.setMain(main);
		theater.setOrchestra(orchestra);

		logger.info("Theater details loaded successfully");
		logger.info("Total available seats in Theater:" + TheaterConstants.TOTAL_SEATS_IN_THEATER);
	}
}
