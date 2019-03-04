package com.vinicius.pontointeligente.api.rest.controller;

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

import com.vinicius.pontointeligente.api.dto.CadastroPjDTO;
import com.vinicius.pontointeligente.api.entities.Empresa;
import com.vinicius.pontointeligente.api.entities.Funcionario;
import com.vinicius.pontointeligente.api.enums.PerfilEnum;
import com.vinicius.pontointeligente.api.rest.resource.PjResource;
import com.vinicius.pontointeligente.api.rest.response.Response;
import com.vinicius.pontointeligente.api.services.EmpresaService;
import com.vinicius.pontointeligente.api.services.FuncionarioService;
import com.vinicius.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/pj")
//Anotação para informar para aceitar requisições de qualquer lugar.
//Ideal só aceitar requisições de um link conhecido para evitar ataques e outros problemas
@CrossOrigin(origins = "*") 
public class PjController implements PjResource {

	private static final Logger log = LoggerFactory.getLogger(PjController.class);
	
	private FuncionarioService funcionarioService;
	private EmpresaService empresaService;
	
	@Autowired
	public PjController(FuncionarioService funcionarioService, EmpresaService empresaService) {
		this.funcionarioService = funcionarioService;
		this.empresaService = empresaService;
	}

	@Override
	public ResponseEntity<Response<CadastroPjDTO>> cadastrar(@Valid @RequestBody CadastroPjDTO cadastroPjDto, BindingResult result) {
		log.info("Inicando o cadastro PJ: {}", cadastroPjDto.toString());
		
		Response<CadastroPjDTO> response = new Response<CadastroPjDTO>();
		
		validarDadosExistentes(cadastroPjDto, result);
		
		if ( result.hasErrors() ) {
			log.error("Error ao validar dados de cadastro PJ: {}", result.getAllErrors());
			result.getAllErrors().forEach( error -> response.getErrors().add(error.getDefaultMessage()) );
			
			return ResponseEntity.badRequest().body(response);
		}
		
		Empresa empresa = this.converterDadosDtoEmpresa(cadastroPjDto);
		Funcionario funcionario = this.converterDadosDtoFuncionario(cadastroPjDto, result);
		
		this.empresaService.salvar(empresa);
		
		funcionario.setEmpresa(empresa);
		this.funcionarioService.salvar(funcionario);
		
		response.setData( this.converterCadastroPjDto(funcionario) );
		
		return ResponseEntity.ok(response);
	}
	
	private void validarDadosExistentes(CadastroPjDTO cadastroPjDto, BindingResult result) {
		this.empresaService.buscarPorCnpj(cadastroPjDto.getCnpj())
			.ifPresent( empresa -> result.addError(new ObjectError("Empresa", "Empresa já cadastrada")) );
		
		this.funcionarioService.buscarPorCpf(cadastroPjDto.getCpf())
			.ifPresent( funcionario -> result.addError(new ObjectError("Funcionario", "CPF já cadastrado")) );
		
		this.funcionarioService.buscarPorEmail(cadastroPjDto.getEmail())
			.ifPresent( funcionario -> result.addError(new ObjectError("Funcionario", "Email já cadastrado")) );
		
	}
	
	private Empresa converterDadosDtoEmpresa(CadastroPjDTO cadastroPjDto) {
		Empresa empresa = new Empresa();
		
		empresa.setCnpj(cadastroPjDto.getCnpj());
		empresa.setRazaoSocial(cadastroPjDto.getRazaoSocial());
		
		return empresa;
	}
	
	private Funcionario converterDadosDtoFuncionario(CadastroPjDTO cadastroPjDto, BindingResult result) {
		Funcionario funcionario = new Funcionario();
		
		funcionario.setNome(cadastroPjDto.getNome());
		funcionario.setCpf(cadastroPjDto.getCpf());
		funcionario.setEmail(cadastroPjDto.getEmail());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.getPasswordHash(cadastroPjDto.getSenha()));
		
		return funcionario;
	}

	private CadastroPjDTO converterCadastroPjDto(Funcionario funcionario) {
		CadastroPjDTO cadastroPjDto = new CadastroPjDTO();
		
		cadastroPjDto.setId(funcionario.getId());
		cadastroPjDto.setNome(funcionario.getNome());
		cadastroPjDto.setEmail(funcionario.getEmail());
		cadastroPjDto.setCpf(funcionario.getCpf());
		cadastroPjDto.setSenha(funcionario.getSenha());
		
		cadastroPjDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		cadastroPjDto.setCnpj(funcionario.getEmpresa().getCnpj());
		
		return cadastroPjDto;
	}

}
