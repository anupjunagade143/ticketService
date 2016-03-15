package com.ticketService.daoImpl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.constants.TheaterEnum;
import com.ticketService.domain.ConfirmedSeatOrder;
import com.ticketService.domain.CustomerConfirmationOrder;
import com.ticketService.domain.Seat;
import com.ticketService.interfaces.dao.IReportFormat;

/**
 * Implementation specific to TEXT report details
 * 
 * @author ajunaga
 *
 */
public class TextReportFormatImpl implements IReportFormat {

	private static final Logger logger = LoggerFactory.getLogger(TextReportFormatImpl.class.getSimpleName());

	@Autowired
	@Qualifier("confirmedSeatOrder")
	private ConfirmedSeatOrder confirmedSeatOrder;

	@Autowired
	@Qualifier("orderConfirmationReportNameText")
	private String orderConfirmationReportNameText;

	/*
	 * Generate report in text format.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ticketService.interfaces.dao.IReportFormat#writeReportFormat()
	 */
	@Override
	public void writeReportFormat() {

		File reportFile = new File(orderConfirmationReportNameText);
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(reportFile, false);
			bw = new BufferedWriter(fw);

			Map<Integer, CustomerConfirmationOrder> confirmOrderMap = confirmedSeatOrder.getConfirmOrderMap();
			StringBuilder reportBuilder = null;

			int orderCounter = 1;
			for (Entry<Integer, CustomerConfirmationOrder> order : confirmOrderMap.entrySet()) {

				reportBuilder = new StringBuilder();
				reportBuilder.append("\n\n========").append("Order-number:").append(orderCounter).append(
						"=============");
				reportBuilder.append("\nMessage:").append(order.getValue().getMsg());
				reportBuilder.append("\ncustomer-email-id:").append(order.getValue().getEmailId());
				reportBuilder.append("\nTotal-Seats-Reserved-Count:").append(
						order.getValue().getTotalReservedSeatCount());
				for (Seat seatDetail : order.getValue().getSeatList()) {
					reportBuilder.append("\n*********seat-details***********");
					reportBuilder.append("\nLevel-id:").append(seatDetail.getLevelId());
					reportBuilder.append("\nLevel-Name:").append(TheaterEnum.getLevelName(seatDetail.getLevelId()));
					reportBuilder.append("\nRow-number:").append(seatDetail.getRowNum());
					reportBuilder.append("\nSeat-number:").append(seatDetail.getSeatNum());
				}

				bw.write(reportBuilder.toString());
				orderCounter++;
			}
			bw.close();
			fw.close();
		} catch (IOException e) {
			logger.error("error while writing customer order report ", e);
		}
		logger.info("Seat booking report generated at:" + reportFile.getAbsolutePath());
	}
}
