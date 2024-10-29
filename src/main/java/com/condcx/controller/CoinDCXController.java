package com.condcx.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.condcx.service.Orderservice;
import com.condcx.websocket.CoinDCXWebSocketService;

import Response.ApiResponse;

@RestController
@RequestMapping("/api/coindcx")
public class CoinDCXController 
{

	private Orderservice orderService;
	private CoinDCXWebSocketService coinDCXWebSocketService;

    public CoinDCXController(CoinDCXWebSocketService coinDCXWebSocketService, Orderservice orderService) {
        this.coinDCXWebSocketService = coinDCXWebSocketService;
        this.orderService = orderService;
    }

	@PostMapping("/start")
	public ResponseEntity<ApiResponse> trackSymbol(@RequestParam String symbol, @RequestParam double triggerPrice) {
		//TODO: process POST request
		 try {
	            orderService.setSymbol(symbol);
	            orderService.setTriggerPrice(triggerPrice);
	            coinDCXWebSocketService.initialize(symbol, triggerPrice);
	            coinDCXWebSocketService.connect();
	            return ResponseEntity.ok(new ApiResponse("success", "Started tracking for symbol: " + symbol + " at trigger price: " + triggerPrice));
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body(new ApiResponse("error", "Failed to start WebSocket: " + e.getMessage()));
	        }
	}
	   @GetMapping("/stop")
	    public ResponseEntity<ApiResponse>  stopWebSocket() {
		   try {
	            coinDCXWebSocketService.close();
	            return ResponseEntity.ok(new ApiResponse("success", "Stopped tracking."));
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body(new ApiResponse("error", "Failed to stop WebSocket: " + e.getMessage()));
	        }
	   }
	   
	   @PostMapping("/cancelOrder")
	    public  ResponseEntity<ApiResponse> cancelOrder() {
		   try {
	            coinDCXWebSocketService.cancelOrder();
	            return ResponseEntity.ok(new ApiResponse("success", "Order cancellation simulated successfully."));
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body(new ApiResponse("error", "Failed to simulate order cancellation: " + e.getMessage()));
	        }
	    }
	
}
