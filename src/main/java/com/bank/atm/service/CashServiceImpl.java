package com.bank.atm.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.atm.exception.CustomException;
import com.bank.atm.repository.CashRepository;
import com.bank.atm.util.Denomination;
import com.bank.atm.util.Validator;

@Service
public class CashServiceImpl implements CashService{

	@Autowired
	CashRepository cashRepository;
	
	@Autowired
	Validator validator;
	
	@Override
	public Map<Denomination, Integer> curBal() throws CustomException {
		
		return cashRepository.curBal();
	}

	@Override
	public void reloadBal(Map<Denomination, Integer> reloadCashMap) throws CustomException{

		cashRepository.reloadBal(reloadCashMap);
	}

	@Override
	public Map<Denomination, Integer> withdrawCash(int amount) throws CustomException {
		
		Map<Denomination, Integer> curCashMap = curBal();
		
		if(!validator.isValidAmount(amount)) {
			throw new CustomException("Service.NOT_VALID_AMOUNT");
		} 
		else if(!validator.isAvailableAmount(amount, curCashMap.get(Denomination.FIFTY), curCashMap.get(Denomination.TWENTY))) {
			throw new CustomException("Service.NOT_AVAILABLE_AMOUNT");
		}
		else {
			return cashRepository.withdrawCash(amount);
		}
	}
}
