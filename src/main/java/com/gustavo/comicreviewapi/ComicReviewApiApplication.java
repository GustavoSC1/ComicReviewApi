package com.gustavo.comicreviewapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ComicReviewApiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ComicReviewApiApplication.class, args);
	}

}
