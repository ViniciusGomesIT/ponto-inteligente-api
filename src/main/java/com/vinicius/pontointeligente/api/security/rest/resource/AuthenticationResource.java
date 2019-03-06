package com.vinicius.pontointeligente.api.security.rest.resource;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vinicius.pontointeligente.api.rest.response.Response;
import com.vinicius.pontointeligente.api.security.dto.JwtAuthenticationDto;
import com.vinicius.pontointeligente.api.security.dto.TokenDto;

public interface AuthenticationResource {
	
	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseEntity<Response<TokenDto>> gerarTokenJwt(@Valid @RequestBody JwtAuthenticationDto authenticationDto, BindingResult result );
	
	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	ResponseEntity<Response<TokenDto>> gerarRefreshTokenJwt(HttpServletRequest request);
}
