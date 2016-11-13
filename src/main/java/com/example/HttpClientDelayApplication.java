package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HttpClientDelayApplication {

	public static void main(String[] args) {
		try (ConfigurableApplicationContext ctx = SpringApplication
				.run(HttpClientDelayApplication.class, args)) {
			HttpClientDelay client = ctx.getBean(HttpClientDelay.class);
			client.run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Bean
	public HttpClientDelayConfiguration httpClientDelayConfigulation() {
		return new HttpClientDelayConfiguration();
	}

}
