package com.demo;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class ResilientAppController {

	private final ApiServiceInvoker apiServiceInvoker;

	@Autowired
	public ResilientAppController(BackendServiceCaller externalApi, ApiServiceInvoker apiServiceInvoker) {
		this.apiServiceInvoker = apiServiceInvoker;
	}

	@GetMapping("bizTrxA")
	public String bizTrxA() throws IOException {
		// return externalAPICaller.callAps();
		return apiServiceInvoker.callAps();
	}

	@GetMapping("bizTrxB")
	public CompletableFuture<String> bizTrxB() {
		return apiServiceInvoker.callEcprAsync();
	}

	@GetMapping("bizTrxC")
	public String bizTrxC() throws IOException {
		return apiServiceInvoker.callEws();
	}

	@GetMapping("bizTrxD")
	public String bizTrxD() throws IOException {
		return apiServiceInvoker.callSims();
	}

	@GetMapping("bizTrxE")
	public String bizTrxE() throws IOException {
		return apiServiceInvoker.callAng();
	}

	@GetMapping("bizTrxF")
	public CompletableFuture<String> bizTrxF() {
		return apiServiceInvoker.callLexisNexisAsync();
	}

}
