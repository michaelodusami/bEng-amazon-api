package com.github.michaelodusami.eccomerce_backend;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.michaelodusami.eccomerce_backend.modules.user.entity.User;
import com.github.michaelodusami.eccomerce_backend.modules.user.repository.UserRepository;

@SpringBootApplication
public class EccomerceBackendApplication {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EccomerceBackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EccomerceBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository)
	{
		return args -> {
			if (userRepository.count() == 0)
			{
				var user = User.builder().name("Michael Odusami").password("user").email("modusami03@gmail.com").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

				userRepository.save(user);
				log.info("user created: " + user);
			}
		};
	}

}
