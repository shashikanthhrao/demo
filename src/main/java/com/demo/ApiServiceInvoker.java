package com.demo;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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
	private final OkHttpClient apsClient = new OkHttpClient();
	private final OkHttpClient ecprClient = new OkHttpClient();
	private final OkHttpClient ewsClient = new OkHttpClient();
	private final OkHttpClient simsClient = new OkHttpClient();
	private final OkHttpClient angClient = new OkHttpClient();
	private final OkHttpClient lexisNexisClient = new OkHttpClient();

	@Autowired
	private Environment env;

	@Bulkhead(name = "ApsBackendService")
	public String callAps() throws IOException {
		Request request = new Request.Builder().url(env.getProperty("apsUrl")).build();
		Response response = apsClient.newCall(request).execute();
		System.out.println("Invoking aps: " + env.getProperty("apsUrl"));
		String s=response.body().string();
		System.out.println(s);
		return s;
	}

	@TimeLimiter(name = "EcprBackendService")
	public CompletableFuture<String> callEcprAsync() {
		ApiServiceInvoker apiServiceInvoker = this;
		return CompletableFuture.supplyAsync(apiServiceInvoker::callEcpr);

	}

	public String callEcpr() {
		Request request = new Request.Builder().url(env.getProperty("ecprUrl")).build();
		Response response = null;
		String s = null;
		try {
			response = ecprClient.newCall(request).execute();
			if (response.isSuccessful()) {
				s = response.body().string();
			}
		} catch (IOException e) {
			s = String.valueOf(response.code());

		}
		return s;
	}

	@RateLimiter(name = "EwsBackendService")
	public String callEws() throws IOException {
		Request request = new Request.Builder().url(env.getProperty("ewsUrl")).build();
		Response response = ewsClient.newCall(request).execute();
		return response.body().string();
	}

	@CircuitBreaker(name = "SimsBackendService")
	public String callSims() throws IOException {
		Request request = new Request.Builder().url(env.getProperty("simsUrl")).build();
		Response response = simsClient.newCall(request).execute();
		return response.body().string();
	}

	@Retry(name = "AngBackendService", fallbackMethod = "fallbackAfterRetry")
	public String callAng() throws IOException {
		Request request = new Request.Builder().url(env.getProperty("angUrl")).build();
		Response response = angClient.newCall(request).execute();
		return response.body().string();
	}

	@Retry(name = "LexisNexisBackendService", fallbackMethod = "fallbackAfterRetry")
	@CircuitBreaker(name = "LexisNexisBackendService")
	@RateLimiter(name = "LexisNexisBackendService")
	@TimeLimiter(name = "LexisNexisBackendService")
	@Bulkhead(name = "LexisNexisBackendService")
	public CompletableFuture<String> callLexisNexisAsync() {
		ApiServiceInvoker apiServiceInvoker = this;
		return CompletableFuture.supplyAsync(apiServiceInvoker::callLexisNexis);

	}

	public String callLexisNexis() {
		Request request = new Request.Builder().url(env.getProperty("lexisNexisUrl")).build();
		Response response = null;
		String s = null;
		try {
			response = lexisNexisClient.newCall(request).execute();
			if (response.isSuccessful()) {
				s = response.body().string();
			}
		} catch (IOException e) {
			s = String.valueOf(response.code());

		}
		return s;
	}

}
