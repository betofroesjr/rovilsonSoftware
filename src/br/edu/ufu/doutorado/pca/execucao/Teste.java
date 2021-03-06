package br.edu.ufu.doutorado.pca.execucao;

import java.util.List;

import br.edu.ufu.doutorado.pca.modelo.Mensagem;
import br.edu.ufu.doutorado.pca.modelo.Pictograma;
import br.edu.ufu.doutorado.pca.servico.impl.Frequencia;
import br.edu.ufu.doutorado.pca.servico.impl.Markov;
import br.edu.ufu.doutorado.pca.servico.impl.MarkovFrequencia;
import br.edu.ufu.doutorado.pca.servico.impl.TecladoVirtual;
import br.edu.ufu.doutorado.pca.servico.interfaces.IPreditor;

public class Teste {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
//		IPreditor preditor = new MarkovFrequencia(new TecladoVirtual(), Markov.BIGRAM);
//		IPreditor preditor = new Markov(new TecladoVirtual(), Markov.BIGRAM);
		
		
		String[] arquivos = new String[] {"_a.txt", "_b.txt", "_c.txt", "_d.txt", "_e.txt"};

		for (String arquivo : arquivos) {
			Pictograma.limparDicionario();

			List<Mensagem> mensagens = Mensagem.agruparMensagens(arquivo);
			
			Integer pontoMensagem = 100;//((Double)(mensagens.size() * 0.005)).intValue();
			List<Mensagem> mensagensDigitacao = mensagens.subList(0, pontoMensagem);
			int contPictograma = 0;
			for (Mensagem mensagem : mensagensDigitacao) {
				contPictograma += mensagem.getPictogramas().size();
			}
			
			List<Mensagem> mensagensTreino = mensagens.subList(pontoMensagem, mensagens.size());
			
			
			StringBuilder saidaFreq = new StringBuilder();
			StringBuilder saidaMarkov = new StringBuilder();
			StringBuilder saidaMarkovFreq = new StringBuilder();
			StringBuilder saidaHadoop = new StringBuilder();
			
			System.out.println(String.format("%s\t%s\t%s\n------------------------", arquivo.replace("_", "").replace(".txt", "").toUpperCase(), mensagensDigitacao.size(), contPictograma));
			for (int i = 1; i <= 100; i = i + 9) {
				Integer percentualTreino = i;
				Integer pontoCorte = ((Double)(mensagensTreino.size() * (percentualTreino / 100.))).intValue();
				
				IPreditor preditorFreq = new Frequencia(new TecladoVirtual());
				IPreditor preditorMarkovFreq = new MarkovFrequencia(new TecladoVirtual(), Markov.UNIGRAM);
				IPreditor preditorMarkov = new Markov(new TecladoVirtual(), Markov.UNIGRAM);
				
//				preditorFreq.treinar(mensagensTreino.subList(0, pontoCorte - 1));
//				((Markov)preditorMarkov).treinar(mensagensTreino.subList(0, pontoCorte - 1), preditorFreq);
//				((MarkovFrequencia)preditorMarkovFreq).treinar(preditorMarkov, preditorFreq);
				
				
//				Prancha pranchaFreq = new Prancha(preditorFreq, new TecladoVirtual());
//				Prancha pranchaMarkov = new Prancha(preditorMarkov, new TecladoVirtual());
//				Prancha pranchaMarkovFreq = new Prancha(preditorMarkovFreq, new TecladoVirtual());
				
				System.out.print(String.format("%s\t", percentualTreino));
//				System.out.print(String.format("%s\t", pranchaFreq.iniciarTeste(mensagensDigitacao)));
//				System.out.print(String.format("%s\t", pranchaMarkov.iniciarTeste(mensagensDigitacao)));
//				System.out.print(String.format("%s\n", pranchaMarkovFreq.iniciarTeste(mensagensDigitacao)));
				
			}
			
			
//			System.out.println(saidaFreq.toString());
//			System.out.println(saidaMarkov.toString());
//			System.out.println(saidaMarkovFreq.toString());
			System.out.println(saidaHadoop.toString());				
			System.out.println("------------------------");
		}
		
		
		
		
		
		
	}

}
