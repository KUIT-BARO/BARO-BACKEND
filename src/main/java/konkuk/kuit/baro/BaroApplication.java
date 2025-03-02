package konkuk.kuit.baro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BaroApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaroApplication.class, args);
	}

}
