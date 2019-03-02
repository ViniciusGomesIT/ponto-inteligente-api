package com.vinicius.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinicius.pontointeligente.api.entities.Funcionario;
import com.vinicius.pontointeligente.api.repositories.FuncionarioRepository;
import com.vinicius.pontointeligente.api.services.FuncionarioService;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {
	
	private static final Logger log = LoggerFactory.getLogger(FuncionarioServiceImpl.class);
	
	@Autowired
	private FuncionarioRepository repository;

	@Override
	public Optional<Funcionario> buscarPorCpf(String cpf) {
		return Optional.ofNullable( repository.findByCpf(cpf) );
	}

	@Override
	public Optional<Funcionario> buscarPorEmail(String email) {		
		return Optional.ofNullable( repository.findByEmail(email) );
	}

	@Override
	public Optional<Funcionario> buscarPorEmailOuCpf(String email, String cpf) {
		return Optional.ofNullable(repository.findByCpfOrEmail(cpf, email));

	}

	@Override
	public Funcionario salvar(Funcionario funcionario) {		
		if ( isFuncionadioValido(funcionario) ) {
			return repository.save(funcionario);
		}
		
		log.info("Funcionário inválido");
		return null;
	}
	
	@Override
	public Optional<Funcionario> buscarPorId(Long id) {		
		return repository.findById(id);
	}

	private boolean isFuncionadioValido(Funcionario funcionario) {
		if ( null == funcionario ) {
			return false;
		} else if ( null == funcionario.getCpf() 	|| funcionario.getCpf().isEmpty()
				|| null  == funcionario.getNome() 	|| funcionario.getNome().isEmpty() 
				|| null  == funcionario.getEmail() 	|| funcionario.getEmail().isEmpty()
				|| null  == funcionario.getSenha()	|| funcionario.getSenha().isEmpty()
				|| null  == funcionario.getPerfil() ) {
			
			return false;
		}
		
		return true;
	}
}
