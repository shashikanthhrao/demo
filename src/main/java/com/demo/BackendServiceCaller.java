package com.demo;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;

@Component
public class BackendServiceCaller {
	private final RestTemplate restTemplate;

	@Autowired
	private Environment env;

	@Autowired
	public BackendServiceCaller(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Bulkhead(name = "Ip1BackendService")
	public String callIp1() {
		String baseUrl = env.getProperty("backendService.baseUrl");

		String path = env.getProperty("backendService.path.iP1");

		String s = restTemplate.getForObject(path, String.class);
		// System.out.println(s);
		return s;
	}

	// @TimeLimiter(name = "Ip2BackendService")
	public CompletableFuture<String> callIp2Async() {
		BackendServiceCaller backendServiceCaller = this;
		return CompletableFuture.supplyAsync(backendServiceCaller::callIp2);

	}

	// @RateLimiter(name = "Ip3BackendService")
	public String callIp3() {
		String path = env.getProperty("backendService.path.iP3");
		return restTemplate.getForObject(path, String.class);
	}

	// @CircuitBreaker(name = "Ip4BackendService")
	public String callIp4() {
		String path = env.getProperty("backendService.path.iP4");
		return restTemplate.getForObject(path, String.class);
	}

	// @Retry(name = "Ip5BackendService", fallbackMethod = "fallbackAfterRetry")
	public String callIp5() {
		String path = env.getProperty("backendService.path.iP5");
		return restTemplate.getForObject(path, String.class);
	}

	// @Retry(name = "Ip6BackendService", fallbackMethod =
	// "fallbackAfterRetry")
	// @CircuitBreaker(name = "Ip6BackendService")
	// @RateLimiter(name = "Ip6BackendService")
	// @TimeLimiter(name = "Ip6BackendService")
	// @Bulkhead(name = "Ip6BackendService")
	public CompletableFuture<String> callIp6Async() {
		BackendServiceCaller backendServiceCaller = this;
		return CompletableFuture.supplyAsync(backendServiceCaller::callIp6);

	}

	public String callIp2() {
		String path = env.getProperty("backendService.path.iP2");
		return restTemplate.getForObject(path, String.class);
	}

	public String callIp6() {
		String path = env.getProperty("backendService.path.iP6");
		return restTemplate.getForObject(path, String.class);
	}

}
