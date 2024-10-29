package com.condcx.interfaces;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.condcx.service.Orderservice;
import com.condcx.websocket.CoinDCXWebSocketService;

@Component
public class CoinDCXCLI 
{
	@Autowired
	private CoinDCXWebSocketService coinDCXWebSocketService;
	@Autowired
	private Orderservice orderService;
	
	public void start()
	{
		Scanner sc = new  Scanner(System.in);
		
		  // Get user inputs for symbol and trigger price
        System.out.println("Enter the symbol (e.g., BTCINR): ");
        String symbol = sc.nextLine();
        
        System.out.println("Enter the trigger price: ");
        double triggerPrice = sc.nextDouble();
        
        // Initialize tracking
        System.out.println("Starting tracking for symbol: " + symbol + " at trigger price: " + triggerPrice);
        orderService.setSymbol(symbol);
        orderService.setTriggerPrice(triggerPrice);
        
        try {
            coinDCXWebSocketService.initialize(symbol, triggerPrice);
            coinDCXWebSocketService.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // CLI options
        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Stop Tracking");
            System.out.println("2. Simulate Order Cancellation");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    coinDCXWebSocketService.close();
                    System.out.println("Stopped tracking.");
                    break;
                case 2:
                    coinDCXWebSocketService.cancelOrder();
                    break;
                case 3:
                    coinDCXWebSocketService.close();
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
	}
}
