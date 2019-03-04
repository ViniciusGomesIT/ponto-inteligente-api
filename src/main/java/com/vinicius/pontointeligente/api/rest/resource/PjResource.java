package com.vinicius.pontointeligente.api.rest.resource;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vinicius.pontointeligente.api.dto.CadastroPjDTO;
import com.vinicius.pontointeligente.api.rest.response.Response;

public interface PjResource {

	//TODO melhorar o mapeamento para não expor a ação...
	@RequestMapping(value = "/cadastro", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseEntity<Response<CadastroPjDTO>> cadastrar(@Valid @RequestBody CadastroPjDTO cadastroPjDto, BindingResult resul);
}
