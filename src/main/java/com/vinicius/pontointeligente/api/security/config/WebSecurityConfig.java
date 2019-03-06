package com.vinicius.pontointeligente.api.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vinicius.pontointeligente.api.security.JwtAuthenticationEntryPoint;
import com.vinicius.pontointeligente.api.security.filters.JWTAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity ( prePostEnabled = true )
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler ;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(this.userDetailsService)
			.passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
		.disable()
		.exceptionHandling()
		.authenticationEntryPoint(unauthorizedHandler)
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
		.antMatchers("/auth/**", "/api/cadastrar-pj", "/api/cadastrar-pf", "/v2/api-docs",
			"/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**")
		.permitAll()
		.anyRequest()
		.authenticated()
		.and()		
		.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
		.headers().cacheControl();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JWTAuthenticationFilter authenticationTokenFilterBean() {
		return new JWTAuthenticationFilter();
	}

}