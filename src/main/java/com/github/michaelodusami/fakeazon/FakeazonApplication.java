package com.github.michaelodusami.fakeazon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FakeazonApplication {

	// private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FakeazonApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FakeazonApplication.class, args);
	}

	// @Bean
	// CommandLineRunner commandLineRunner(UserRepository userRepository)
	// {
	// 	return args -> {
	// 		if (userRepository.count() == 0)
	// 		{
	// 			var user = User.builder().name("Michael Odusami").password("user").email("modusami03@gmail.com").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

	// 			userRepository.save(user);
	// 			log.info("user created: " + user);
	// 		}
	// 	};
	// }

}
