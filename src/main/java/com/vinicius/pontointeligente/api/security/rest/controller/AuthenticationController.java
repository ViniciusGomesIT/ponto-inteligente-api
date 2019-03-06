package com.vinicius.pontointeligente.api.security.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinicius.pontointeligente.api.rest.response.Response;
import com.vinicius.pontointeligente.api.security.dto.JwtAuthenticationDto;
import com.vinicius.pontointeligente.api.security.dto.TokenDto;
import com.vinicius.pontointeligente.api.security.model.JwtProperties;
import com.vinicius.pontointeligente.api.security.rest.resource.AuthenticationResource;
import com.vinicius.pontointeligente.api.security.utils.JWTUtils;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController implements AuthenticationResource {
	
	private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
	
//	private AuthenticationManager authenticationManager;
	
//	private ProviderManager providerManager = new ProviderManager(Arrays.asList( (AuthenticationProvider) new AuthProvider()));
	
	@Autowired
	private JWTUtils jwtUtils;
	
	@Autowired
	private JwtProperties properties;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public ResponseEntity<Response<TokenDto>> gerarTokenJwt(@Valid @RequestBody JwtAuthenticationDto authenticationDto, BindingResult result) {
		Response<TokenDto> response = new Response<TokenDto>();

		log.info("Gerando token para o email {}.", authenticationDto.getEmail());
		Authentication authentication = this.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationDto.getEmail(), authenticationDto.getSenha()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDto.getEmail());
		BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
		
		if ( !bCryptEncoder.matches(authenticationDto.getSenha(), userDetails.getPassword()) ) {
			result.addError(new ObjectError("Token", "Credenciais inválidas "));
		}
		
		if (result.hasErrors()) {
			log.error("Erro ao geraro token: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		String token = jwtUtils.obterToken(userDetails);
		response.setData(new TokenDto(token));

		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Response<TokenDto>> gerarRefreshTokenJwt(HttpServletRequest request) {
		Response<TokenDto> response = new Response<TokenDto>();
		Optional<String> token = Optional.ofNullable( request.getHeader(properties.getHeader()) );
		
		if (token.isPresent() && token.get().startsWith(properties.getPrefix())) {
			token = Optional.of(token.get().substring(7));
		}
		
		if (!token.isPresent()) {
			response.getErrors().add("Token não informado.");
		} else if ( !jwtUtils.isTokenValido(token.get()) ) {
			response.getErrors().add("Token inválido ou expirado.");
		}
		
		if ( !response.getErrors().isEmpty() ) {
			return ResponseEntity.badRequest().body(response);
		}
		
		String refreshedToken = jwtUtils.refreshToken(token.get());
		
		response.setData(new TokenDto(refreshedToken));
		
		return ResponseEntity.ok(response);
	}
	
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String username = auth.getName();
        String password = auth.getCredentials().toString();
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        
        userDetails.getAuthorities().forEach(authority -> grantedAuths.add(new SimpleGrantedAuthority(authority.getAuthority())));
        
        return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
    }

}
