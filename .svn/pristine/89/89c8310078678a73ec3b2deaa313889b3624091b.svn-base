package br.edu.ufu.doutorado.pca.modelo;

import java.util.List;

import br.edu.ufu.doutorado.pca.servico.interfaces.IPreditor;
import br.edu.ufu.doutorado.pca.servico.interfaces.ITecladoVirtual;

public class Prancha {

	
	private IPreditor preditor;
	
	private ITecladoVirtual teclado;
	
	private Grupo grupoSelecionado;
	
	private Mensagem mensagemAtual;

	
	public Prancha(IPreditor preditor, ITecladoVirtual teclado) {
		this.preditor = preditor;
		this.grupoSelecionado = Grupo.SUBSTANTIVO;		
		this.teclado = teclado;
	}
	
	public Integer iniciarTeste(List<Mensagem> textos) {
		Integer acoes = 0;
		for (Mensagem mensagem : textos) {
			acoes += iniciarTeste(mensagem);
		}
		return acoes;
	}
	
	public Integer iniciarTeste(Mensagem texto) {
		this.mensagemAtual = new Mensagem();
		
		Integer acoes = 0;
		for (Pictograma pictograma : texto.getPictogramas()) {
			Integer acao = this.contarAcao(pictograma); 
			acoes += acao;
//			System.out.println(String.format("%d - %s", acao, pictograma));
		}
//		System.out.println("----------------------");
//		System.out.println(String.format("%d ações para selecionar %d pictogramas\n", acoes, texto.getPictogramas().size()));
		
		return acoes;
	}
	
	
	private Integer contarAcao(Pictograma selecao) {
		Integer interacao = 0;
		
		// ALTERAR O GRUPO DE PICTOGRAMA
		if (! this.grupoSelecionado.equals(selecao.getGrupo())) {
			this.grupoSelecionado = selecao.getGrupo();
			interacao++;
		}
		
		this.preditor.iniciarNovaSugestao();
		List<Pictograma> sugestao = this.preditor.sugerir(mensagemAtual, "", grupoSelecionado, this.preditor.getLimite());
		
		if (sugestao.indexOf(selecao) >= 0) {
			// AÇÃO PARA SELECIONAR O PICTOGRAMA NA LISTA
			interacao++;
		} else {
			// ACESSO À OPÇÃO DE BUSCA
			interacao++;			
			// NÚMERO DE INTERAÇÕES NO TECLADO PARA QUE O PICTOGRAMA APAREÇA NA LISTA DE SUGESTÃO
			interacao += this.teclado.contarAcaoBusca(preditor, mensagemAtual, selecao);
			// AÇÃO PARA SELECIONAR O PICTOGRAMA NA LISTA
			interacao++;
		}
		
		this.mensagemAtual.getPictogramas().add(selecao);
		
		
		this.grupoSelecionado = this.preditor.sugerirGrupo(this.mensagemAtual);
		
		
		return interacao;
	}
	
	
	
}
