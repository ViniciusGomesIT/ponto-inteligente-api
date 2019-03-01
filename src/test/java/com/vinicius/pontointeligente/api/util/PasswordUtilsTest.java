package com.vinicius.pontointeligente.api.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vinicius.pontointeligente.api.utils.PasswordUtils;

public class PasswordUtilsTest {

	private static final String PASSWORD = "123456";
	private final BCryptPasswordEncoder bCryptEncodre = new BCryptPasswordEncoder();

	@Test
	public void testGerarHashSenha() throws Exception {
		String hash = PasswordUtils.getPasswordHash(PASSWORD);
		
		assertTrue( bCryptEncodre.matches(PASSWORD, hash) );
	}

}
