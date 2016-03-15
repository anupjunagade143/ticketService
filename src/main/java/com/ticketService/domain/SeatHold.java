package com.ticketService.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Once seat is found , system holds the seats for customer
 * 
 * @author ajunaga
 *
 */
public class SeatHold implements Serializable {

	private static final long serialVersionUID = 2290322398378029888L;

	private Integer seatHoldId;
	private String customerEmail;
	private List<Seat> seatList;

	public Integer getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(Integer seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public List<Seat> getSeatList() {
		return seatList;
	}

	public void setSeatList(List<Seat> seatList) {
		this.seatList = seatList;
	}

	@Override
	public String toString() {
		return String.format("SeatHold [seatHoldId=%s, customerEmail=%s, seatList=%s]", seatHoldId, customerEmail,
				seatList);
	}

}
