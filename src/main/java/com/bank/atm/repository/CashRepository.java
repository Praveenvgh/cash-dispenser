package com.bank.atm.repository;

import java.util.Map;

import com.bank.atm.exception.CustomException;
import com.bank.atm.util.Denomination;

public interface CashRepository {

	public Map<Denomination, Integer> curBal() throws CustomException;

	public void reloadBal(Map<Denomination, Integer> reloadCashMap) throws CustomException;

	public Map<Denomination, Integer> withdrawCash(int amount) throws CustomException;
}
