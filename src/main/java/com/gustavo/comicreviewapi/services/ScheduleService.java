package com.gustavo.comicreviewapi.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.gustavo.comicreviewapi.entities.User;

@Service
public class ScheduleService {
	
	private static final String CRON_BIRTHDAYS = "0 1 6 1/1 * ?";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
		
	@Scheduled(cron=CRON_BIRTHDAYS)
	public void sendMailToBirthdayUsers() {
		List<User> allBirthdayUsers = userService.findBirthdayUsers();
		List<String> mailsList = allBirthdayUsers.stream()
							.map(user -> user.getEmail())
							.collect(Collectors.toList());
		
		emailService.sendHappyBirthdayMails(mailsList);
	}

}
