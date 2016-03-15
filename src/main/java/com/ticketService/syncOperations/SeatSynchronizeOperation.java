package com.ticketService.syncOperations;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.constants.TheaterConstants;
import com.ticketService.dataGenerator.CommonRandomUtils;
import com.ticketService.domain.Seat;
import com.ticketService.domain.SeatHold;
import com.ticketService.domain.Theater;
import com.ticketService.domain.TheaterLevel;

/**
 * All customer operations like find availability of seats, find and hold the seats, confirm the seat selection after
 * expiry are handled in this class
 * 
 * @author ajunaga
 *
 */
public class SeatSynchronizeOperation {

	private static final Logger logger = LoggerFactory.getLogger(SeatSynchronizeOperation.class.getSimpleName());

	@Autowired
	@Qualifier("commonRandomUtils")
	private CommonRandomUtils commonRandomUtils;

	@Autowired
	@Qualifier("theater")
	private Theater theater;

	@Autowired
	@Qualifier("balcony1SyncObj")
	private Object balcony1SyncObj;

	@Autowired
	@Qualifier("balcony2SyncObj")
	private Object balcony2SyncObj;

	@Autowired
	@Qualifier("mainLevelSyncObj")
	private Object mainLevelSyncObj;

	@Autowired
	@Qualifier("orchestraSyncObj")
	private Object orchestraSyncObj;

	/**
	 * Calculate all available seats in all levels available in theater
	 * 
	 * @return
	 */
	public int calculateAvailableSeatsInAllLevel() {
		int availableSeatCount = 0;
		for (Integer level : TheaterConstants.THEATER_LEVEL_LIST) {
			availableSeatCount += calculateAvailableSeatsInSelectedLevel(level);
		}
		return availableSeatCount;
	}

	/**
	 * Calculate seats available at particular level i.e. balcony1/mainLevel etc.
	 * 
	 * @param level
	 * @return
	 */
	public int calculateAvailableSeatsInSelectedLevel(int level) {
		int availableSeatCount = 0;
		TheaterLevel theatreLevel = theater.getTheaterInfoMap().get(level);
		for (Seat seat : theatreLevel.getSeatList()) {
			Object syncObj = getSyncObj(level);
			synchronized (syncObj) {
				boolean isBooked = seat.isBooked().get();
				boolean isOnHold = seat.isOnHold().get();
				if (!isBooked && !isOnHold) {
					availableSeatCount++;
				}
			}
		}
		return availableSeatCount;
	}

	/**
	 * If customer do not confirm within expiration time, such seats isOnHold status will be set to false and customer
	 * will need to do new selection of seat
	 * 
	 * @param seatHold
	 * @return
	 */
	public void expireTicketSeatHold(SeatHold seatHold) {
		for (Seat seat : seatHold.getSeatList()) {
			Object syncObj = getSyncObj(seat.getLevelId());
			boolean isOnHoldSuccess = false;
			synchronized (syncObj) {
				isOnHoldSuccess = seat.isOnHold().compareAndSet(true, false);
			}
			if (isOnHoldSuccess) {
				logger.debug("hold released successful on seat:" + seat.toString());
			} else {
				logger.debug("hold released failed on seat:" + seat.toString());
			}
		}
	}

	/**
	 * For customer request at particular level, seats will be found in theater and will be put onHold status as true
	 * for that customer
	 * 
	 * @param level
	 * @param numSeats
	 * @return
	 */
	public List<Seat> findAndHoldSeats(int level, int numSeats) {
		List<Seat> holdSeatList = new ArrayList<Seat>();
		TheaterLevel theatreLevel = theater.getTheaterInfoMap().get(level);
		for (Seat seat : theatreLevel.getSeatList()) {
			Object syncObj = getSyncObj(level);
			boolean isOnHoldSuccess = false;
			synchronized (syncObj) {
				isOnHoldSuccess = seat.isOnHold().compareAndSet(false, true);
			}
			if (isOnHoldSuccess) {
				holdSeatList.add(seat);
			}
			if (holdSeatList.size() == numSeats) {
				break;
			}
		}
		return holdSeatList;
	}

	/**
	 * Once seat reservation is finally confirmed from customer, such seats will be booked under that customer profile
	 * 
	 * @param seatList
	 * @param seatHoldId
	 */
	public void reserveSeats(List<Seat> seatList, Integer seatHoldId) {
		for (Seat seat : seatList) {
			Object syncObj = getSyncObj(seat.getLevelId());
			boolean isHoldReleased = false;
			boolean isSeatBookConfirm = false;
			synchronized (syncObj) {
				isHoldReleased = seat.isOnHold().compareAndSet(true, false);
				isSeatBookConfirm = seat.isBooked().compareAndSet(false, true);
			}
			if (isHoldReleased && isSeatBookConfirm) {
				StringBuilder successMsg = new StringBuilder();
				successMsg.append("Seat reserved successful. Status updated to booked seat details").append(seat).append(
						" SeatHoldId:").append(seatHoldId);
				logger.debug(successMsg.toString());
			}
			incrementSeatBookCounter(seat.getLevelId());
		}
	}

	private void incrementSeatBookCounter(Integer level) {
		switch (level) {
		case 1:
			TheaterConstants.TOTAL_ORCHESTRA_LEVEL_SEAT_BOOKED_COUNT.addAndGet(1);
			break;

		case 2:
			TheaterConstants.TOTAL_MAIN_LEVEL_SEAT_BOOKED_COUNT.addAndGet(1);
			break;

		case 3:
			TheaterConstants.TOTAL_BALCONY1_LEVEL_SEAT_BOOKED_COUNT.addAndGet(1);
			break;

		case 4:
			TheaterConstants.TOTAL_BALCONY2_LEVEL_SEAT_BOOKED_COUNT.addAndGet(1);
			break;

		default:
			logger.info("invalid level-id in incrementSeatBookCounter()", level);
		}
		TheaterConstants.TOTAL_SEATS_BOOKED_IN_THEATER_COUNT.addAndGet(1);
	}

	/**
	 * As multiple request to book seats will be processed, synchronization is required to maintain data consistency.
	 * Individual object for each level is constructed and a lock is acquired over that object to do operation
	 * atomically
	 * 
	 * @param level
	 * @return
	 */
	private Object getSyncObj(int level) {
		switch (level) {
		case 1:
			return orchestraSyncObj;

		case 2:
			return mainLevelSyncObj;

		case 3:
			return balcony1SyncObj;

		case 4:
			return balcony1SyncObj;

		case -1:
			return null;

		default:
			return null;
		}
	}

}
