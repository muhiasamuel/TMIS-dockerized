package com.example.talent_man.security.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); // replace with your SMTP host
        mailSender.setPort(465); // replace with your SMTP port

        mailSender.setUsername("smuhia621@gmail.com"); // replace with your SMTP username
        mailSender.setPassword("yfoq lgdj cdxv serk"); // replace with your SMTP password

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "true"); // Set to false in production

        return mailSender;
    }
}
