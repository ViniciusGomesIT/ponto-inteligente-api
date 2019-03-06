package com.vinicius.pontointeligente.api.security.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

	private String secret;
	private int expirationInSeconds;
	private String header;
	private String prefix;
	private String userDefault;
	private String passwordDefault;
	private String roleDefault;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public int getExpirationInSeconds() {
		return expirationInSeconds;
	}

	public void setExpirationInSeconds(int expirationInSeconds) {
		this.expirationInSeconds = expirationInSeconds;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getUserDefault() {
		return userDefault;
	}

	public void setUserDefault(String userDefault) {
		this.userDefault = userDefault;
	}

	public String getPasswordDefault() {
		return passwordDefault;
	}

	public void setPasswordDefault(String passwordDefault) {
		this.passwordDefault = passwordDefault;
	}

	public String getRoleDefault() {
		return roleDefault;
	}

	public void setRoleDefault(String roleDefault) {
		this.roleDefault = roleDefault;
	}

}
