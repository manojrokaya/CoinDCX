package com.condcx.websocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.condcx.service.Orderservice;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
 
@Service
public class CoinDCXWebSocketService  extends WebSocketClient
{
	private static final Logger logger  =  LoggerFactory.getLogger(CoinDCXWebSocketService.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private final Orderservice orderService;
	private String symbol;
	private double triggerPrice;
	
	public CoinDCXWebSocketService(Orderservice orderService) throws URISyntaxException
	{
		super(new URI("wss://stream.coindcx.com"));
	        this.orderService = orderService;
	        
	}
	
	   // Method to initialize parameters after bean creation
    public void initialize(String symbol, double triggerPrice) {
        this.symbol = symbol;
        this.triggerPrice = triggerPrice;
        subscribeToTicker();
        logger.info("Initialized WebSocket service with symbol: {} and trigger price: {}", symbol, triggerPrice);
    }

	@Override
	public void onOpen(ServerHandshake handshakedata) 
	{
		// TODO Auto-generated method stub
        logger.info("Connected to CoinDCX WebSocket for symbol: {}", symbol);
        
	}

	@Override
	public void onMessage(String message) {
		// TODO Auto-generated method stub
		 logger.debug("Received message from WebSocket: {}", message);
	        try {
	            JsonNode jsonNode = objectMapper.readTree(message);
	            processMessage(jsonNode);
	        } catch (Exception e) {
	            logger.error("Error parsing WebSocket message: {}", e.getMessage(), e);
	        }	
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		// TODO Auto-generated method stub
        logger.warn("WebSocket connection closed. Code: {}, Reason: {}", code, reason);
	}

	@Override
	public void onError(Exception ex) {
		// TODO Auto-generated method stub
        logger.error("WebSocket error: {}", ex.getMessage(), ex);
	}
	
	private void subscribeToTicker()
	{
		Map<String, Object> subscribeMessage = new HashMap<>();
        subscribeMessage.put("event", "subscribe");
        subscribeMessage.put("channel", "ticker");
        subscribeMessage.put("symbol", symbol); // Example

        try {
            String message = objectMapper.writeValueAsString(subscribeMessage);

            if (this.isOpen()) {
                send(message);
                logger.info("Subscribed to ticker for symbol: {}", symbol);
            } else {
                logger.warn("WebSocket is not connected. Unable to send subscription message.");
            }
        } catch (Exception e) {
            logger.error("Failed to send subscription message: {}", e.getMessage(), e);
        }
	}
	
	
	private void retrySend(String message, int retries)
	{
	    while (retries > 0) 
	    {
	    	try {
	            send(message);
	            break; // Exit loop on success
	        }
	    	catch (Exception e) 
	    	{
	    		 retries--;
	    		 if (retries == 0) {
	                 logger.error("Failed to send message after retries: {}", e.getMessage(), e);
	             } 
	    		 else
	    		 {
	    			 logger.warn("Retrying to send message. Attempts left: {}", retries);
	                 try {
	                     Thread.sleep(1000); // Wait before retrying
	                 } catch (InterruptedException ex) {
	                     Thread.currentThread().interrupt();
	                 }
	    		 }
	    	}
	    }

	}
	
	private void processMessage(JsonNode message)
	{	
		if (message.has("price")) {
            double currentPrice = message.get("price").asDouble();
            logger.info("Processing message with current price: {}", currentPrice);
            //System.out.println("Current price: " + currentPrice);
            try {
                orderService.checkAndPrepareOrder(currentPrice);
            } catch (Exception e) {
                logger.error("Error in order preparation: {}", e.getMessage(), e);
            }
        } else {
            logger.warn("Received message without price field: {}", message);
        }
        
	}
	
	   public void cancelOrder() {
	        logger.info("Simulating order cancellation");

	        orderService.simulateOrderCancellation();
	    }
}
