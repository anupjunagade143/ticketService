package com.ticketService.dataGenerator;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import com.ticketService.domain.Optional;

/**
 * Utility to generate random numbers,names,email-ids
 * 
 * @author ajunaga
 *
 */
public class CommonRandomUtils {

	/**
	 * get random userName
	 * 
	 * @return
	 */
	public String getRandomName() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(50, random).toString(32);
	}

	/**
	 * get random emailId
	 * 
	 * @return
	 */
	public String getRandomEmailId() {
		StringBuilder emailId = new StringBuilder();
		SecureRandom random = new SecureRandom();
		emailId.append(new BigInteger(34, random).toString(32)).append("@myGmail.com");
		return emailId.toString();
	}

	/**
	 * Generate random level-id
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public Optional<Integer> getRandomLevelId(int min, int max) {
		Random random = new Random();
		int value = random.nextInt(max - min + 1) + min;
		return new Optional<Integer>(value);
	}

	/**
	 * generate random booking count
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public int getRandomBookingCount(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}

	/**
	 * generate random level-id
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public Integer getRandomLevel(int min, int max) {
		Random random = new Random();
		int value = random.nextInt(max - min + 1) + min;
		if (value == 0) {
			value = max;
		}
		return value;
	}

	/**
	 * generate random boolean value true/false
	 * 
	 * @return
	 */
	public boolean generateRandomBoolean() {
		Random random = new Random();
		return random.nextBoolean();
	}
}
