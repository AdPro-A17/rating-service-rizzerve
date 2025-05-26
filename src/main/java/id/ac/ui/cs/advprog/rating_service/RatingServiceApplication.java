package id.ac.ui.cs.advprog.rating_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RatingServiceApplication {

    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            dotenv.entries().forEach(e -> {
                System.setProperty(e.getKey(), e.getValue());
            });
        } catch (Exception e) {
            System.err.println("Error loading .env file: " + e.getMessage());
        }
        SpringApplication.run(RatingServiceApplication.class, args);
    }

}
