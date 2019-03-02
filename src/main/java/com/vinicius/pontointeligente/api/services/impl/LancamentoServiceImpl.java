package com.vinicius.pontointeligente.api.services.impl;

import java.util.List;
import static java.util.Objects.isNull;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinicius.pontointeligente.api.entities.Lancamento;
import com.vinicius.pontointeligente.api.repositories.LancamentoRepository;
import com.vinicius.pontointeligente.api.services.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Override
	public List<Lancamento> findByFuncionarioId(Long id) {

		if (id == 0) {
			log.info("Id informado inválido, devolvendo nulo: {}", id);
			return null;
		}
		return lancamentoRepository.findByFuncionarioId(id);
	}

	@Override
	public Lancamento salvar(Lancamento lancamento) {
		
		if ( isValidLancamento(lancamento) ) {
			return lancamentoRepository.save(lancamento);
		}
		
		log.info("O lancamento informado não é válido: {}", lancamento);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	public Optional<Lancamento> buscarPorId(Long id) {			
		return lancamentoRepository.findById(id);
	}
	
	@Override
	public void remover(Long id) {
		this.lancamentoRepository.deleteById(id);
	}

	private boolean isValidLancamento(Lancamento lancamento) {
		if ( isNull(lancamento) ) {
			return false;
		} else if ( null == lancamento.getTipo() ) {
			return false;
		}
		return true;
	}



}
