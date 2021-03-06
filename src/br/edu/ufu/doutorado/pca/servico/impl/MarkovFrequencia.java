package br.edu.ufu.doutorado.pca.servico.impl;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufu.doutorado.pca.modelo.Grupo;
import br.edu.ufu.doutorado.pca.modelo.Matriz;
import br.edu.ufu.doutorado.pca.modelo.MatrizGrupo;
import br.edu.ufu.doutorado.pca.modelo.Mensagem;
import br.edu.ufu.doutorado.pca.modelo.Pictograma;
import br.edu.ufu.doutorado.pca.servico.interfaces.IPreditor;
import br.edu.ufu.doutorado.pca.servico.interfaces.ITecladoVirtual;
import br.edu.ufu.doutorado.pca.util.GenericComparator;
import br.edu.ufu.doutorado.pca.util.SortType;

public class MarkovFrequencia extends Preditor {

	private List<Matriz> matriz;
	
	private List<Pictograma> historicoSugestao;
	
	private List<Pictograma> listaPreferencia;
	
	private List<MatrizGrupo> matrizGrupo;
	
	private IPreditor frequencia;
	
	private Integer gram;
	
	
	
	public MarkovFrequencia(ITecladoVirtual teclado, Integer gram) {
		super(teclado);
		this.frequencia = new Frequencia(teclado);
		this.matriz = new ArrayList<Matriz>();
		this.matrizGrupo = new ArrayList<MatrizGrupo>();
		this.gram = gram;
		this.historicoSugestao = new ArrayList<Pictograma>();
		this.listaPreferencia = new ArrayList<Pictograma>();
	}

	public static final int UNIGRAM = 1;
	public static final int BIGRAM = 2;
	public static final int TRIGRAM = 3;
	
	public void adicionarListaPreferencia(Pictograma pictograma) {
		if (this.listaPreferencia.indexOf(pictograma) < 0) {
			this.listaPreferencia.add(pictograma);			
		}
	}
	
	@Override
	public List<Pictograma> sugerir(Mensagem mensagemAtual, String busca,
			Grupo grupoSelecionado, Integer limite) throws Exception {
		
		// SE A MENSAGEM ATUAL POSSUI O TAMANHO MÍNIMO
		if (mensagemAtual.getPictogramas().size() >= this.gram) {
			List<Pictograma> ngram = new ArrayList<Pictograma>();
			
			for (int i = 0; i < this.gram; i++) {
				int idx = mensagemAtual.getPictogramas().size() - this.gram + i;
				ngram.add(mensagemAtual.getPictogramas().get(idx));
			}
			
			List<Matriz> matrizSugestao = Matriz.filtrar(this.matriz, ngram);
			
			GenericComparator.sortList(matrizSugestao, "peso", SortType.DESC);
			
			List<Pictograma> sugestao = Pictograma.parse(matrizSugestao);
			
			sugestao = Pictograma.filtrar(sugestao, grupoSelecionado);
			sugestao = Pictograma.removerElementos(sugestao, this.historicoSugestao);
			
			if (! busca.equals("")) {
				sugestao = this.getTeclado().filtrarPictogramas(busca, sugestao);
				Pictograma.removerElementos(sugestao, this.listaPreferencia);
				sugestao.addAll(this.getTeclado().filtrarPictogramas(busca, this.listaPreferencia));
			}
			
			if (sugestao.size() >= limite) {
				sugestao = Pictograma.sortear(sugestao, limite);
//				sugestao = sugestao.subList(0, limite);
			} else { // SE ESTIVER SOBRANDO ESPAÇO NA LISTA DE SUGESTÕES
				
 				List<Pictograma> sugestaoFrequencia = Pictograma.filtrar(Pictograma.getDicionario(), grupoSelecionado);
				
				if (! busca.equals("")) {
					sugestaoFrequencia = this.getTeclado().filtrarPictogramas(busca, sugestaoFrequencia);
				}
				sugestaoFrequencia = Pictograma.removerElementos(sugestaoFrequencia, this.historicoSugestao);
				
				GenericComparator.sortList(sugestaoFrequencia, "peso", SortType.DESC);
				
				for (int i = 0; sugestao.size() < limite && i < sugestaoFrequencia.size(); i++) {
					if (! sugestao.contains(sugestaoFrequencia.get(i))) {
						sugestao.add(sugestaoFrequencia.get(i));
					}
				}
				
				if (sugestao.size() >= limite)
					sugestao = sugestao.subList(0, limite);
			}

			historicoSugestao.addAll(sugestao);
			return sugestao;
		} else {
			List<Pictograma> sugestaoFrequencia = Pictograma.filtrar(Pictograma.getDicionario(), grupoSelecionado);
			
			if (! busca.equals("")) {
				sugestaoFrequencia = this.getTeclado().filtrarPictogramas(busca, sugestaoFrequencia);
			}
			sugestaoFrequencia = Pictograma.removerElementos(sugestaoFrequencia, this.historicoSugestao);
			
			GenericComparator.sortList(sugestaoFrequencia, "peso", SortType.DESC);
			
			if (sugestaoFrequencia.size() >= limite)
				sugestaoFrequencia = sugestaoFrequencia.subList(0, limite);
			
			return sugestaoFrequencia;
		}
	}
	
