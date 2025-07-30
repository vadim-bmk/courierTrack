package com.dvo.order_service.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPIDescription() {
        Server localhostServer = new Server();
        localhostServer.setUrl("http://localhost:8082");
        localhostServer.setDescription("Local environment");
        Contact contact = new Contact();
        contact.setName("Dzgoev Vadim");
        contact.setEmail("vadim-bmk@yandex.ru");
        License license = new License().name("GNU AGPLv3").url("https://www.gnu.org/licenses/agpl-3.0.en.html");
        Info info = new Info()
                .title("Courier Track")
                .version("1.0")
                .contact(contact)
                .description("API for courier track service")
                .termsOfService("http://example.term.url")
                .license(license);
        return new OpenAPI().info(info).servers(List.of(localhostServer));

    }
}
