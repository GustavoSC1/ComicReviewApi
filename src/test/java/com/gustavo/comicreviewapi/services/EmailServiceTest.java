package com.gustavo.comicreviewapi.services;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.TestPropertySource;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

//https://rieckpil.de/use-greenmail-for-spring-mail-javamailsender-junit-5-integration-tests/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Define uma nova fonte de configuração e substitui o valor da propriedade injetanda usando 
// a anotação @Value
//@TestPropertySource(properties = {"application.mail.default-remetent=mail@comic-api.com"})
public class EmailServiceTest {	

	@Autowired
	EmailService emailService;
	
	@RegisterExtension
	static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
			.withConfiguration(GreenMailConfiguration.aConfig().withUser("gustavo", "springboot"))
			.withPerMethodLifecycle(false);
				
	@Test
	public void sendHappyBirthdayMailsTest() throws MessagingException {
		// Scenario
		List<String> list = Arrays.asList("gu.cruz17@hotmail.com");
		
		// Execution
		emailService.sendHappyBirthdayMails(list);
		
		// Verification
		MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
		Assertions.assertThat(receivedMessages.length).isEqualTo(1);
		
		MimeMessage receivedMessage = receivedMessages[0];
		Assertions.assertThat(receivedMessage.getAllRecipients().length).isEqualTo(1);
		Assertions.assertThat(receivedMessage.getAllRecipients()[0].toString()).isEqualTo("gu.cruz17@hotmail.com");
		Assertions.assertThat(receivedMessage.getSubject()).isEqualTo("Comics Review wishes you a Happy Birthday!");
		Assertions.assertThat(GreenMailUtil.getBody(receivedMessage)).isEqualTo("Our sincerest wishes on your birthday. We hope you have a blessed day full of joy and happiness, "
				+ "and may all your birthday wishes come true.");					
	}

}
