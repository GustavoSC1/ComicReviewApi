package com.gustavo.comicreviewapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Value("${application.mail.default-remetent}")
	private String remetent;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendHappyBirthdayMails(List<String> mailsList) {
		String[] mails = mailsList.toArray(new String[mailsList.size()]);

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(remetent);
		mailMessage.setTo(mails);
		mailMessage.setSubject("Comics Review wishes you a Happy Birthday!");
		mailMessage.setText("Our sincerest wishes on your birthday. We hope you have a blessed day full of joy and happiness, "
				+ "and may all your birthday wishes come true.");

		javaMailSender.send(mailMessage);
	}

}
