package com.vinicius.pontointeligente.api.rest.resource;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vinicius.pontointeligente.api.dto.FuncionarioDTO;
import com.vinicius.pontointeligente.api.dto.FuncionarioFindByCpfOrEmailDTO;
import com.vinicius.pontointeligente.api.rest.response.Response;

public interface FuncionarioResource {
	
	@RequestMapping(value = "/byCpfOrEmail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseEntity<Response<FuncionarioDTO>> findByCPFOrEmail(@Valid @RequestBody FuncionarioFindByCpfOrEmailDTO funcionarioFindByCpfOrEmailDtro);

	@RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseEntity<Response<FuncionarioDTO>> update(@PathVariable("id") Long id, @Valid @RequestBody FuncionarioDTO funcionarioDTO, BindingResult result);
}
