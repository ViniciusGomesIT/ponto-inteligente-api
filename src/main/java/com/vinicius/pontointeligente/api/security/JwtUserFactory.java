package com.vinicius.pontointeligente.api.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.vinicius.pontointeligente.api.entities.Funcionario;
import com.vinicius.pontointeligente.api.enums.PerfilEnum;

public class JwtUserFactory {
	
	public static JwtUser create(Funcionario funcionario) {
		return new JwtUser(funcionario.getId(), 
				funcionario.getEmail(), 
				funcionario.getSenha(), 
				mapToGrantedAuthorities(funcionario.getPerfil()));
	}

	private static Collection<? extends GrantedAuthority> mapToGrantedAuthorities(PerfilEnum perfil) {
		List <GrantedAuthority> authorities = new ArrayList <GrantedAuthority>();
		
		authorities.add( new SimpleGrantedAuthority(perfil.toString()) );
		
		return authorities;
	}

}
