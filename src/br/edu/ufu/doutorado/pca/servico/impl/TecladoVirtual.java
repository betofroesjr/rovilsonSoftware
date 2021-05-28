package br.edu.ufu.doutorado.pca.servico.impl;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import br.edu.ufu.doutorado.pca.modelo.Mensagem;
import br.edu.ufu.doutorado.pca.modelo.Pictograma;
import br.edu.ufu.doutorado.pca.servico.interfaces.IPreditor;
import br.edu.ufu.doutorado.pca.servico.interfaces.ITecladoVirtual;

public class TecladoVirtual implements ITecladoVirtual {

	
	private static final String[][] MODELO = new String[][]{
		{"a", "e", "i", "o", "u"},
		{"b", "c", "d", "f", "g", "h", "j"},
		{"k", "l", "m", "n", "p", "q", "r"},
		{"s", "t", "v", "w", "x", "y", "z"},
		{"-"}};
	
	
	public static String getPalavra(List<String> chars) {
		String palavra = "";
		for (String car : chars) {
			if (car.substring(0, 1).equals(" ")) {
				palavra += " ";
			} else {
				palavra += obterTecla(car.substring(0, 1));
			}
			
		}
		return palavra;
	}
	
	public static String getCaracteres(int grupo) {
		String palavra = "";
		for (int i = 0; i < MODELO[grupo].length; i++) {
			palavra += MODELO[grupo][i];
		}
		return palavra.toUpperCase();
	}
	
	@Override
	public Integer contarAcaoBusca(IPreditor preditor, Mensagem mensagemAtual,
			Pictograma selecao) throws Exception {
		Integer acao = 0;
		
		String digitacao = "";
		boolean naoAchou = true;
		
		while (naoAchou) {
			acao++;
			digitacao = incrementarDigitacao(selecao.getRotulo(), digitacao);
			List<Pictograma> sugestao = preditor.sugerir(mensagemAtual, digitacao, selecao.getGrupo(), preditor.getLimiteTeclado());
			if (sugestao.indexOf(selecao) >= 0) {
				if (preditor instanceof MarkovFrequencia) {
					((MarkovFrequencia)preditor).adicionarListaPreferencia(selecao);
				}
				return acao;
			}
//			if (digitacao.length() == selecao.getRotulo().length())
//				return acao;
			if (acao > selecao.getRotulo().length() + 4) {
				return acao;
			}
		}
		
		return acao;
	}
	
	private Boolean verificarCaractere(String caractere, String caractereCodificado) {
		Integer posicao = Integer.parseInt(caractereCodificado);
		String[] teclas = MODELO[posicao];
		caractere = normalizarCaractere(caractere);
		for (String letra : teclas) {
			if (caractere.equalsIgnoreCase(letra))
				return true;
		}
		return false;
	}

	public Boolean validarDigitacao(String palavra, String digitacaoCodificada) {
		boolean fimPalavra = digitacaoCodificada.contains(" ");
		digitacaoCodificada = digitacaoCodificada.replace(" ", "");
				
		if (fimPalavra && (palavra.length() != digitacaoCodificada.length()))
			return false;
		
		
		if (digitacaoCodificada.length() > palavra.length())
			return false;
		
		for (int i = 0; i < digitacaoCodificada.length(); i++) {
			if (! verificarCaractere(palavra.charAt(i) + "", digitacaoCodificada.charAt(i) + "")) {
				return false;
			}
		}
		return true;
	}
	
	
	public List<Pictograma> filtrarPictogramas(String busca, List<Pictograma> sugestao) {
		List<Pictograma> novaSugestao = new ArrayList<Pictograma>();
		
		for (Pictograma pic : sugestao) {
			if (this.validarDigitacao(pic.getRotulo(), busca)) {
				novaSugestao.add(pic);
			}
		}
		return novaSugestao;
	}
	
	
	
	private String incrementarDigitacao(String palavra, String digitacaoAtual) {
		if (digitacaoAtual.contains(" "))
			return digitacaoAtual;
		
		if (digitacaoAtual.length() != palavra.length()) {
			return digitacaoAtual + obterTecla(palavra.charAt(digitacaoAtual.length()) + "");
		} else {
			return digitacaoAtual + " ";		
		}
	}
	
	private static String normalizarCaractere(String caractere) {
		return Normalizer.normalize(caractere, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	private static String obterTecla(String caractere) {
		caractere = normalizarCaractere(caractere);
		for (int i = 0; i < MODELO.length; i++) {
			for (int j = 0; j < MODELO[i].length; j++) {
				if (caractere.equalsIgnoreCase(MODELO[i][j])) {
					return String.valueOf(i);
				}
			}
		}
		return "";
	}

	
	
	
	
}
