package com.camohealth;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Principal;

@SpringBootApplication
public class CamoHealthApplication {


	public static void main(String[] args) {

		SpringApplication.run(CamoHealthApplication.class, args);
	}

}
