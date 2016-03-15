package com.ticketService.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.constants.TheaterConstants;
import com.ticketService.interfaces.dao.IReportFormat;

/**
 * Factory to address mutliple formats in which report will be created.
 * 
 * @author ajunaga
 *
 */
public class ReportFormatFactory {

	@Autowired
	@Qualifier("textIReportFormat")
	private IReportFormat textIReportFormat;

	@Autowired
	@Qualifier("velocityIReportFormat")
	private IReportFormat velocityIReportFormat;

	@Autowired
	@Qualifier("pdfIReportFormat")
	private IReportFormat pdfIReportFormat;

	/**
	 * Returns a reportWriter object on basis of input type
	 * 
	 * @param format
	 * @return
	 */
	public IReportFormat getReportFormat(String format) {

		switch (format) {
		case TheaterConstants.TEXT_FORMAT:
			return textIReportFormat;

		case TheaterConstants.PDF_FORMAT:
			return pdfIReportFormat;

		case TheaterConstants.VELOCITY_FORMAT:
			return velocityIReportFormat;

		default:
			return textIReportFormat;
		}
	}

}
