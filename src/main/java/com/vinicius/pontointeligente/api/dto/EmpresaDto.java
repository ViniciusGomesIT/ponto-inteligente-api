package com.vinicius.pontointeligente.api.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

public class EmpresaDto {

	private Long id;
	
	@NotEmpty(message = "Razão Social não pode ser vazio.")
	@Length(min = 5, max = 200, message = "Razão Sozial deve conter entre 5 e 200 caracteres.")
	private String razaoSocial;
	
	@NotEmpty(message = "CNPJ não pode ser vazio.")
	@CNPJ
	private String cnpj;
	
	private Date dataCriacao;
	private Date dataAtualizacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	@Override
	public String toString() {
		return "EmpresaDto [id=" + id + ", razaoSocial=" + razaoSocial + ", cnpj=" + cnpj + ", dataCriacao="
				+ dataCriacao + ", dataAtualizacao=" + dataAtualizacao + "]";
	}
	
}
