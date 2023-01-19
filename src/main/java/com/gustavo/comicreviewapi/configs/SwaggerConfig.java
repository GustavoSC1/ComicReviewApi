package com.gustavo.comicreviewapi.configs;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	
	public static final String AUTHORIZATION_HEADER = "Authorization";

	// https://www.treinaweb.com.br/blog/documentando-uma-api-spring-boot-com-o-swagger
	
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.securitySchemes(Arrays.asList(apiKey())) // Tipo de autenticação
				.securityContexts(Arrays.asList(securityContext())) // São especificadas particularidades desta autenticação, como os escopos e endpoints que necessitam de autenticação
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.gustavo.comicreviewapi.resources"))
				.paths(PathSelectors.any())
				.build();
	}
	
	// Inclui o JWT como um cabeçalho de autorização
	private ApiKey apiKey() {
		return new ApiKey("Bearer", AUTHORIZATION_HEADER, "header");
	}
	
	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}
	
	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("Bearer", authorizationScopes));
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
					.title("Comic Book Reviews API")
					.description("API that gathers comic book reviews and gives an overall rating for each issue based on individual reviewer scores. Users can post their own Comics reviews and/or ratings which will be taken into account when calculating an issue's overall rating.")
					.version("1.0")
					.contact(contact())
					.build();
	}
	
	private Contact contact() {
		return new Contact("Gustavo da Silva Cruz", 
						   "https://github.com/GustavoSC1", 
						   "gu.cruz17@hotmail.com");
	}

}
