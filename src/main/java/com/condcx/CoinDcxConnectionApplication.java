package com.condcx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.condcx.interfaces.CoinDCXCLI;

@SpringBootApplication
public class CoinDcxConnectionApplication implements 		CommandLineRunner{


    @Autowired
    private CoinDCXCLI coinDCXCLI;

	public static void main(String[] args) {
		SpringApplication.run(CoinDcxConnectionApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		coinDCXCLI.start();
	}

}
