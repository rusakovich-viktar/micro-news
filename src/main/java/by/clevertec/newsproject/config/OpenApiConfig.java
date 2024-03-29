package by.clevertec.newsproject.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация для OpenAPI.
 */
@Configuration
public class OpenApiConfig {

    @Value("${micro-news.openapi.dev-url}")
    private String devUrl;

    @Value("${micro-news.openapi.prod-url}")
    private String prodUrl;

    /**
     * Создает и настраивает OpenAPI.
     *
     * @return Объект OpenAPI с информацией о серверах и контактах.
     */
    @Bean
    public OpenAPI myOpenApi() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("mrisviz.rus@gmail.com");
        contact.setName("Victor");
        contact.url("https://github.com/rusakovich-viktar/news-management-system");

        Info info = new Info()
                .title("News-part API")
                .version("1.0")
                .contact(contact);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
