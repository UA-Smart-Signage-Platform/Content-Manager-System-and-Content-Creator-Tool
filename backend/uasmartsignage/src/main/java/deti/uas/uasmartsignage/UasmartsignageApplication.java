package deti.uas.uasmartsignage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class UasmartsignageApplication {

	public static void main(String[] args) {
		SpringApplication.run(UasmartsignageApplication.class, args);
	}

}
