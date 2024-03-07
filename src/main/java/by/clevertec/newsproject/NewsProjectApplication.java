package by.clevertec.newsproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NewsProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsProjectApplication.class, args);
    }

}
