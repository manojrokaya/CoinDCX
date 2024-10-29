package com.condcx.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.Setter;

@Service
@Setter
public class Orderservice 
{
	private static final Logger logger = LoggerFactory.getLogger(Orderservice.class);
	private double triggerPrice;
	private String symbol;
	
	
	public void checkAndPrepareOrder(double currentPrice)
	{
		 try {
	            if (currentPrice <= triggerPrice) {
	                logger.info("Preparing buy order as current price {} is below or equal to trigger price {}", currentPrice, triggerPrice);
	                prepareBuyOrderPayload(currentPrice);
	            } else if (currentPrice >= triggerPrice) {
	                logger.info("Preparing sell order as current price {} is above or equal to trigger price {}", currentPrice, triggerPrice);
	                prepareSellOrderPayload(currentPrice);
	            }
	        } catch (Exception e) {
	            logger.error("Error during order preparation: {}", e.getMessage(), e);
	        }
	}
	
	private void prepareBuyOrderPayload(double currentPrice) {
        Map<String, Object> buyOrderPayload = new HashMap<>();
        buyOrderPayload.put("symbol", symbol);
        buyOrderPayload.put("side", "buy");
        buyOrderPayload.put("price", currentPrice);
        buyOrderPayload.put("quantity", 1);

        logger.debug("Prepared Buy Order Payload: {}", buyOrderPayload);
    }
	
	 private void prepareSellOrderPayload(double currentPrice) {
	        Map<String, Object> sellOrderPayload = new HashMap<>();
	        sellOrderPayload.put("symbol", symbol);
	        sellOrderPayload.put("side", "sell");
	        sellOrderPayload.put("price", currentPrice);
	        sellOrderPayload.put("quantity", 1);

	        logger.debug("Prepared Sell Order Payload: {}", sellOrderPayload);
	    }	
	
	
	public void simulateOrderCancellation() {

        Map<String, Object> cancelOrderPayload = new HashMap<>();
        cancelOrderPayload.put("order_id", UUID.randomUUID().toString());  // Simulated order ID for cancellation
        logger.info("Simulated Cancel Order Payload: {}", cancelOrderPayload);
	}

}
