package com.vinicius.pontointeligente.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

	private static final Logger log = LoggerFactory.getLogger(PasswordUtils.class);

	public PasswordUtils() {

	}

	public static String getPasswordHash(String password) {
		if ( password.isEmpty() || null == password ) {
			
			log.info("Password não informado");
			return password;
		}
		
		log.info("Gerando Hash do password");
		BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
		
		return bCryptEncoder.encode(password);
	}
}
