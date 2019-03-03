package com.vinicius.pontointeligente.api.rest.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinicius.pontointeligente.api.dto.EmpresaDto;
import com.vinicius.pontointeligente.api.entities.Empresa;
import com.vinicius.pontointeligente.api.rest.resource.EmpresaResource;
import com.vinicius.pontointeligente.api.rest.response.Response;
import com.vinicius.pontointeligente.api.services.EmpresaService;

@RestController
@RequestMapping("/api/empresa")
@CrossOrigin(origins = "*")
public class EmpresaController implements EmpresaResource {

	private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);

	@Autowired
	private EmpresaService service;
	
	Response<EmpresaDto> response;

	@Override
	public ResponseEntity<Response<EmpresaDto>> cadastrar(@Valid @RequestBody EmpresaDto empresaDto, BindingResult result) {
		log.info("Iniciando o cadastro de empresa.");
		
		response = new Response<EmpresaDto>();

		validarEmpresaExistente(empresaDto, result);

		if (result.hasErrors()) {
			log.error("Erro na validação dos dados: {}", response.getErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		Empresa empresa = converterEmpresa(empresaDto);
		
		this.service.salvar(empresa);
		
		response.setData( this.converterDtoEmpresa(empresa) );

		return ResponseEntity.ok(response);
	}
	
	@Override
	public ResponseEntity<Response<EmpresaDto>> findByCnpj(String cnpj) {
		log.info("Buscando empresa com o CNPJ: {}", cnpj);
		
		response = new Response<EmpresaDto>();
		
		Optional<Empresa> empresaOpt = this.service.buscarPorCnpj(cnpj);
		
		if ( empresaOpt.isPresent() ) {
			response.setData(this.converterDtoEmpresa(empresaOpt.get()));
			
			return ResponseEntity.ok(response);
		} else {
			response.getErrors().add("Empresa não encontrada para o CNPJ " + cnpj);
			
			return ResponseEntity.badRequest().body(response);
		}
	}

	private void validarEmpresaExistente(EmpresaDto empresaDto, BindingResult result) {
		Optional<Empresa> emp = this.service.buscarPorCnpj(empresaDto.getCnpj());

		if (emp.isPresent()) {
			result.addError(new ObjectError("Empresa", "Empresa já cadastrada!"));
		}
	}

	private Empresa converterEmpresa(EmpresaDto empresaDto) {
		Empresa emp = new Empresa();
		
		emp.setRazaoSocial(empresaDto.getRazaoSocial());
		emp.setCnpj(empresaDto.getCnpj());
		
		return emp;
	}

	private EmpresaDto converterDtoEmpresa(Empresa empresa) {
		EmpresaDto dto = new EmpresaDto();
		
		dto.setId(empresa.getId());
		dto.setRazaoSocial(empresa.getRazaoSocial());
		dto.setCnpj(empresa.getCnpj());
		dto.setDataCriacao(empresa.getDataCriacao());
		dto.setDataAtualizacao(empresa.getDataAtualizacao());
		
		return dto;
	}


}
