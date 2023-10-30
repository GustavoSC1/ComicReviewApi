package com.gustavo.comicreviewapi.services;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.RefreshTokenRepository;

@Service
public class ScheduleService {
	
	private static final String CRON_BIRTHDAYS = "0 0 6 1/1 * ?";
	
	private static final String CRON_EXPIRED_TOKENS = "0 0 1 1/1 * ?";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;	
		
	@Scheduled(cron=CRON_BIRTHDAYS)
	public void sendMailToBirthdayUsers() {
		System.out.println("Entrou Java Mail");
		List<User> allBirthdayUsers = userService.findBirthdayUsers();
		
		List<String> mailsList = allBirthdayUsers.stream()
							.map(user -> user.getEmail())
							.collect(Collectors.toList());
				
		emailService.sendHappyBirthdayMails(mailsList);
	}
	
	@Scheduled(cron=CRON_EXPIRED_TOKENS)
	public void deleteAllExpiredTokens() {
		System.out.println("Entrou delete All Expired Tokens");
		refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
	}

}
