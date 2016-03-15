package com.ticketService.domain;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ticketService.constants.TheaterConstants;

/**
 * Used for generating level-id
 * 
 * @author ajunaga
 *
 * @param <T>
 */
public class Optional<T> implements Serializable {

	private static final long serialVersionUID = 1887009683442040322L;

	@Autowired
	@Qualifier("minTheaterLevel")
	private Integer minTheaterLevel;

	@Autowired
	@Qualifier("maxTheaterLevel")
	private Integer maxTheaterLevel;

	private int level;

	public Optional(int level) {
		super();
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		if (level >= minTheaterLevel && level <= maxTheaterLevel) {
			this.level = level;
		} else {
			this.level = TheaterConstants.DEFAULT_LEVEL;
		}
	}

	@Override
	public String toString() {
		return String.format("Optional [level=%s]", level);
	}

}
