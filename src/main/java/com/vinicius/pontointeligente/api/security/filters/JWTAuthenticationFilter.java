package com.vinicius.pontointeligente.api.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vinicius.pontointeligente.api.security.model.JwtProperties;
import com.vinicius.pontointeligente.api.security.utils.JWTUtils;


public class JWTAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JWTUtils jwtUtils;
	
	@Autowired
	private JwtProperties properties;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = request.getHeader(properties.getHeader());
		
		if (null != token && token.startsWith(properties.getPrefix())) {
			token = token.substring(7);
		}
		
		String username = jwtUtils.getUserNameFromToken(token);
		
		if ( null != username && null == SecurityContextHolder.getContext().getAuthentication() ) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			
			if ( jwtUtils.isTokenValido(token) ) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( userDetails, null , userDetails.getAuthorities() );
				
				authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails(request) );
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
}
