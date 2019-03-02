package com.vinicius.pontointeligente.api.services;

import java.util.List;
import java.util.Optional;

import com.vinicius.pontointeligente.api.entities.Lancamento;

public interface LancamentoService {

	List<Lancamento> findByFuncionarioId(Long id);
	
	Lancamento salvar(Lancamento lancamento);
	
	Optional<Lancamento> buscarPorId(Long id);
	
	void remover(Long id);
}
