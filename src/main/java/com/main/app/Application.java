package com.main.app;

import com.main.app.service.AktivacijaService;
import com.main.app.service.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;


@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class })
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private JavaMailSender mailSender;

    @Bean
    public AktivacijaService aktivacijaService() {
        return new AktivacijaService(korisnikService, mailSender);
    }
}