package com.github.michaelodusami.eccomerce_backend;

import org.springframework.boot.SpringApplication;

public class TestEccomerceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(EccomerceBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
