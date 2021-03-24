package br.edu.ufu.doutorado.pca.servico.impl;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufu.doutorado.pca.modelo.Grupo;
import br.edu.ufu.doutorado.pca.modelo.Mensagem;
import br.edu.ufu.doutorado.pca.modelo.Pictograma;
import br.edu.ufu.doutorado.pca.servico.interfaces.ITecladoVirtual;
import br.edu.ufu.doutorado.pca.util.GenericComparator;
import br.edu.ufu.doutorado.pca.util.SortType;

public class Frequencia extends Preditor {

	
	private List<Pictograma> pictogramas;
	
	
	public Frequencia(ITecladoVirtual teclado) {
		super(teclado);
		this.pictogramas = new ArrayList<Pictograma>();
	}
	
	public void treinar(List<Mensagem> mensagens) {
		this.pictogramas = Pictograma.getDicionario();
		
		
		for (Mensagem mensagem : mensagens) {
			for (Pictograma pictograma : mensagem.getPictogramas()) {
				
				if (this.pictogramas.indexOf(pictograma) < 0) {
					pictograma.setPeso(1);
					this.pictogramas.add(pictograma);
				} else {
					Pictograma picDic = this.pictogramas.get(this.pictogramas.indexOf(pictograma));
					picDic.setPeso(picDic.getPeso() + 1);
				}
			}
		}
	}
	
	
	
	@Override
	public List<Pictograma> sugerir(Mensagem mensagemAtual, String busca,
			Grupo grupoSelecionado, Integer limite) {
		List<Pictograma> sugestao = Pictograma.filtrar(this.pictogramas, grupoSelecionado);
		
		if (! busca.equals("")) {
			sugestao = this.getTeclado().filtrarPictogramas(busca, sugestao);
		}
		
		GenericComparator.sortList(sugestao, "peso", SortType.DESC);

		if (sugestao.size() >= limite)
			sugestao = Pictograma.sortear(sugestao, limite);
//			sugestao = sugestao.subList(0, limite);
		
		return sugestao;
	}

	@Override
	public Grupo sugerirGrupo(Mensagem mensagemAtual) {
		return mensagemAtual.getPictogramas().get(mensagemAtual.getPictogramas().size() - 1).getGrupo();
	}

	@Override
	public void iniciarNovaSugestao() {
		
	}

	
	
	
}
