package ch.keile.keilestatsAPI.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2
/*Indicates that Swagger support should be enabled. 
 * This should have an accompanying '@Configuration' annotation. 
 * Loads all required beans.
 * */
@Configuration
/*Indicates that a class declares one or more @Bean methods and may be
processed by the Spring container to generate bean definitions 
and service requests for those beans at runtime*/
public class SwaggerConfig {

	/* Returns the Swagger2 Interface */
	@Bean
	public Docket keileAPI() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("ch.keile.keilestatsAPI")).paths(regex("/keilestats.*"))
				.build().apiInfo(metaInfo());
	}

	// Titletext to be displayed in the Swagger interface
	private ApiInfo metaInfo() {

		ApiInfo apiInfo = new ApiInfo("Keile Stats API",
				"API for managing " + "statistics of a just-for-fun Icehockey Team", "1.0", "Terms of Service", "", "",
				"");

		return apiInfo;
	}
}
