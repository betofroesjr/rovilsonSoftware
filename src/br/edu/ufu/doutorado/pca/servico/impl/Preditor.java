package br.edu.ufu.doutorado.pca.servico.impl;

import java.util.List;

import br.edu.ufu.doutorado.pca.modelo.Grupo;
import br.edu.ufu.doutorado.pca.modelo.Mensagem;
import br.edu.ufu.doutorado.pca.modelo.Pictograma;
import br.edu.ufu.doutorado.pca.servico.interfaces.IPreditor;
import br.edu.ufu.doutorado.pca.servico.interfaces.ITecladoVirtual;

public abstract class Preditor implements IPreditor {

	private Integer limite;
	private Integer limiteTeclado;
	private ITecladoVirtual teclado;
	
	
	
	
	@Override
	public Integer getLimiteTeclado() {
		return this.limiteTeclado;
	}

	public ITecladoVirtual getTeclado() {
		return teclado;
	}

	public Preditor(ITecladoVirtual teclado) {
		this.limite = 5;
		this.limiteTeclado = 8;
		this.teclado = teclado;
	}
	
	public Integer getLimite() {
		return limite;
	}



	public void setLimite(Integer limite) {
		this.limite = limite;
	}



	@Override
	public abstract List<Pictograma> sugerir(Mensagem mensagemAtual, String busca,
			Grupo grupoSelecionado, Integer limite) throws Exception;

	
	
	
}
