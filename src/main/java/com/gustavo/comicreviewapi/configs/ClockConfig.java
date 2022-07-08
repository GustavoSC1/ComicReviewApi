package com.gustavo.comicreviewapi.configs;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfig {
	
	@Bean
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }

}
