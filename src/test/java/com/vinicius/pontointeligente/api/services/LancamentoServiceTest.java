package com.vinicius.pontointeligente.api.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.vinicius.pontointeligente.api.entities.Lancamento;
import com.vinicius.pontointeligente.api.enums.TipoEnum;
import com.vinicius.pontointeligente.api.repositories.LancamentoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoServiceTest {
	
	@MockBean
	private LancamentoRepository lancamentoRepository;
	
	@Mock
	private List<Lancamento> listaLancamento;
	
	@Autowired
	private LancamentoService lancamentoService;

	@Before
	public void setUp() throws Exception {
		BDDMockito.given(lancamentoRepository.findByFuncionarioId(Mockito.anyLong())).willReturn(listaLancamento);
		BDDMockito.given(lancamentoRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Lancamento()));
		BDDMockito.given(lancamentoRepository.save(Mockito.any(Lancamento.class))).willReturn(new Lancamento());
		
	}

	@Test
	public void testFindByFuncionarioId() {
		List<Lancamento> lista = lancamentoService.findByFuncionarioId(1L);
		
		assertFalse(lista.isEmpty());
	}
	
	@Test
	public void testFindById() {
		Optional<Lancamento> lancamento = lancamentoService.buscarPorId(1L);
		
		assertTrue(lancamento.isPresent());
	}
	
	@Test
	public void testSave() {
		Lancamento lancamento = lancamentoService.salvar( obterDadosLancamento() );
		
		assertNotNull(lancamento);
	}
	
	@Test
	public void testDelete() {
		Lancamento lancamento = lancamentoService.salvar( obterDadosLancamento() );
		
		assertNotNull(lancamento);
	}

	private Lancamento obterDadosLancamento() {
		Lancamento lancamento = new Lancamento();
		
		lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
		
		return lancamento;
	}

}
