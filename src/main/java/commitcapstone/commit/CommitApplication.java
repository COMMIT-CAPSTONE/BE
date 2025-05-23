package commitcapstone.commit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CommitApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommitApplication.class, args);
	}


}
