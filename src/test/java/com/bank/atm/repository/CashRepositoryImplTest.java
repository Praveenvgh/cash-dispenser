package com.bank.atm.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.bank.atm.util.Denomination;

@TestMethodOrder(OrderAnnotation.class)
class CashRepositoryImplTest {

	CashRepository cashRepository = new CashRepositoryImpl();
	
	@Test
	@Order(1)
	void TestCurrentBalance() throws Exception {
		Map<Denomination, Integer> cashMap = cashRepository.curBal();
		assertNotNull(cashMap);
		assertEquals(200, cashMap.get(Denomination.TWENTY));
		assertEquals(200, cashMap.get(Denomination.FIFTY));
	}
	
	@Test
	@Order(2)
	void TestWithdrawCash() throws Exception {

		Map<Denomination, Integer> cashMap = cashRepository.withdrawCash(520);
		assertNotNull(cashMap);
		assertEquals(1, cashMap.get(Denomination.TWENTY));
		assertEquals(10, cashMap.get(Denomination.FIFTY));
	}
	
	@Test
	@Order(3)
	void TestCurrentBalanceAfterWithdraw() throws Exception {
		Map<Denomination, Integer> cashMap = cashRepository.curBal();
		assertNotNull(cashMap);
		assertEquals(199, cashMap.get(Denomination.TWENTY));
		assertEquals(190, cashMap.get(Denomination.FIFTY));
	}
	
	@Test
	@Order(4)
	void TestReloadBalance() throws Exception {
		Map<Denomination, Integer> reloadCashMap = new HashMap<>();
		reloadCashMap.put(Denomination.TWENTY, 1);
		reloadCashMap.put(Denomination.FIFTY, 10);
		cashRepository.reloadBal(reloadCashMap);
		Map<Denomination, Integer> cashMap = cashRepository.curBal();
		assertNotNull(cashMap);
		assertEquals(200, cashMap.get(Denomination.TWENTY));
		assertEquals(200, cashMap.get(Denomination.FIFTY));
	}
	
	@Test
	@Order(5)
	void TestCurrentBalanceAfterReload() throws Exception {
		Map<Denomination, Integer> cashMap = cashRepository.curBal();
		assertNotNull(cashMap);
		assertEquals(200, cashMap.get(Denomination.TWENTY));
		assertEquals(200, cashMap.get(Denomination.FIFTY));
	}
}
