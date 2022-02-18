package ru.mshchurkin.mirowidgetsapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger for preety api interface from web
 * 
 * @author Mikhail Shchurkin
 * @created 17.02.2022
 */
@Configuration
public class SwaggerConfiguration {
    
    @Bean
    public Docket api() {

        final Contact contact = new Contact(
            "Mikhail Shchurkin",
            "https://github.com/mshchurkin",
            "mshchurkin@gmail.com");

        final ApiInfo apiInfo = new ApiInfoBuilder()
            .title("Miro Widgets REST API")
            .version("1.0")
            .description("api service for managing widgets")
            .contact(contact)
            .build();

        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
            .paths(PathSelectors.any())
            .build()
            .useDefaultResponseMessages(false);
    }
}
