package com.vinicius.pontointeligente.api.rest.resource;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.vinicius.pontointeligente.api.dto.LancamentoDTO;
import com.vinicius.pontointeligente.api.rest.response.Response;

public interface LancamentoResource {

	@RequestMapping(value = "/{funcionarioId}", method = RequestMethod.GET)
	ResponseEntity<Response<Page<LancamentoDTO>>> findByUserId(@PathVariable("funcionarioId") Long funcionarioId, 
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir
	);
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	ResponseEntity<Response<LancamentoDTO>> findById(@PathVariable("id") Long id);
	
	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseEntity<Response<LancamentoDTO>> salvar(@Valid @RequestBody LancamentoDTO lancamentodto, BindingResult result);
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	ResponseEntity<Response<String>> deletar(@PathVariable("id") Long id);
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseEntity<Response<LancamentoDTO>> update(@PathVariable("id") Long id, @Valid @RequestBody LancamentoDTO lancamentoDTO, BindingResult result);
}
