package com.vinicius.pontointeligente.api.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import com.vinicius.pontointeligente.api.security.model.JwtProperties;
import com.vinicius.pontointeligente.api.security.service.impl.JwtUserDetailsServiceImpl;
import com.vinicius.pontointeligente.api.security.utils.JWTUtils;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ActiveProfiles("dev")
public class SwaggerConfig {

	private JWTUtils jwtUtils;
	private JwtUserDetailsServiceImpl jwtUserDetailsService;
	private JwtProperties properties;

	public SwaggerConfig(JWTUtils jwtUtils, JwtUserDetailsServiceImpl jwtUserDetailsService, JwtProperties properties) {
		this.jwtUtils = jwtUtils;
		this.jwtUserDetailsService = jwtUserDetailsService;
		this.properties = properties;
	}
	
	@Bean
	public Docket api() {			
		return new Docket(DocumentationType.SWAGGER_2)
				.globalOperationParameters( getParameters() )
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.vinicius.pontointeligente.api.rest.controller"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}
	
	private List<Parameter> getParameters() {
		String token;
		
		UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("admin@vinicius.com");
		token = jwtUtils.obterToken(userDetails);
		
		ParameterBuilder paramBuilder = new ParameterBuilder();
		List<Parameter> parameters = new ArrayList<Parameter>();
				
		paramBuilder
			.name( properties.getHeader() )
			.modelRef( new ModelRef("string") )
			.parameterType("header")
			.required(true)
			.defaultValue( properties.getPrefix() + " " + token )
			.allowMultiple(false)
			.build();
		
		parameters.add(paramBuilder.build());
		
		return parameters;
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Ponto Inteligente API")
				.description("Documentação da API de Ponto Inteligente")
				.version("1.0")
				.build();
	}
}
