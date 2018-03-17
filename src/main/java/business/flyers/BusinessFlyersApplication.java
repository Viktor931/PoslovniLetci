package business.flyers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class BusinessFlyersApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessFlyersApplication.class, args);
	}
}
