package com.ticketService.constants;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration for various level in Theater
 * 
 * @author ajunaga
 *
 */
public enum TheaterEnum {

	ORCHESTRA(1),
	MAIN(2),
	BALCONY1(3),
	BALCONY2(4);

	private int value;

	private TheaterEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	private static final Map<Integer, TheaterEnum> lookup = new HashMap<Integer, TheaterEnum>();

	static {
		for (TheaterEnum s : EnumSet.allOf(TheaterEnum.class))
			lookup.put(s.getValue(), s);
	}

	public static String getLevelName(int level) {
		return lookup.get(level).name();
	}

};
