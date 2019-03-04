package com.vinicius.pontointeligente.api.rest.resource;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vinicius.pontointeligente.api.dto.CadastroPfDTO;
import com.vinicius.pontointeligente.api.rest.response.Response;

public interface PfResource {
	
	@RequestMapping(value = "/cadastrar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseEntity<Response<CadastroPfDTO>> cadastrar(@Valid @RequestBody CadastroPfDTO cadastroPfDto, BindingResult result);

}
