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

	private final BackendServiceCaller backendServiceCaller;
	private final ApiServiceInvoker apiServiceInvoker;
	

	@Autowired
	public ResilientAppController(BackendServiceCaller backendServiceCaller, ApiServiceInvoker apiServiceInvoker) {
		this.backendServiceCaller=backendServiceCaller;
		this.apiServiceInvoker = apiServiceInvoker;
	}

	@GetMapping("bizTrxA")
	public String bizTrxA() throws IOException {
		//return backendServiceCaller.callAps();
		return apiServiceInvoker.callIP1();
	}

	@GetMapping("bizTrxB")
	public CompletableFuture<String> bizTrxB() {
		return apiServiceInvoker.callIp2Async();
	}

	@GetMapping("bizTrxC")
	public String bizTrxC() throws IOException {
		return apiServiceInvoker.callIP3();
	}

	@GetMapping("bizTrxD")
	public String bizTrxD() throws IOException {
		return apiServiceInvoker.callIP4();
	} 

	@GetMapping("bizTrxE")
	public String bizTrxE() throws IOException {
		return apiServiceInvoker.callIP5();
	}

	@GetMapping("bizTrxF")
	public CompletableFuture<String> bizTrxF() {
		return apiServiceInvoker.callIP6Async();
	}

}
