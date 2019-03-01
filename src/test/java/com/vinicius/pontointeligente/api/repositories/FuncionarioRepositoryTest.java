package com.vinicius.pontointeligente.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.vinicius.pontointeligente.api.entities.Empresa;
import com.vinicius.pontointeligente.api.entities.Funcionario;
import com.vinicius.pontointeligente.api.enums.PerfilEnum;
import com.vinicius.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest {

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	private static final String EMAIL = "emailtest@email.com";
	private static final String CPF = "14536985201";
	
	@Before
	public void setUp() {
		Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
		this.funcionarioRepository.save(obterDadosFuncionario(empresa));
	}
	
	@After
	public final void tearDown() {
		this.empresaRepository.deleteAll();
	}
	
	@Test
	public final void testBuscarFuncionarioPorEmail() throws Exception {
		Funcionario funcionario = this.funcionarioRepository.findByEmail(EMAIL);
		
		assertEquals(EMAIL, funcionario.getEmail());
	}
	
	@Test
	public final void testBuscarFuncionarioPorCpf() throws Exception {
		Funcionario funcionario = this.funcionarioRepository.findByCpf(CPF);
		
		assertEquals(CPF, funcionario.getCpf());
	}
	
	@Test
	public final void testBuscarFuncionarioPorCpfOuEmail() throws Exception {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, EMAIL);
		
		assertEquals(CPF, funcionario.getCpf());
		assertEquals(EMAIL, funcionario.getEmail());
	}
	
	@Test
	public final void testBuscarFuncionarioPorCpfOuEmailParaCpfInvalido() throws Exception {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail("25836914752", EMAIL);
		
		assertNotNull(funcionario);
	}
	
	@Test
	public final void testBuscarFuncionarioPorCpfOuEmailParaCpEEmailfInvalidos() throws Exception {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail("25836914752", "emailinvalido@email.com");
		
		assertNull(funcionario);
	}

	private Funcionario obterDadosFuncionario(Empresa empresa) {
		Funcionario funcionario = new Funcionario();
		
		funcionario.setNome("Usuario Teste Unitário");
		funcionario.setCpf(CPF);
		funcionario.setEmail(EMAIL);
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.getPasswordHash("123456"));
		funcionario.setEmpresa(empresa);
		
		return funcionario;
	}

	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		
		empresa.setRazaoSocial("Empresa Teste");
		empresa.setCnpj("21641521000106");
		
		return empresa;
	}
	
}
