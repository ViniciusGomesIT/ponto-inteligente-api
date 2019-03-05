package com.vinicius.pontointeligente.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vinicius.pontointeligente.api.entities.Lancamento;

public interface LancamentoService {

	List<Lancamento> findByFuncionarioId(Long id);
	
	Lancamento salvar(Lancamento lancamento);
	
	Optional<Lancamento> buscarPorId(Long id);
	
	Page<Lancamento> buscarPorId(Long id, Pageable pageable);
	
	void remover(Long id);
}
