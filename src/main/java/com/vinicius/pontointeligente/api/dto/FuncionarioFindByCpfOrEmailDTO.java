package com.vinicius.pontointeligente.api.dto;

import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.br.CPF;

public class FuncionarioFindByCpfOrEmailDTO {
	
	@CPF(message = "CPF inválido.")
	private String cpf;
	
	@Email(message = "Email inválido")
	private String email;
	
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
