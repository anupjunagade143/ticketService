package com.ticketService.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.constants.TheaterConstants;
import com.ticketService.factory.ReportFormatFactory;
import com.ticketService.interfaces.dao.IReportFormat;

/**
 * Report will generated specific to customer booking details
 * 
 * @author ajunaga
 *
 */
public class OrderReportWriter {

	@Autowired
	@Qualifier("reportFormatFactory")
	private ReportFormatFactory reportFormatFactory;

	private static final Logger logger = LoggerFactory.getLogger(OrderReportWriter.class.getSimpleName());

	@Autowired
	@Qualifier("textIReportFormat")
	private IReportFormat textIReportFormat;

	/**
	 * Report to write customer order details
	 */
	public void writeCustomerOrderReport() {
		textIReportFormat = reportFormatFactory.getReportFormat(TheaterConstants.TEXT_FORMAT);
		textIReportFormat.writeReportFormat();
	}

}
