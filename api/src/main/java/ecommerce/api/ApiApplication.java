package ecommerce.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"ecommerce.api", "ecommerce.core", "ecommerce.infra"})
@EnableJpaRepositories(basePackages = "ecommerce.infra")
@EntityScan(basePackages = "ecommerce.infra")
public class ApiApplication {

    public static void main(String[] args) {
        System.setProperty("user.timezone", "Asia/Ho_Chi_Minh");
        SpringApplication.run(ApiApplication.class, args);
    }

}
