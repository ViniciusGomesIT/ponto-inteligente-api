package com.vinicius.pontointeligente.api.rest.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinicius.pontointeligente.api.entities.Empresa;
import com.vinicius.pontointeligente.api.services.EmpresaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class EmpresaControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private EmpresaService service;
	
	private static final Long ID = 1L;
	private static final String URL_BUSCA_EMPRESA_BY_CNPJ = "/api/empresa/"; 
	private static final String URL_SALVAR_EMPRESA = "/api/empresa/cadastrar";
	private static final String CNPJ = "81577329000111";
	private static final String RAZAO_SOCIAL = "Empresa Teste Unitario";
	
	@Test
	public void testFindByCNPJInvalido() throws Exception{
		BDDMockito.given(this.service.buscarPorCnpj(Mockito.anyString())).willReturn(Optional.empty());
		
		mvc.perform( MockMvcRequestBuilders.get(URL_BUSCA_EMPRESA_BY_CNPJ + CNPJ)
				.accept(MediaType.APPLICATION_JSON_UTF8) )
		   .andExpect( status().isBadRequest() )
		   .andExpect( jsonPath("$.errors").value("Empresa não encontrada para o CNPJ " + CNPJ) );
	}
	
	@Test
	public void testFindBValido() throws Exception {
		BDDMockito.given(this.service.buscarPorCnpj(Mockito.anyString())).willReturn(this.gerarDadosEmpresa());
		
			mvc.perform( MockMvcRequestBuilders.get(URL_BUSCA_EMPRESA_BY_CNPJ + CNPJ)
					.accept(MediaType.APPLICATION_JSON_UTF8) )
			   .andExpect( status().isOk() )
			   .andExpect( jsonPath("$.data.id").value(ID) )
			   .andExpect( jsonPath("$.data.cnpj", equalTo(CNPJ)) )
			   .andExpect( jsonPath("$.data.razaoSocial", equalTo(RAZAO_SOCIAL)) )
			   .andExpect( jsonPath("$.errors").isEmpty() );
	}
	
	@Test
	public void testSalvar() throws Exception {
		BDDMockito.given(this.service.salvar(Mockito.any(Empresa.class))).willReturn(new Empresa());
		Empresa emp = this.gerarDadosEmpresa().get();		
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonOfEmpresa = mapper.writeValueAsString(emp);
		
		System.out.println(jsonOfEmpresa);
				
		mvc.perform( MockMvcRequestBuilders.post(URL_SALVAR_EMPRESA)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(jsonOfEmpresa) )
		   .andExpect( status().isOk() )
		   .andExpect( jsonPath("$.data.razaoSocial", equalTo(RAZAO_SOCIAL)));
	}

	private Optional<Empresa> gerarDadosEmpresa() {
		Empresa emp = new Empresa();
		
		emp.setId(ID);
		emp.setRazaoSocial(RAZAO_SOCIAL);
		emp.setCnpj(CNPJ);
		emp.setDataAtualizacao(new Date());
		emp.setDataCriacao(new Date());
		
		return Optional.ofNullable(emp);
	}
}
