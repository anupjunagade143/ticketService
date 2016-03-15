package com.ticketService.constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Constants for Ticket Service Application
 * 
 * @author ajunaga
 *
 */
public class TheaterConstants {

	/* This denotes no specific level, so will combine all level from MIN_LEVEL to MAX_LEVEL */
	public static final Integer DEFAULT_LEVEL = -1;

	public static final Integer ORCHESTRA_LEVEL = 1;

	public static final Integer MAIN_LEVEL = 2;

	public static final Integer BALCONY1_LEVEL = 3;

	public static final Integer BALCONY2_LEVEL = 4;

	public static final List<Integer> THEATER_LEVEL_LIST = new ArrayList<Integer>() {
		private static final long serialVersionUID = -7939931794811589122L;

		{
			add(ORCHESTRA_LEVEL);
			add(MAIN_LEVEL);
			add(BALCONY1_LEVEL);
			add(BALCONY2_LEVEL);
		}
	};

	public static Integer TOTAL_SEATS_IN_THEATER = 0;

	public static final AtomicInteger SEAT_HOLD_ID = new AtomicInteger(0);

	public static Boolean IS_NUM_SEATS_AVAIL_POOL_EXECUTOR_COMPLETE = false;
	public static Boolean IS_FIND_AND_HOLD_SEATS_POOL_EXECUTOR_COMPLETE = false;
	public static Boolean IS_TICKET_HOLD_EXPIRE_POOL_EXECUTOR_COMPLETE = false;
	public static Boolean IS_RESERVE_SEAT_POOL_EXECUTOR_COMPLETE = false;

	public static final String PDF_FORMAT = "pdf";
	public static final String VELOCITY_FORMAT = "velocity";
	public static final String TEXT_FORMAT = "text";

	public static final AtomicInteger CONFIRM_SEAT_RESERVATION_COUNT = new AtomicInteger(0);
	public static final AtomicInteger REJECT_SEAT_RESERVATION_COUNT = new AtomicInteger(0);

	public static final AtomicInteger TOTAL_MAIN_LEVEL_SEAT_BOOKED_COUNT = new AtomicInteger(0);
	public static final AtomicInteger TOTAL_ORCHESTRA_LEVEL_SEAT_BOOKED_COUNT = new AtomicInteger(0);
	public static final AtomicInteger TOTAL_BALCONY1_LEVEL_SEAT_BOOKED_COUNT = new AtomicInteger(0);
	public static final AtomicInteger TOTAL_BALCONY2_LEVEL_SEAT_BOOKED_COUNT = new AtomicInteger(0);
	public static final AtomicInteger TOTAL_SEATS_BOOKED_IN_THEATER_COUNT = new AtomicInteger(0);
}
