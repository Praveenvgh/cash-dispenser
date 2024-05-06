package com.bank.atm.util;

import org.springframework.stereotype.Component;

@Component
public class Validator {

	public boolean isValidAmount(int amount) {
		
		if(amount%10 != 0 || amount<20)
			return false;
		else if(amount == 10 && amount == 30)
			return false;
		return true;
	}
	
	public boolean isAvailableAmount(int amount, int noOfFiftys, int noOfTwentys) {
		
		if(amount > (50 * noOfFiftys + 20 * noOfTwentys))
			return false;
		if(amount/50 <= noOfFiftys)
			return true;
		else if(amount/20 <= noOfTwentys)
			return true;
		else {
			amount = amount - 50 * noOfFiftys;
			//handle special cases
			if(amount == 10 || amount == 30) {
				amount += 50;
			}
			if(amount/20 <= noOfTwentys)
				return true;
		}
		return false;
	}
}
