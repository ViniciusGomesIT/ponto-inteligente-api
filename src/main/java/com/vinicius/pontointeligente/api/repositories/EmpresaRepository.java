package com.vinicius.pontointeligente.api.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.vinicius.pontointeligente.api.entities.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

	//Anotação para indicar ao banco que é apenas uma consulta
	//para evitar assim o lock do banco
	@Transactional(readOnly = true)
	Empresa findByCnpj(String cnpj);
}
