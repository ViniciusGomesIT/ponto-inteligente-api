package com.vinicius.pontointeligente.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.vinicius.pontointeligente.api.entities.Funcionario;
import com.vinicius.pontointeligente.api.enums.PerfilEnum;
import com.vinicius.pontointeligente.api.repositories.FuncionarioRepository;
import com.vinicius.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioServiceTest {
	
	@MockBean
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	private static final String CPF = "36925814745";
	private static final String EMAIL = "teste.funcionario.service@email.com";
	
	@Before
	public void setUp() throws Exception {
		BDDMockito.given(funcionarioRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Funcionario()));
		BDDMockito.given(funcionarioRepository.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(funcionarioRepository.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(funcionarioRepository.save(Mockito.any(Funcionario.class))).willReturn(new Funcionario());
		BDDMockito.given(funcionarioRepository.findByCpfOrEmail(Mockito.anyString(), Mockito.anyString())).willReturn(new Funcionario());
	}

	@Test
	public void testSalvarFuncionario() {
		Funcionario funcionario = this.funcionarioService.salvar( obterDadosFuncionario() );
		
		assertNotNull(funcionario);
	}
	
	@Test
	public void testFindByCpf() {
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorCpf(CPF);
		
		assertTrue(funcionario.isPresent());
	}
	
	@Test
	public void testFindByEmail() {
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorEmail(EMAIL);
		
		assertTrue(funcionario.isPresent());
	}

	@Test
	public void testFindById() {
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(1L);
		
		assertTrue(funcionario.isPresent());
	}
	
	@Test
	public void testFindByEmailOrCpf() {
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorEmailOuCpf(EMAIL, CPF);
		
		assertTrue(funcionario.isPresent());
	}

	private Funcionario obterDadosFuncionario() {
		Funcionario funcionario = new Funcionario();
		
		funcionario.setNome("Funcionario Teste");
		funcionario.setCpf(CPF);
		funcionario.setEmail(EMAIL);
		funcionario.setSenha(PasswordUtils.getPasswordHash("123456"));
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		
		return funcionario;
	}

}
