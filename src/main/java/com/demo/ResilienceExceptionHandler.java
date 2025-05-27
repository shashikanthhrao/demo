package com.demo;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;

@ControllerAdvice
public class ResilienceExceptionHandler {

	Logger logger = LoggerFactory.getLogger(ResilienceExceptionHandler.class);

	// Bulkhead Full Exception
	@ExceptionHandler({ BulkheadFullException.class })
	@ResponseStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
	public void handleBulkheadFullException(BulkheadFullException e) {
		//logger.error("BulkheadFullException handled. HTTP Response Status code set to: "+ HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, e);		
		logger.error("BulkheadFullException handled. HTTP Response Status code set to: "+ HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);

	}

	// TimeLimiter Exception
	@ExceptionHandler({ TimeoutException.class })
	@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
	public void handleTimeoutException(TimeoutException e) {
		//logger.error("TimeoutException handled. HTTP Response Status code set to: " + HttpStatus.REQUEST_TIMEOUT, e);
		logger.error("TimeoutException handled. HTTP Response Status code set to: " + HttpStatus.REQUEST_TIMEOUT);
	}

	// RateLimiter related RequestNotPermitted Exception
	@ExceptionHandler({ RequestNotPermitted.class })
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public void handleRequestNotPermitted(RequestNotPermitted e) {
		//logger.error("RequestNotPermitted Exception handled. HTTP Response Status code set to: "+ HttpStatus.TOO_MANY_REQUESTS, e);
		logger.error("RequestNotPermitted Exception handled. HTTP Response Status code set to: "+ HttpStatus.TOO_MANY_REQUESTS);
	}

	// CircuitBreaker related CallNotPermittedException
	@ExceptionHandler({ CallNotPermittedException.class })
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public void handleCallNotPermittedException(CallNotPermittedException e) {
		//logger.error("CallNotPermittedException handled. HTTP Response Status code set to: "+ HttpStatus.SERVICE_UNAVAILABLE, e);
		logger.error("CallNotPermittedException handled. HTTP Response Status code set to: "+ HttpStatus.SERVICE_UNAVAILABLE);
	}

}
