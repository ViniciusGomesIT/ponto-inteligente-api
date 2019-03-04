package com.vinicius.pontointeligente.api.rest.controller;

import java.math.BigDecimal;
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

import com.vinicius.pontointeligente.api.dto.CadastroPfDTO;
import com.vinicius.pontointeligente.api.entities.Empresa;
import com.vinicius.pontointeligente.api.entities.Funcionario;
import com.vinicius.pontointeligente.api.enums.PerfilEnum;
import com.vinicius.pontointeligente.api.rest.resource.PfResource;
import com.vinicius.pontointeligente.api.rest.response.Response;
import com.vinicius.pontointeligente.api.services.EmpresaService;
import com.vinicius.pontointeligente.api.services.FuncionarioService;
import com.vinicius.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/pf")
@CrossOrigin(origins = "*")
public class PfController implements PfResource {

	private static final Logger log = LoggerFactory.getLogger(PfController.class);

	private EmpresaService empresaService;
	private FuncionarioService funcionarioService;
	
	private Optional<Empresa> empresa;

	@Autowired
	public PfController(EmpresaService empresaService, FuncionarioService funcionarioService) {
		this.empresaService = empresaService;
		this.funcionarioService = funcionarioService;
	}

	@Override
	public ResponseEntity<Response<CadastroPfDTO>> cadastrar(@Valid @RequestBody CadastroPfDTO cadastroPfDto, BindingResult result) {
		log.info("Iniciando cadastro de PF: {}", cadastroPfDto.toString());
		
		Response<CadastroPfDTO> response = new Response<CadastroPfDTO>();
		
		validaEmpresaEfuncionario(cadastroPfDto, result);
		
		if ( result.hasErrors() ) {
			log.error("Erro ao validar os dados: {}", result.getAllErrors());
			result.getAllErrors().forEach( error -> response.getErrors().add(error.getDefaultMessage()) );
			
			return ResponseEntity.badRequest().body(response);
		}

		Funcionario funcionario = converterDtoFuncionario(cadastroPfDto);
		empresa.ifPresent( empresa -> funcionario.setEmpresa(empresa) );		
		this.funcionarioService.salvar(funcionario);
		
		response.setData( this.converterCadastroPfDto(funcionario) );
				
		return ResponseEntity.ok(response);
	}

	private Funcionario converterDtoFuncionario(CadastroPfDTO cadastroPfDto) {
		Funcionario func = new Funcionario();

		func.setCpf(cadastroPfDto.getCpf());
		func.setEmail(cadastroPfDto.getEmail());
		func.setNome(cadastroPfDto.getNome());
		func.setPerfil(PerfilEnum.ROLE_USUARIO);
		func.setSenha(PasswordUtils.getPasswordHash(cadastroPfDto.getSenha()));
		
		cadastroPfDto.getQtdHorasAlmoco()
			.ifPresent(qtdHorasAlmoco -> func.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		cadastroPfDto.getQtdHorasTrabalhoDia()
			.ifPresent(qtdHorasTrabalhoDia -> func.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));
		cadastroPfDto.getValorHora()
			.ifPresent(valorHora -> func.setValorHora(new BigDecimal(valorHora)));

		return func;
	}

	private void validaEmpresaEfuncionario(CadastroPfDTO cadastroPfDto, BindingResult result) {	
		
		Optional<Funcionario> func = this.funcionarioService.buscarPorEmailOuCpf(cadastroPfDto.getEmail(), cadastroPfDto.getCpf());
		Optional<Empresa> emp = this.empresaService.buscarPorCnpj(cadastroPfDto.getCnpj());
		
		if ( func.isPresent() ) {
			result.addError(new ObjectError("Funcionario", "CPF ou Email já cadastrados."));
		}
		
		if ( emp.isPresent() ) {
			this.empresa = emp;
		} else {
			result.addError(new ObjectError("Emprresa", "Não existe uma empresa cadastrada para o CNPJ informado."));
		}
	}
	

	private CadastroPfDTO converterCadastroPfDto(Funcionario funcionario) {
		CadastroPfDTO dto = new CadastroPfDTO();
		
		dto.setId(funcionario.getId());
		dto.setNome(funcionario.getNome());
		dto.setEmail(funcionario.getEmail());
		dto.setCpf(funcionario.getCpf());
		dto.setSenha(funcionario.getSenha());
		dto.setCnpj(funcionario.getEmpresa().getCnpj());
		
		funcionario.getQtdHorasAlmocoOpt()
			.ifPresent(horasAlmoco -> dto.setQtdHorasAlmoco(Optional.of(Float.toString(horasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt()
			.ifPresent(horasTrabalhoDia -> dto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(horasTrabalhoDia))));
		funcionario.getValorHoraOpt()
			.ifPresent(valorHora -> dto.setValorHora(Optional.of(valorHora.toString())));
		
		return dto;
	}
}
