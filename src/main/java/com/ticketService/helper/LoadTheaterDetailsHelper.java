package com.ticketService.helper;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.interfaces.serviceDao.IServiceDAO;

/**
 * Load theater details like levels, prices , seat arrangement etc.
 * 
 * @author ajunaga
 *
 */
public class LoadTheaterDetailsHelper implements InitializingBean {

	@Autowired
	@Qualifier("serviceDAO")
	private IServiceDAO serviceDAO;

	/**
	 * This method will populate theater details
	 */
	public void afterPropertiesSet() throws Exception {
		serviceDAO.populateTheaterDetails();
	}

}
