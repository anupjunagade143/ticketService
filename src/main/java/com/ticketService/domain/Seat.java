package com.ticketService.domain;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Seat object which is mapped to Theater
 * 
 * @author ajunaga
 *
 */
public class Seat implements Serializable {

	private static final long serialVersionUID = -2580969613856441344L;

	private int levelId;
	private int rowNum;
	private int seatNum;
	private AtomicBoolean isBooked = new AtomicBoolean(false);
	private AtomicBoolean isOnHold = new AtomicBoolean(false);

	public Seat(int levelId, int rowNum, int seatNum) {
		super();
		this.levelId = levelId;
		this.rowNum = rowNum;
		this.seatNum = seatNum;
	}

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	public AtomicBoolean isBooked() {
		return isBooked;
	}

	public void setBooked(AtomicBoolean isBooked) {
		this.isBooked = isBooked;
	}

	public AtomicBoolean isOnHold() {
		return isOnHold;
	}

	public void setOnHold(AtomicBoolean isOnHold) {
		this.isOnHold = isOnHold;
	}

	@Override
	public String toString() {
		return String.format("Seat [levelId=%s, rowNum=%s, seatNum=%s, isBooked=%s, isOnHold=%s]", levelId, rowNum,
				seatNum, isBooked, isOnHold);
	}

}
