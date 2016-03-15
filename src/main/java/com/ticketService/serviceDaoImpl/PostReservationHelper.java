package com.ticketService.serviceDaoImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.domain.ConfirmedSeatOrder;
import com.ticketService.domain.CustomerConfirmationOrder;
import com.ticketService.domain.Seat;

/**
 * Post-Reservation/Email-utility to inform customer once seats are booked and order is confirmed.
 * 
 * @author ajunaga
 *
 */
public class PostReservationHelper {

	private static final Logger logger = LoggerFactory.getLogger(PostReservationHelper.class.getSimpleName());

	@Autowired
	@Qualifier("confirmedSeatOrder")
	private ConfirmedSeatOrder confirmedSeatOrder;

	/**
	 * Compose email object
	 * 
	 * @param seatHoldId
	 * @param seatList
	 * @param successMsg
	 * @param emailId
	 * @return
	 */
	public CustomerConfirmationOrder composeEmail(Integer seatHoldId, List<Seat> seatList, String successMsg,
			String emailId) {
		CustomerConfirmationOrder customerConfirmOrder = new CustomerConfirmationOrder();
		customerConfirmOrder.setSeatHoldId(seatHoldId);
		customerConfirmOrder.setMsg(successMsg);
		customerConfirmOrder.setSeatList(seatList);
		customerConfirmOrder.setTotalReservedSeatCount(seatList.size());
		customerConfirmOrder.setEmailId(emailId);
		return customerConfirmOrder;
	}

	/**
	 * Send email to customer. Order added to a map which will be later used for reporting purpose
	 * 
	 * @param confirmOrder
	 */
	public void sendEmail(CustomerConfirmationOrder confirmOrder) {
		// todo: send an email to customer about seat order confirmation. Need SMTP for email[not implemented]
		confirmedSeatOrder.addConfirmOrderMap(confirmOrder.getSeatHoldId(), confirmOrder);
	}

}
