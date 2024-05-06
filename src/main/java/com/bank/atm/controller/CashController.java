package com.bank.atm.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.atm.service.CashService;
import com.bank.atm.util.Denomination;

@RestController
@RequestMapping("/atm/v1")
public class CashController {
	
	@Autowired
	CashService cashService;
	
	@GetMapping("/currentBalance")
	public ResponseEntity<Map<Denomination, Integer>> currentBalance() throws Exception {
		
		Map<Denomination, Integer> cashMap = cashService.curBal();
		
		return new ResponseEntity<>(cashMap, HttpStatus.OK);
	}
	
	@PostMapping("/reloadBalance")
	public ResponseEntity<String> reloadBalance(@RequestBody Map<Denomination, Integer> reloadCashMap) throws Exception {
		
		cashService.reloadBal(reloadCashMap);
		return new ResponseEntity<>("Cash reloaded", HttpStatus.OK);
	}

	@GetMapping("/withdraw/{amount}")
	public ResponseEntity<Map<Denomination, Integer>> withdraw(@PathVariable int amount) throws Exception {
		
		Map<Denomination, Integer> cashMap = cashService.withdrawCash(amount);
		
		return new ResponseEntity<>(cashMap, HttpStatus.OK);
	}
}
