package com.ticketService.domain;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

/**
 * Every individual seat-level like balcony, orchestra etc
 * 
 * @author ajunaga
 *
 */
public class TheaterLevel extends TheaterDetails {

	private static final long serialVersionUID = -6329514567097177772L;

	public TheaterLevel() {
		super();
	}

	public TheaterLevel(Integer levelId, String levelName, Float price, Integer rows, Integer seats) {
		super(levelId, levelName, price, rows, seats);
	}

	/* get total seats for that particular level */
	private Integer totalSeats;

	/* total available seats */
	private Integer freeSeats;

	/**/
	private List<Seat> seatList;

	public List<Seat> getSeatList() {
		return seatList;
	}

	public void addAllSeatList(List<Seat> seatList) {
		if (CollectionUtils.isEmpty(this.seatList)) {
			// linked-list used to maintain order of seating arrangement
			this.seatList = new LinkedList<>();
		}
		this.seatList.addAll(seatList);
	}

	/* total rows * seats in every row */
	public Integer getTotalSeats() {
		if (this.totalSeats == null) {
			this.totalSeats = getRows() * getSeats();
		}
		return totalSeats;
	}

	/* initially all seats are free seats */
	public Integer getFreeSeats() {
		this.freeSeats = this.totalSeats;
		return freeSeats;
	}

	@Override
	public String toString() {
		return String.format("TheaterLevel [totalSeats=%s, freeSeats=%s, seatList=%s]", totalSeats, freeSeats, seatList);
	}

}
