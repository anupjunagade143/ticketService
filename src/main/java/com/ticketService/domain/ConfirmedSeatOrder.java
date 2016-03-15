package com.ticketService.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is mainly used for reporting purpose to check all customer orders,seat details etc.
 * 
 * @author ajunaga
 *
 */
public class ConfirmedSeatOrder implements Serializable {

	private static final long serialVersionUID = 2516306847792725389L;

	/* Key is seatHoldId and value is confirmationOrder object */
	private Map<Integer, CustomerConfirmationOrder> confirmOrderMap = new ConcurrentHashMap<>();

	public Map<Integer, CustomerConfirmationOrder> getConfirmOrderMap() {
		return confirmOrderMap;
	}

	public void addConfirmOrderMap(int seatHoldId, CustomerConfirmationOrder order) {
		this.confirmOrderMap.put(seatHoldId, order);
	}

	@Override
	public String toString() {
		return String.format("ConfirmedSeatOrder [confirmOrderMap=%s]", confirmOrderMap);
	}

}
