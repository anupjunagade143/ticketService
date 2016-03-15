package com.ticketService.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.MapUtils;

import com.ticketService.constants.TheaterConstants;

/**
 * Theater object consisting of multiple levels
 * 
 * @author ajunaga
 *
 */
public class Theater {

	private TheaterLevel orchestra;
	private TheaterLevel main;
	private TheaterLevel balcony1;
	private TheaterLevel balcony2;

	private Map<Integer, TheaterLevel> theaterInfoMap;

	public TheaterLevel getOrchestra() {
		return orchestra;
	}

	public void setOrchestra(TheaterLevel orchestra) {
		this.orchestra = orchestra;
		putInTheaterInfoMap(TheaterConstants.ORCHESTRA_LEVEL, orchestra);
	}

	public TheaterLevel getMain() {
		return main;
	}

	public void setMain(TheaterLevel main) {
		this.main = main;
		putInTheaterInfoMap(TheaterConstants.MAIN_LEVEL, main);
	}

	public TheaterLevel getBalcony1() {
		return balcony1;
	}

	public void setBalcony1(TheaterLevel balcony1) {
		this.balcony1 = balcony1;
		putInTheaterInfoMap(TheaterConstants.BALCONY1_LEVEL, balcony1);
	}

	public TheaterLevel getBalcony2() {
		return balcony2;
	}

	public void setBalcony2(TheaterLevel balcony2) {
		this.balcony2 = balcony2;
		putInTheaterInfoMap(TheaterConstants.BALCONY2_LEVEL, balcony2);
	}

	private void putInTheaterInfoMap(int level, TheaterLevel theaterLevel) {
		if (MapUtils.isEmpty(theaterInfoMap)) {
			theaterInfoMap = new ConcurrentHashMap<Integer, TheaterLevel>();
		}
		theaterInfoMap.put(level, theaterLevel);
	}

	public Map<Integer, TheaterLevel> getTheaterInfoMap() {
		return theaterInfoMap;
	}

	@Override
	public String toString() {
		return String.format("Theater [orchestra=%s, main=%s, balcony1=%s, balcony2=%s, theaterInfoMap=%s]", orchestra,
				main, balcony1, balcony2, theaterInfoMap);
	}

}
