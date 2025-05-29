package edu.yonsei.Studymate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudymateApplication {

	public static void main(String[] args) {

		SpringApplication.run(StudymateApplication.class, args);
		System.out.println("Successfully Connected");
	}

}
