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

	private final OkHttpClient apsClient;
	private final OkHttpClient ecprClient;
	private final OkHttpClient ewsClient;
	private final OkHttpClient simsClient;
	private final OkHttpClient angClient;
	private final OkHttpClient lexisNexisClient;

	@Autowired
	private Environment env;

	@Autowired
	public ApiServiceInvoker() {
		apsClient = new OkHttpClient();
		ecprClient = new OkHttpClient();
		ewsClient = new OkHttpClient();
		simsClient = new OkHttpClient();
		angClient = new OkHttpClient();
		lexisNexisClient = new OkHttpClient();
	}

	@Bulkhead(name = "ApsBackendService")
	public String callAps() throws IOException {
		logger.debug("ApiServiceInvoker making a call to " + env.getProperty("apsUrl"));
		Request request = new Request.Builder().url(env.getProperty("apsUrl")).build();
		Response response = apsClient.newCall(request).execute();
		String responseString = response.body().string();
		logger.trace("Response Recieved: " + responseString);
		logger.trace("********************************");
		return responseString;

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

	@Retry(name = "AngBackendService")
	public String callAng() throws IOException {
		Request request = new Request.Builder().url(env.getProperty("angUrl")).build();
		Response response = angClient.newCall(request).execute();
		return response.body().string();
	}

	@Retry(name = "LexisNexisBackendService")
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
