package org.gdf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LoginExceptionHandler {
	
	
	@ExceptionHandler(value = LoginException.class)
	public ResponseEntity<Object> exception(LoginException loginException) {
		return new ResponseEntity<>(loginException.getMessage(), HttpStatus.BAD_REQUEST);
		
	}

}
