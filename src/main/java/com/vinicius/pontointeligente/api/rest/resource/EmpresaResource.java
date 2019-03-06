package com.vinicius.pontointeligente.api.rest.resource;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vinicius.pontointeligente.api.dto.EmpresaDTO;
import com.vinicius.pontointeligente.api.rest.response.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "company")
public interface EmpresaResource {

	@ApiResponses({
		@ApiResponse(code = 200, message = "Created"),
		@ApiResponse(code = 404, message = "Not Found"),
		@ApiResponse(code = 500, message = "Internal Server Error"),
		@ApiResponse(code = 401, message = "Forbidden")
	})
	@ApiOperation(value = "Endpoint para cadastro de empresa")
	@RequestMapping(value = "/cadastrar", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseEntity<Response<EmpresaDTO>> cadastrar(@Valid @RequestBody EmpresaDTO empresaDto, BindingResult result);
	
	@RequestMapping(value = "/{cnpj}", method=RequestMethod.GET)
	ResponseEntity<Response<EmpresaDTO>> findByCnpj(@PathVariable("cnpj") String cnpj);
}
