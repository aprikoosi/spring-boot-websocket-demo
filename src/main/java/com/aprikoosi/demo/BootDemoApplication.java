package com.aprikoosi.demo;

import com.aprikoosi.demo.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootDemoApplication.class, args);
		System.out.println(SpringContextUtil.getBean("webSocketServerEndpoint"));
	}

}
