package com.bank.atm.repository;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.bank.atm.exception.CustomException;
import com.bank.atm.util.Denomination;

@Repository
public class CashRepositoryImpl implements CashRepository {

	public final static Logger LOGGER = LogManager.getLogger(CashRepositoryImpl.class);
	
	private final static Map<Denomination, Integer> cashRepoMap = new HashMap<>();
	
	@Value("${THRESHOLD_VALUE_TWENTY}")
	private static int THRESHOLD_VALUE_TWENTY;
	
	@Value("${THRESHOLD_VALUE_FIFTY}")
	private static int THRESHOLD_VALUE_FIFTY;
	
	static {
		cashRepoMap.put(Denomination.TWENTY, 200);
		cashRepoMap.put(Denomination.FIFTY, 200);
		
		LOGGER.debug("Cash dispenser is initialized with below amount:");
		LOGGER.debug("Number of Twenty notes : "+ cashRepoMap.get(Denomination.TWENTY));
		LOGGER.debug("Number of Fifty notes  : "+ cashRepoMap.get(Denomination.FIFTY));
	}

	@Override
	public Map<Denomination, Integer> curBal() throws CustomException {
		Map<Denomination, Integer> currentCashBalMap = new HashMap<>();
		currentCashBalMap.put(Denomination.TWENTY, cashRepoMap.get(Denomination.TWENTY));
		currentCashBalMap.put(Denomination.FIFTY, cashRepoMap.get(Denomination.FIFTY));
		return currentCashBalMap;
	}

	@Override
	public void reloadBal(Map<Denomination, Integer> reloadCashMap) throws CustomException{
		for(Map.Entry<Denomination, Integer> entry : reloadCashMap.entrySet()) {
			cashRepoMap.put(entry.getKey(), cashRepoMap.get(entry.getKey()) + entry.getValue());
		}
	}

	@Override
	public Map<Denomination, Integer> withdrawCash(int amount) throws CustomException {

		Map<Denomination, Integer> withdrawCashMap = new HashMap<>();
		int noOfFiftys = 0, noOfTwentys = 0;
		if(amount/50 <= cashRepoMap.get(Denomination.FIFTY)) {
			noOfFiftys = amount/50;
			amount %= 50;
		}else {
			noOfFiftys = cashRepoMap.get(Denomination.FIFTY);
			amount = amount - 50 * noOfFiftys;
		}
		//handle special cases
		if(amount == 10 || amount == 30 ) {
			amount += 50;
			noOfFiftys--;
		}
		noOfTwentys = amount/20;
		
		withdrawCashMap.put(Denomination.TWENTY, noOfTwentys);
		withdrawCashMap.put(Denomination.FIFTY, noOfFiftys);
		
		deductCurrentBalance(withdrawCashMap);
		
		return withdrawCashMap;
	}
	
	private void deductCurrentBalance(Map<Denomination, Integer> withdrawCashMap) {		
		for(Map.Entry<Denomination, Integer> entry : withdrawCashMap.entrySet()) {
			cashRepoMap.put(entry.getKey(), cashRepoMap.get(entry.getKey()) - entry.getValue());
		}
		checkToAlertCashSupplier();
	}
	
	private void checkToAlertCashSupplier() {
		if(cashRepoMap.get(Denomination.TWENTY) <= THRESHOLD_VALUE_TWENTY
				|| cashRepoMap.get(Denomination.FIFTY) <= THRESHOLD_VALUE_FIFTY){
			LOGGER.warn("Warning! the cash dispenser is running out of notes");
		}
	}
}