	public void treinar(IPreditor markov, IPreditor frequencia) {
		this.matriz = ((Markov)markov).matriz;
		this.matrizGrupo = ((Markov)markov).matrizGrupo;
	}

	@Override
	public void treinar(List<Mensagem> mensagens) throws Exception {
		this.frequencia.treinar(mensagens);
		
		for (Mensagem mensagem : mensagens) {
			for (int i = this.gram; i < mensagem.getPictogramas().size(); i++) {
				List<Pictograma> pictogramas = new ArrayList<Pictograma>();
				List<Grupo> grupos = new ArrayList<Grupo>();
				for (int j = i - this.gram; j < i; j++) {
					pictogramas.add(mensagem.getPictogramas().get(j));
					grupos.add(mensagem.getPictogramas().get(j).getGrupo());
				}
				Matriz item = new Matriz();
				item.setNgram(pictogramas);
				item.setVizinhanca(mensagem.getPictogramas().get(i));
				item.setPeso(1);
				
				int idx = matriz.indexOf(item); 
				if (idx >= 0) {
					matriz.get(idx).setPeso(matriz.get(idx).getPeso() + 1);
				} else {
					matriz.add(item);
				}
				
				MatrizGrupo itemGrupo = new MatrizGrupo();
				itemGrupo.setNgram(grupos);
				itemGrupo.setVizinhanca(mensagem.getPictogramas().get(i).getGrupo());
				itemGrupo.setPeso(1);
				
				idx = matrizGrupo.indexOf(itemGrupo); 
				if (idx >= 0) {
					matrizGrupo.get(idx).setPeso(matrizGrupo.get(idx).getPeso() + 1);
				} else {
					matrizGrupo.add(itemGrupo);
				}
			}
		}
		
//		for (Matriz matriz : this.matriz) {
//			System.out.println(matriz);
//		}
//		System.out.println("\n\n\n\n");
//		for (MatrizGrupo matriz : this.matrizGrupo) {
//			System.out.println(matriz);
//		}
	}

	@Override
	public Grupo sugerirGrupo(Mensagem mensagemAtual) {
		Grupo grupo = this.frequencia.sugerirGrupo(mensagemAtual);
		
		if (mensagemAtual.getPictogramas().size() >= this.gram) {
			List<Grupo> grupos = new ArrayList<Grupo>();
			for (int i = 0; i < this.gram; i++) {
				int idx = mensagemAtual.getPictogramas().size() - this.gram + i;
				grupos.add(mensagemAtual.getPictogramas().get(idx).getGrupo());
			}
			List<MatrizGrupo> sugestao = MatrizGrupo.filtrar(this.matrizGrupo, grupos);
			if (sugestao.size() > 0) {
				GenericComparator.sortList(sugestao, "peso", SortType.DESC);
				return sugestao.get(0).getVizinhanca();
			}
		}
		
		return grupo;
	}

	@Override
	public void iniciarNovaSugestao() {
		this.historicoSugestao = new ArrayList<Pictograma>();
	}
	
}
