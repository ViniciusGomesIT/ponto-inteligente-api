package com.vinicius.pontointeligente.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
//Sem está anotação, o teste falha pois não consegue intanciar o mysql
//serve para informar ao spring que o arquivo de properties a ser carregado é o de test
@ActiveProfiles("test")
public class PontoInteligenteApplicationTests {

	@Test
	public void contextLoads() {
		
	}

}
