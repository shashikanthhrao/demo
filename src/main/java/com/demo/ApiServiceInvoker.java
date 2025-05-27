package com.demo;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class ApiServiceInvoker {

	Logger logger = LoggerFactory.getLogger(ApiServiceInvoker.class);

	private final OkHttpClient iP1Client;
	private final OkHttpClient iP2Client;
	private final OkHttpClient iP3Client;
	private final OkHttpClient iP4Client;
	private final OkHttpClient iP5Client;
	private final OkHttpClient iP6Client;

	@Autowired
	private Environment env;

	@Autowired
	public ApiServiceInvoker() {
		iP1Client = new OkHttpClient();
		iP2Client = new OkHttpClient();
		iP3Client = new OkHttpClient();
		iP4Client = new OkHttpClient();
		iP5Client = new OkHttpClient();
		iP6Client = new OkHttpClient();
	}

	@Bulkhead(name = "Ip1BackendService")
	public String callIP1() throws IOException {
		logger.debug("ApiServiceInvoker making a call to " + env.getProperty("iP1Url"));
		Request request = new Request.Builder().url(env.getProperty("iP1Url")).build();
		Response response = iP1Client.newCall(request).execute();
		String responseString = response.body().string();
		logger.trace("Response Recieved: " + responseString);
		logger.trace("********************************");
		return responseString;

	}

	@TimeLimiter(name = "Ip2BackendService")
	public CompletableFuture<String> callIp2Async() {
		ApiServiceInvoker apiServiceInvoker = this;
		return CompletableFuture.supplyAsync(apiServiceInvoker::callIP2);

	}

	public String callIP2() {
		Request request = new Request.Builder().url(env.getProperty("Ip2Url")).build();
		Response response = null;
		String s = null;
		try {
			response = iP2Client.newCall(request).execute();
			if (response.isSuccessful()) {
				s = response.body().string();
			}
		} catch (IOException e) {
			s = String.valueOf(response.code());

		}
		return s;
	}

	@RateLimiter(name = "Ip3BackendService")
	public String callIP3() throws IOException {
		Request request = new Request.Builder().url(env.getProperty("Ip3Url")).build();
		Response response = iP3Client.newCall(request).execute();
		return response.body().string();
	}

	@CircuitBreaker(name = "Ip4BackendService")
	@TimeLimiter(name = "Ip6BackendService")
	public String callIP4() throws IOException {
		Request request = new Request.Builder().url(env.getProperty("iP4Url")).build();
		Response response = iP4Client.newCall(request).execute();
		return response.body().string();
	}

	@Retry(name = "Ip5BackendService")
	public String callIP5() throws IOException {
		Request request = new Request.Builder().url(env.getProperty("iP5Url")).build();
		Response response = iP5Client.newCall(request).execute();
		return response.body().string();
	}

	// @Retry(name = "Ip6BackendService")
	@CircuitBreaker(name = "Ip6BackendService")
	// @RateLimiter(name = "Ip6BackendService")
	@TimeLimiter(name = "Ip6BackendService")
	@Bulkhead(name = "Ip6BackendService")
	public CompletableFuture<String> callIP6Async() {
		ApiServiceInvoker apiServiceInvoker = this;
		return CompletableFuture.supplyAsync(apiServiceInvoker::callIP6);

	}

	public String callIP6() {
		Request request = new Request.Builder().url(env.getProperty("iP6Url")).build();
		Response response = null;
		String s = null;
		try {
			response = iP6Client.newCall(request).execute();
			if (response.isSuccessful()) {
				s = response.body().string();
			}
		} catch (IOException e) {
			s = String.valueOf(response.code());

		}
		return s;
	}

}
