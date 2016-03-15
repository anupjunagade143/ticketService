package com.ticketService.domain;

import java.io.Serializable;
import java.util.List;

/**
 * User seat request object
 * 
 * @author ajunaga
 *
 */
public class UserOrder implements Serializable {

	private static final long serialVersionUID = 662668826154683763L;

	private String customerName;
	private String customerEmail;
	private Optional<Integer> minLevel;
	private Optional<Integer> maxLevel;
	private Integer numSeats;

	private Integer seatHoldId;
	private List<Seat> seatList;

	public UserOrder(String customerEmail, Integer seatHoldId, List<Seat> seatList) {
		super();
		this.customerEmail = customerEmail;
		this.seatHoldId = seatHoldId;
		this.seatList = seatList;
	}

	public UserOrder(String customerName, String customerEmail, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			Integer numSeats) {
		super();
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.numSeats = numSeats;
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public void setNumSeats(Integer numSeats) {
		this.numSeats = numSeats;
	}

	public Optional<Integer> getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(Optional<Integer> minLevel) {
		this.minLevel = minLevel;
	}

	public Optional<Integer> getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(Optional<Integer> maxLevel) {
		this.maxLevel = maxLevel;
	}

	public Integer getNumSeats() {
		return numSeats;
	}

	@Override
	public String toString() {
		return String.format(
				"UserOrder [customerName=%s, customerEmail=%s, minLevel=%s, maxLevel=%s, numSeats=%s, seatHoldId=%s, seatList=%s]",
				customerName, customerEmail, minLevel, maxLevel, numSeats, seatHoldId, seatList);
	}

}
