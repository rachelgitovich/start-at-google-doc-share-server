package docSharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringApp {
    public static void main(String[] args) {
        System.out.println("OVED SOF SOF");
        SpringApplication.run(SpringApp.class, args);
    }
}