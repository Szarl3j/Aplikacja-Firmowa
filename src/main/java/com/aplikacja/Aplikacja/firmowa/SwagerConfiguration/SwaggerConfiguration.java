package com.aplikacja.Aplikacja.firmowa.SwagerConfiguration;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EnableWebMvc
@EnableSwagger2
@Import(springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String DEFAULT_INCLUDE_PATTERN = "/Aplikacja-firmowa/.*";

    @Bean
    public Docket swaggerSpringDocket() {
        Contact contact = new Contact("Tomasz Moch", "https://github.com/Szarl3j",
                "toomaszmoch@gmail.com");
        List<VendorExtension> vendorExtensionList = new ArrayList<>();
        ApiInfo applicationInfo = new ApiInfo("Aplikacja Firmowa",
                "Aplikacja dla osób indywidualnych jak i dla małych i średnich firm",
                "1.0.0",
                "https://github.com/Szarl3j",
                contact,
                "MIT",
                "https://github.com/Szarl3j",
                vendorExtensionList);

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(applicationInfo)
                .pathMapping("/")
                .apiInfo(ApiInfo.DEFAULT)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .ignoredParameterTypes(Pageable.class)
                .ignoredParameterTypes(java.sql.Date.class)
                .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()))
                .useDefaultResponseMessages(false);

        docket = docket.select().apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
        return docket;


    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
                .build();

    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessForEveryone");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                new SecurityReference("JWT", authorizationScopes));
    }
}

