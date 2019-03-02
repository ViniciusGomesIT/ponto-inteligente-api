package com.vinicius.pontointeligente.api.services;

import java.util.Optional;

import com.vinicius.pontointeligente.api.entities.Funcionario;

//TODO adicionar mapeamento Swagger
public interface FuncionarioService {

	Optional<Funcionario> buscarPorCpf(String cpf); 
	
	Optional<Funcionario> buscarPorEmail(String email);
	
	Optional<Funcionario> buscarPorEmailOuCpf(String email, String Cpf);

	Optional<Funcionario> buscarPorId(Long id);
	
	Funcionario salvar(Funcionario funcionario);
}
