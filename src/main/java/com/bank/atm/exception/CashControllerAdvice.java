package com.bank.atm.exception;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CashControllerAdvice {

	@Autowired
	Environment environment;
	
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CustomError> handleCustomException(CustomException ce) {
		
		return new ResponseEntity<>(getCustomError(ce), HttpStatus.BAD_REQUEST);
	}
	
	private CustomError getCustomError(Exception e) {
		
		CustomError error = new CustomError();
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		error.setErrorMessage(environment.getProperty(e.getMessage()));
		error.setTimestamp(LocalDateTime.now());
		
		return error;
	}
}
