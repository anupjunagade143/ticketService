package com.ticketService.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Customer booking details
 * 
 * @author ajunaga
 *
 */
public class CustomerConfirmationOrder implements Serializable {

	private static final long serialVersionUID = -698120493623919850L;

	private String msg;
	private Integer seatHoldId;
	private List<Seat> seatList;
	private Integer totalReservedSeatCount;
	private String emailId;

	public Integer getTotalReservedSeatCount() {
		return totalReservedSeatCount;
	}

	public void setTotalReservedSeatCount(Integer totalReservedSeatCount) {
		this.totalReservedSeatCount = totalReservedSeatCount;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(Integer seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

	public List<Seat> getSeatList() {
		return seatList;
	}

	public void setSeatList(List<Seat> seatList) {
		this.seatList = seatList;
	}

	@Override
	public String toString() {
		return String.format(
				"CustomerConfirmationOrder [msg=%s, seatHoldId=%s, seatList=%s, totalReservedSeatCount=%s, emailId=%s]",
				msg, seatHoldId, seatList, totalReservedSeatCount, emailId);
	}

}
