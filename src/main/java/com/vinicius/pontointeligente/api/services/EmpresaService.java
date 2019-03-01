package com.vinicius.pontointeligente.api.services;

import java.util.Optional;

import com.vinicius.pontointeligente.api.entities.Empresa;

//TODO adicionar mapeamento swagger
public interface EmpresaService {

	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	Empresa salvar(Empresa empresa);
}
