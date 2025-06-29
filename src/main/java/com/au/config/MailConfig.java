package com.au.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

	private final Environment env;

	public MailConfig(Environment env) {
		this.env = env;
	}

	// First email sender (noreply@acharya.ac.in)
	@Bean
	public JavaMailSender mailSenderPrimary() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(env.getProperty("spring.mail.host"));
		mailSender.setPort(Integer.parseInt(env.getProperty("spring.mail.port")));
		mailSender.setUsername(env.getProperty("spring.mail.username"));
		mailSender.setPassword(env.getProperty("spring.mail.password"));

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

	// Second email sender (acerp@acharya.ac.in)
	@Bean
	public JavaMailSender mailSenderSecondary() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(env.getProperty("second.mail.host"));
		mailSender.setPort(Integer.parseInt(env.getProperty("second.mail.port")));
		mailSender.setUsername(env.getProperty("second.mail.username"));
		mailSender.setPassword(env.getProperty("second.mail.password"));

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

	// Third email sender (noreply1@acharya.ac.in)
	@Bean
	public JavaMailSender mailSenderThird() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(env.getProperty("third.mail.host"));
		mailSender.setPort(Integer.parseInt(env.getProperty("third.mail.port")));
		mailSender.setUsername(env.getProperty("third.mail.username"));
		mailSender.setPassword(env.getProperty("third.mail.password"));

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}
}
