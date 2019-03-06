package com.vinicius.pontointeligente.api.services.impl;

import java.util.List;
import static java.util.Objects.isNull;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

		if (null == id) {
			log.info("Id informado inválido, devolvendo nulo: {}", id);
			return null;
		}
		return lancamentoRepository.findByFuncionarioId(id);
	}

	@Override
	@CachePut(value = "lancamentoPorId")
	public Lancamento salvar(Lancamento lancamento) {
		
		if ( isValidLancamento(lancamento) ) {
			return lancamentoRepository.save(lancamento);
		}
		
		log.info("O lancamento informado não é válido: {}", lancamento);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	@Cacheable(value = "lancamentoPorId")
	public Optional<Lancamento> buscarPorId(Long id) {			
		return lancamentoRepository.findById(id);
	}
	
	@Override
	public Page<Lancamento> buscarPorId(Long id, Pageable pageable) {
		return lancamentoRepository.findByFuncionarioId(id, pageable);
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
