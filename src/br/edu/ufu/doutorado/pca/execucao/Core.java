package br.edu.ufu.doutorado.pca.execucao;

import java.util.List;

import br.edu.ufu.doutorado.pca.modelo.Grupo;
import br.edu.ufu.doutorado.pca.modelo.Mensagem;
import br.edu.ufu.doutorado.pca.modelo.Pictograma;
import br.edu.ufu.doutorado.pca.servico.impl.Frequencia;
import br.edu.ufu.doutorado.pca.servico.impl.Markov;
import br.edu.ufu.doutorado.pca.servico.impl.MarkovFrequencia;
import br.edu.ufu.doutorado.pca.servico.impl.TecladoVirtual;
import br.edu.ufu.doutorado.pca.servico.interfaces.IPreditor;

public class Core {

	public static String DIR = "fxmlTeste/";
	
	private IPreditor preditorFreq = new Frequencia(new TecladoVirtual());
	private IPreditor preditorMarkovFreq = new MarkovFrequencia(new TecladoVirtual(), Markov.UNIGRAM);
	private IPreditor preditorMarkov = new Markov(new TecladoVirtual(), Markov.UNIGRAM);
	
	private IPreditor preditorSelecionado;
	
	public void init() throws Exception{
		
		String[] arquivos = new String[] {"_a.txt", "_b.txt", "_c.txt", "_d.txt", "_e.txt"};

		String arquivo = "_b.txt";

		Pictograma.limparDicionario();

		List<Mensagem> mensagens = Mensagem.agruparMensagens(DIR+arquivo);

		Integer pontoMensagem = 100;//((Double)(mensagens.size() * 0.005)).intValue();
		List<Mensagem> mensagensDigitacao = mensagens.subList(0, pontoMensagem);
		int contPictograma = 0;
		for (Mensagem mensagem : mensagensDigitacao) {
			contPictograma += mensagem.getPictogramas().size();
		}

		List<Mensagem> mensagensTreino = mensagens.subList(pontoMensagem, mensagens.size());

		Integer percentualTreino = 10;//100;
		Integer pontoCorte = ((Double)(mensagensTreino.size() * (percentualTreino / 100.))).intValue();

		preditorFreq.treinar(mensagensTreino.subList(0, pontoCorte - 1));
		((Markov)preditorMarkov).treinar(mensagensTreino.subList(0, pontoCorte - 1), preditorFreq);
		((MarkovFrequencia)preditorMarkovFreq).treinar(preditorMarkov, preditorFreq);

		preditorSelecionado = preditorFreq;			
	}
	
	public List<Pictograma> listaSugestao(Mensagem mensagem, Grupo grupo, int limite) throws Exception {
		return listaSugestao(mensagem, grupo, "", limite);
	}
	
	public List<Pictograma> listaSugestao(Mensagem mensagem, Grupo grupo, String busca, int limite) throws Exception {
		return preditorSelecionado.sugerir(mensagem, busca, grupo, limite);
	}
	
	public Grupo sugerirGrupo(Mensagem mensagem) {
		return preditorSelecionado.sugerirGrupo(mensagem);
	}
	
	public void iniciarPesquisa() {
		preditorSelecionado.iniciarNovaSugestao();
	}
	
	public IPreditor getPreditorSelecionado() {
		return preditorSelecionado;
	}
	
}
