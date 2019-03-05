package com.vinicius.pontointeligente.api.rest.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinicius.pontointeligente.api.dto.LancamentoDTO;
import com.vinicius.pontointeligente.api.entities.Funcionario;
import com.vinicius.pontointeligente.api.entities.Lancamento;
import com.vinicius.pontointeligente.api.enums.TipoEnum;
import com.vinicius.pontointeligente.api.services.FuncionarioService;
import com.vinicius.pontointeligente.api.services.LancamentoService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LancamentoControllerTest {
	
	@Autowired
	private MockMvc mock;
	
	@MockBean
	private LancamentoService lancamentoService;
	
	@MockBean
	private FuncionarioService funcionarioService;
	
	private static final String URL_BASE = "/api/lancamento/";
	private static final Long ID_FUNCIONARIO = 1L;
	private static final Long ID_LANCAMENTO = 1L;
	private static final String TIPO_LANCAMENTO = TipoEnum.INICIO_TRABALHO.name();
	private static final Date DATA = new Date();
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Test
	public void testCadastroLancamento() throws Exception {
		Lancamento lancamento = obterDadosLancamento();
		BDDMockito.given(this.funcionarioService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(new Funcionario()));
		BDDMockito.given(this.lancamentoService.salvar(Mockito.any(Lancamento.class))).willReturn(lancamento);
		
		mock.perform( MockMvcRequestBuilders.post(URL_BASE)
				.content( this.obterJsonPost() )
				.contentType( MediaType.APPLICATION_JSON_UTF8_VALUE )
				.accept( MediaType.APPLICATION_JSON_UTF8_VALUE  ))
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$.errors").isEmpty());
	}
	
	@Test
	public void testCadastroLancamentoFuncionarioIdInvalido() throws Exception {
		BDDMockito.given(funcionarioService.buscarPorId(Mockito.anyLong())).willReturn(Optional.empty());
		
		mock.perform( MockMvcRequestBuilders.post(URL_BASE) 
				.content( this.obterJsonPost() )
				.contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
			.andExpect( status().isBadRequest() );
	}
	
	@Test
	public void testRemoverLancamento() throws Exception {
		BDDMockito.given(lancamentoService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(new Lancamento()));
		
		mock.perform( MockMvcRequestBuilders.get(URL_BASE + "delete/" + ID_LANCAMENTO )
				.param( "id", Long.toString(ID_LANCAMENTO)) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$.errors").isEmpty() );
	}
	
	private String obterJsonPost() throws JsonProcessingException {		
		LancamentoDTO lancamentoDTO = new LancamentoDTO();
		
		lancamentoDTO.setId( null );
		lancamentoDTO.setData( this.dateFormat.format(DATA) );
		lancamentoDTO.setTipo( TIPO_LANCAMENTO );
		
		lancamentoDTO.setFuncionarioId( ID_FUNCIONARIO );
		
		ObjectMapper mapper = new ObjectMapper();		
		
		return mapper.writeValueAsString(lancamentoDTO);
	}

	private Lancamento obterDadosLancamento() {
		Lancamento lancamento = new Lancamento();
		
		lancamento.setId(ID_LANCAMENTO);
		lancamento.setData(DATA);
		lancamento.setTipo(TipoEnum.valueOf(TIPO_LANCAMENTO));
		
		lancamento.setFuncionario(new Funcionario());
		lancamento.getFuncionario().setId(ID_FUNCIONARIO);		
		
		return lancamento;
	}
}
