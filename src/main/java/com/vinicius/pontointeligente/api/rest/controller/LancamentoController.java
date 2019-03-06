package com.vinicius.pontointeligente.api.rest.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinicius.pontointeligente.api.dto.LancamentoDTO;
import com.vinicius.pontointeligente.api.entities.Funcionario;
import com.vinicius.pontointeligente.api.entities.Lancamento;
import com.vinicius.pontointeligente.api.enums.TipoEnum;
import com.vinicius.pontointeligente.api.rest.resource.LancamentoResource;
import com.vinicius.pontointeligente.api.rest.response.Response;
import com.vinicius.pontointeligente.api.services.FuncionarioService;
import com.vinicius.pontointeligente.api.services.LancamentoService;

@RestController
@RequestMapping("/api/lancamento")
@CrossOrigin(origins = "*")
public class LancamentoController implements LancamentoResource {
	
	private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private LancamentoService lancamentoService;
	private FuncionarioService funcionarioService;
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;

	@Autowired
	public LancamentoController(LancamentoService lancamentoService, FuncionarioService funcionarioService) {
		this.lancamentoService = lancamentoService;
		this.funcionarioService = funcionarioService;
	}
	
	@Override
	public ResponseEntity<Response<Page<LancamentoDTO>>> findByUserId(Long funcionarioId, int pag, String ord, String dir) {
		Response<Page<LancamentoDTO>> response = new Response<Page<LancamentoDTO>>();
		
		PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
		
		Page<Lancamento> lancamentos = this.lancamentoService.buscarPorId(funcionarioId, pageRequest);
		Page<LancamentoDTO> lancamentosDTO = lancamentos.map(lancamento -> this.converterParaDTO(lancamento));
		
		response.setData(lancamentosDTO);
		
		return ResponseEntity.ok(response);
	}
	
	@Override
	public ResponseEntity<Response<LancamentoDTO>> findById(Long id) {
		Response<LancamentoDTO> response = new Response<LancamentoDTO>();
		
		log.info("Buscando lancamento par ao ID: {}", id);
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);
		
		if ( !lancamento.isPresent() ) {
			log.error("Lancamento não encontrado");
			
			response.getErrors().add(String.format("Lançamento não encontrado para o id %s", id));
			
			ResponseEntity.badRequest().body(response.getErrors());
		}
			
