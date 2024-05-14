package com.demo;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@Component
public class BackendServiceCaller {
	private final RestTemplate restTemplate;

	@Autowired
	private Environment env;

	@Autowired
	public BackendServiceCaller(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Bulkhead(name = "ApsBackendService")
	public String callAps() {
		//System.out.println("**************");
		String baseUrl = env.getProperty("backendService.baseUrl");
		//System.out.println("backendService.baseUrl=" + baseUrl);
		String path = env.getProperty("backendService.path.aps");
		//System.out.println("backendService.path.aps=" + path);
		String s = restTemplate.getForObject(path, String.class);
		//System.out.println(s);
		return s;
	}

	//@TimeLimiter(name = "EcprBackendService")
	public CompletableFuture<String> callEcprAsync() {
		BackendServiceCaller backendServiceCaller = this;
		return CompletableFuture.supplyAsync(backendServiceCaller::callEcpr);

	}

	//@RateLimiter(name = "EwsBackendService")
	public String callEws() {
		String path = env.getProperty("backendService.path.ews");
		return restTemplate.getForObject(path, String.class);
	}

	//@CircuitBreaker(name = "SimsBackendService")
	public String callSims() {
		String path = env.getProperty("backendService.path.sims");
		return restTemplate.getForObject(path, String.class);
	}

	//@Retry(name = "AngBackendService", fallbackMethod = "fallbackAfterRetry")
	public String callAng() {
		String path = env.getProperty("backendService.path.ang");
		return restTemplate.getForObject(path, String.class);
	}

	//@Retry(name = "LexisNexisBackendService", fallbackMethod = "fallbackAfterRetry")
	//@CircuitBreaker(name = "LexisNexisBackendService")
	//@RateLimiter(name = "LexisNexisBackendService")
	//@TimeLimiter(name = "LexisNexisBackendService")
	//@Bulkhead(name = "LexisNexisBackendService")
	public CompletableFuture<String> callLexisNexisAsync() {
		BackendServiceCaller backendServiceCaller = this;
		return CompletableFuture.supplyAsync(backendServiceCaller::callLexisNexis);

	}

	public String callEcpr() {
		String path = env.getProperty("backendService.path.ecpr");
		return restTemplate.getForObject(path, String.class);
	}

	public String callLexisNexis() {
		String path = env.getProperty("backendService.path.lexisNexis");
		return restTemplate.getForObject(path, String.class);
	}

}
