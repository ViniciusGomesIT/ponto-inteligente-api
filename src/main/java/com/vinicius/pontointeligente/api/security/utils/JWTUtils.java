package com.vinicius.pontointeligente.api.security.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.vinicius.pontointeligente.api.security.model.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtils {

	@Autowired
	private JwtProperties properties;
	
	static final String CLAIM_KEY_USERNAME = "sub" ;
	static final String CLAIM_KEY_ROLE = "role" ;
	static final String CLAIM_KEY_CREATED = "created" ;

	public String getUserNameFromToken(String token) {
		String userName;
		
		try {
			
			Claims claims = getClaimsFromToken(token);
			userName = claims.getSubject();
			
		} catch (Exception e) {
			userName = null;
		}
		
		return userName;
	}
	
	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		
		try {
			Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
			
		} catch (Exception e) {
			expiration = null;
		}
		
		return expiration;
	}
	
	public String refreshToken(String token) {
		String refreshedToken;		
		try {
			Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			
			refreshedToken = gerarToken(claims);
			
		} catch (Exception e) {
			refreshedToken = null;
		}
		
		return refreshedToken;
	}
	
	public boolean isTokenValido(String token) {
		return !isTokenExpirado(token);
	}
	
	public String obterToken(UserDetails details) {
		Map <String, Object> claims = new HashMap<>();
		
		claims.put(CLAIM_KEY_USERNAME, details.getUsername());
		
		details.getAuthorities().forEach(authority -> claims.put(CLAIM_KEY_ROLE, authority.getAuthority()));
		
		claims.put(CLAIM_KEY_CREATED, new Date());
		
		return gerarToken(claims);
	}

	private Claims getClaimsFromToken(String token) {
		Claims claims;
		
		try {
			
			claims = Jwts.parser()
					.setSigningKey(properties.getSecret())
					.parseClaimsJws(token)
					.getBody();
			
		} catch (Exception e) {
			claims = null;
		}
		
		return claims;
	}
	

	private String gerarToken(Map<String, Object> claims) {				
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration( gerarDataExpiracao() )
				.signWith( SignatureAlgorithm.HS512, properties.getSecret() )
				.compact();
	}

	private Date gerarDataExpiracao() {
		return new Date( System.currentTimeMillis() + (properties.getExpirationInSeconds() * 1000) );
	}
	
	private boolean isTokenExpirado(String token) {
		Date dataExpedicao = this.getExpirationDateFromToken(token);
		
		if ( null == dataExpedicao ) {
			return true;
		}
		
		return dataExpedicao.before(new Date());
	}
}
