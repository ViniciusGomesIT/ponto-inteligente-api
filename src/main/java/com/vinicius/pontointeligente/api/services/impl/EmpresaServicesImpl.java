package com.vinicius.pontointeligente.api.services.impl;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinicius.pontointeligente.api.entities.Empresa;
import com.vinicius.pontointeligente.api.repositories.EmpresaRepository;
import com.vinicius.pontointeligente.api.services.EmpresaService;

@Service
public class EmpresaServicesImpl implements EmpresaService {
	
	private static final Logger log = LoggerFactory.getLogger(EmpresaServicesImpl.class);
	
	@Autowired
	private EmpresaRepository repository;

	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		return Optional.ofNullable( repository.findByCnpj(cnpj) );
	}

	@Override
	public Empresa salvar(Empresa empresa) {
		if ( isEmpresaValida(empresa) ) {
			return repository.save(empresa);
		}
		
		log.info("Empresa informada não é válida, devolvendo Nulo");
		return null;
	}

	private boolean isEmpresaValida(Empresa empresa) {
		if ( Objects.isNull(empresa) ) {
			return false;
		} else if ( null == empresa.getCnpj() || empresa.getCnpj().isEmpty() 
				|| null == empresa.getRazaoSocial() || empresa.getRazaoSocial().isEmpty() ) {
			return false;
		}
					
		return true;
	}
}
