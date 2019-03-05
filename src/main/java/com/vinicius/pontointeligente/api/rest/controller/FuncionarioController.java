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

import com.vinicius.pontointeligente.api.dto.FuncionarioDTO;
import com.vinicius.pontointeligente.api.dto.FuncionarioFindByCpfOrEmailDTO;
import com.vinicius.pontointeligente.api.entities.Funcionario;
import com.vinicius.pontointeligente.api.rest.resource.FuncionarioResource;
import com.vinicius.pontointeligente.api.rest.response.Response;
import com.vinicius.pontointeligente.api.services.FuncionarioService;
import com.vinicius.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/funcionario")
@CrossOrigin(origins = "*")
public class FuncionarioController implements FuncionarioResource {
	
	private static final Logger log = LoggerFactory.getLogger(FuncionarioController.class);
	
	@Autowired
	private FuncionarioService service;
	
	@Override
	public ResponseEntity<Response<FuncionarioDTO>> findByCPFOrEmail(@Valid @RequestBody FuncionarioFindByCpfOrEmailDTO funcionarioFindByCpfOrEmailDtro) {
		Response<FuncionarioDTO> response  = new Response<FuncionarioDTO>();
		
		Optional<Funcionario> func = this.service.buscarPorEmailOuCpf(funcionarioFindByCpfOrEmailDtro.getEmail(), funcionarioFindByCpfOrEmailDtro.getCpf());
		
		if ( !func.isPresent() ) {
			response.getErrors().add("Nenhum funcionario encontrado com o CPF ou EMAIL informado");
			
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData( this.converterDadosParaDto(func.get()) );
		
		return ResponseEntity.ok(response);
	}
	
	@Override
	public ResponseEntity<Response<FuncionarioDTO>> update(Long id, @Valid @RequestBody FuncionarioDTO funcionarioDTO, BindingResult result) {
		log.info("Atualizando funcionario: {}", funcionarioDTO.toString());
		
		Response<FuncionarioDTO> response = new Response<FuncionarioDTO>();
		
		Optional<Funcionario> funcionarioOpt = this.service.buscarPorId(id);
		
		if ( !funcionarioOpt.isPresent() ) {
			result.addError(new ObjectError("Funcionario", "Usuário não encontrato"));
		}
		
		this.atualizarFuncionario(funcionarioOpt.get(), funcionarioDTO, result);
		
		if ( result.hasErrors() ) {
			log.error("Erro ao validar os dados: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		this.service.salvar( funcionarioOpt.get() );
		
		response.setData( converterDadosParaDto(funcionarioOpt.get()) );
		
		return ResponseEntity.ok(response);
	}
	
	private FuncionarioDTO converterDadosParaDto(Funcionario funcionario) {
		FuncionarioDTO funcDTO = new FuncionarioDTO();
		
		funcDTO.setId(funcionario.getId());
		funcDTO.setNome(funcionario.getNome());
		funcDTO.setEmail(funcionario.getEmail());
		
		funcionario.getQtdHorasAlmocoOpt()
			.ifPresent(horasAlmoco -> funcDTO.setQtdHorasAlmoco(Optional.of(Float.toString(horasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt()
			.ifPresent(horasDia -> funcDTO.setQtdHorasTrabalhoDia(Optional.of(Float.toString(horasDia))));
		funcionario.getValorHoraOpt()
			.ifPresent(valorHora -> funcDTO.setValorHora(Optional.of(valorHora.toString())));
		
		return funcDTO;
	}
	
	private void atualizarFuncionario(Funcionario funcionario, FuncionarioDTO funcionarioDTO, BindingResult result) {
		funcionario.setNome(funcionarioDTO.getNome());

		if ( !funcionario.getEmail().equalsIgnoreCase(funcionarioDTO.getEmail()) ) {
			Optional<Funcionario> func = this.service.buscarPorEmail(funcionarioDTO.getEmail());

			func.ifPresent(f -> result.addError(new ObjectError("Email", "Email já cadastrado")));

			funcionario.setEmail(funcionarioDTO.getEmail());
		}

		funcionarioDTO.getSenha()
				.ifPresent(s -> funcionario.setSenha(PasswordUtils.getPasswordHash(funcionarioDTO.getSenha().get())));
		funcionarioDTO.getValorHora()
				.ifPresent(h -> funcionario.setValorHora(new BigDecimal(funcionarioDTO.getValorHora().get())));
		funcionarioDTO.getQtdHorasTrabalhoDia()
				.ifPresent(h -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(funcionarioDTO.getQtdHorasTrabalhoDia().get())));
		funcionarioDTO.getQtdHorasAlmoco()
				.ifPresent(h -> funcionario.setQtdHorasAlmoco(Float.valueOf(funcionarioDTO.getQtdHorasAlmoco().get())));
	}

}
