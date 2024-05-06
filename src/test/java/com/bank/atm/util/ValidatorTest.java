package com.bank.atm.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ValidatorTest {

	Validator validator = new Validator();
	
	@Test
	void TestWithValidAmount() {
		assertTrue(validator.isValidAmount(200));
	}
	
	@Test
	void TestWithInValidAmount() {
		assertFalse(validator.isValidAmount(10));
	}
	
	@Test
	void TestWithAvailableAmount() {
		assertTrue(validator.isAvailableAmount(600, 10, 10));
	}
	
	@Test
	void TestWithAvailableAmountWhenFiftysAvailable() {
		assertTrue(validator.isAvailableAmount(200, 10, 0));
	}
	
	@Test
	void TestWithAvailableAmountWhenTwentysAvailable() {
		assertTrue(validator.isAvailableAmount(200, 0, 10));
	}
	
	@Test
	void TestWithUnavailableAmount() {
		assertFalse(validator.isAvailableAmount(1000, 10, 10));
	}

}