		 response.setData( this.converterParaDTO(lancamento.get()) );
		
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Response<LancamentoDTO>> salvar(@Valid @RequestBody LancamentoDTO lancamentoDTO, BindingResult result) {
		Response<LancamentoDTO> response = new Response<LancamentoDTO>();
		
		this.validarFuncionario(lancamentoDTO, result);
		
		if ( result.hasErrors() ) {
			log.error("Ocorreram erros na validação dos dados: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		Lancamento lancamento = this.convertarDadosParaModel(lancamentoDTO);
		this.lancamentoService.salvar(lancamento);
		
		response.setData( this.converterParaDTO(lancamento) );
		
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Response<String>> deletar(Long id) {
		Response<String> response = new Response<String>();
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);
		
		if ( !lancamento.isPresent() ) {
			log.error("Não foi encontrado o lancamento com o id infomado: {}", id);
			response.getErrors().add(String.format("Não foi encontrado o lançamento com o id informado: %s", id));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		this.lancamentoService.remover(id);
		
		return ResponseEntity.ok(new Response<String>());
	}
	
	@Override
	public ResponseEntity<Response<LancamentoDTO>> update(Long id, @Valid @RequestBody LancamentoDTO lancamentoDTO, BindingResult result) {
		Response<LancamentoDTO> response = new Response<LancamentoDTO>();
		Lancamento lancamento = new Lancamento();
		
		lancamentoDTO.setId(Optional.of(id));
		
		this.validarFuncionario(lancamentoDTO, result);
		
		this.validarLancamento(lancamentoDTO, result);
		
		if ( result.hasErrors() ) {
			log.error("Houveram erros ao validar os dados: {}", result.getAllErrors());
			
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		lancamento = this.atualizarDadosLancamento(lancamentoDTO);
		
		this.lancamentoService.salvar(lancamento);
		
		response.setData( this.converterParaDTO(lancamento) );
		
		return ResponseEntity.ok(response);
	}

	private LancamentoDTO converterParaDTO(Lancamento lancamento) {
		LancamentoDTO lancamentoDTO = new LancamentoDTO();
		
		lancamentoDTO.setData( this.dateFormat.format(lancamento.getData()) );
		lancamentoDTO.setTipo( lancamento.getTipo().toString() );
		lancamentoDTO.setDescricao( lancamento.getDescricao() );
		lancamentoDTO.setLocalizacao( lancamento.getLocalizacao() );
		lancamentoDTO.setFuncionarioId( lancamento.getFuncionario().getId() );
		lancamentoDTO.setId( Optional.ofNullable(lancamento.getId()) );
		
		return lancamentoDTO;
	}

	private void validarFuncionario(LancamentoDTO lancamentoDTO, BindingResult result) {
		if ( null == lancamentoDTO.getFuncionarioId() ) {
			log.error("Usuário não informado.");
			
			result.addError(new ObjectError("Funcionario", "Funcionário não informado."));
		}
		
		Optional<Funcionario> funcionarioOpt = this.funcionarioService.buscarPorId(lancamentoDTO.getFuncionarioId());
		
		if ( !funcionarioOpt.isPresent() ) {
			log.error(String.format( "Não foi encontrado um funcionário com o id: %s", lancamentoDTO.getFuncionarioId()) );
			result.addError(new ObjectError( "FuncionarioID", String.format( "Não foi encontrado um funcionário com o id: %s", lancamentoDTO.getFuncionarioId())) );
		}
	}
	
	private void validarLancamento(LancamentoDTO lancamentoDTO, BindingResult result) {
		Optional<Lancamento> lancamentoOpt = lancamentoService.buscarPorId( lancamentoDTO.getId().get() );
		Long funcionarioIdFromBase = lancamentoOpt.get().getFuncionario().getId();
		Long funcionadioIdFromRequest = lancamentoDTO.getFuncionarioId();
		
		if ( lancamentoOpt.isPresent() ) {			
			if ( funcionarioIdFromBase != funcionadioIdFromRequest ) {
				log.error("Lancamento {} não pertence ao usuário informado {}", lancamentoDTO.getId().get(), lancamentoDTO.getFuncionarioId() );
				
				result.addError(new ObjectError("Lancamento", String.format("Lancamento %s não pertence ao usuário informado %s", lancamentoDTO.getId().get(), lancamentoDTO.getFuncionarioId())));
			}
		} else {
			log.error("Não foi encontrado um lancamento com o id infomaro: {}", lancamentoDTO.getId().get());
			
			result.addError(new ObjectError("Lancamento", String.format("Não voi encontrado um registro de lancamento com o id informado: %s", lancamentoDTO.getId().get())));
		}
	}
	
	
	private Lancamento atualizarDadosLancamento(LancamentoDTO lancamentoDTO) {	
		Lancamento lancamento = new Lancamento();
		
		try {			
			lancamento = this.lancamentoService.buscarPorId( lancamentoDTO.getId().get() ).get();
			
			lancamento.setData( this.dateFormat.parse(lancamentoDTO.getData()) );
			lancamento.setDescricao( lancamentoDTO.getDescricao() );
			lancamento.setTipo( TipoEnum.valueOf(lancamentoDTO.getTipo()) );
			lancamento.setLocalizacao( lancamentoDTO.getLocalizacao() );
			
		} catch (ParseException e) {
			log.error(String.format("Houve um erro ao converter o DTO para o Model: %s", e));
			e.printStackTrace();
		}
		
		return lancamento;
	}
	
	private Lancamento convertarDadosParaModel(LancamentoDTO lancamentoDTO) {
		Lancamento lanc = new Lancamento();
		Funcionario func = this.funcionarioService.buscarPorId(lancamentoDTO.getFuncionarioId()).get();
		
		try {
			lancamentoDTO.getId().ifPresent(id -> lanc.setId( lancamentoDTO.getId().get() ));
			
			lanc.setData( this.dateFormat.parse(lancamentoDTO.getData()) );
			lanc.setDescricao( lancamentoDTO.getDescricao() );
			lanc.setLocalizacao( lancamentoDTO.getLocalizacao() );
			lanc.setFuncionario( func );
			lanc.setTipo( TipoEnum.valueOf(lancamentoDTO.getTipo()) );
			
		} catch (ParseException e) {
			log.error(String.format("Houve um erro ao converter o DTO para o Model: %s", e));
		}
		
		return lanc;
	}
}
