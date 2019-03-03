package com.vinicius.pontointeligente.api.rest.resource;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vinicius.pontointeligente.api.dto.EmpresaDto;
import com.vinicius.pontointeligente.api.rest.response.Response;

public interface EmpresaResource {

	@RequestMapping(value = "/cadastrar", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseEntity<Response<EmpresaDto>> cadastrar(@Valid @RequestBody EmpresaDto empresaDto, BindingResult result);
	
	@RequestMapping(value = "/{cnpj}", method=RequestMethod.GET)
	ResponseEntity<Response<EmpresaDto>> findByCnpj(@PathVariable("cnpj") String cnpj);
}
