package com.ticketService.domain;

import java.io.Serializable;

/**
 * This class is directly mapped to Theater details like name,rows, prices etc. Static data is populated only using
 * constructor.
 * 
 * @author ajunaga
 *
 */
public class TheaterDetails implements Serializable {

	private static final long serialVersionUID = 5986945475467199280L;

	/*
	 * Note:- Only get*() methods available to access data. If need to set an object use constructor. No set*() method
	 * added intentionally so as to avoid any unintentional update of constant value.
	 */
	private Integer levelId;
	private String levelName;
	private Float price;
	private Integer rows;
	private Integer seats;

	public TheaterDetails() {

	}

	public TheaterDetails(Integer levelId, String levelName, Float price, Integer rows, Integer seats) {
		super();
		this.levelId = levelId;
		this.levelName = levelName;
		this.price = price;
		this.rows = rows;
		this.seats = seats;

	}

	public Integer getLevelId() {
		return levelId;
	}

	public String getLevelName() {
		return levelName;
	}

	public Float getPrice() {
		return price;
	}

	public Integer getRows() {
		return rows;
	}

	public Integer getSeats() {
		return seats;
	}

	@Override
	public String toString() {
		return String.format("TheaterDetails [levelId=%s, levelName=%s, price=%s, rows=%s, seats=%s]", levelId,
				levelName, price, rows, seats);
	}

}
