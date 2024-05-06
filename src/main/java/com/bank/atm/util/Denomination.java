package com.bank.atm.util;

public enum Denomination {

	TWENTY(20),
	FIFTY(50);
	
	private int value;
	
	private Denomination(int val) {
		value = val;
	}
	
	public int getValue() {
		return value;
	}
}
