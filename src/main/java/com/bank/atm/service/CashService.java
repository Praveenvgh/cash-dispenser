package com.bank.atm.service;

import java.util.Map;

import com.bank.atm.exception.CustomException;
import com.bank.atm.util.Denomination;

public interface CashService {
	
	public Map<Denomination, Integer> curBal() throws CustomException;

	public void reloadBal(Map<Denomination, Integer> reloadCashMap) throws CustomException;

	public Map<Denomination, Integer> withdrawCash(int amount) throws CustomException;
	
}
