package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DockerHostApplication {

	public static void main(String[] args) {
		// run application
		SpringApplication.run(DockerHostApplication.class, args);
	}

}
