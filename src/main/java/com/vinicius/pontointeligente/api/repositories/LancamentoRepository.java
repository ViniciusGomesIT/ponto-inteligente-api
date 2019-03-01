package com.vinicius.pontointeligente.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.vinicius.pontointeligente.api.entities.Lancamento;

//JPQL
@NamedQueries({
	@NamedQuery(name = "LancamentoRepository.findByFuncionarioId",
			query = "SELECT lanc FROM Lancamento lanc WHERE lanc.funcionario.id = :funcionarioId")
})
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
	@Transactional(readOnly = true)
	List<Lancamento> findByFuncionarioId(@Param("funcionadioId") Long funcionadioId);
	
//	@Transactional(readOnly = true)
//	Page<Lancamento> findByFuncionarioId(@Param("funcionadioId") Long funcionadioId, Pageable pageable);

}
