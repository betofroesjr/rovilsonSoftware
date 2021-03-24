package br.edu.ufu.doutorado.pca.servico.interfaces;

import java.util.List;

import br.edu.ufu.doutorado.pca.modelo.Grupo;
import br.edu.ufu.doutorado.pca.modelo.Mensagem;
import br.edu.ufu.doutorado.pca.modelo.Pictograma;

public interface IPreditor {

	
	public List<Pictograma> sugerir(Mensagem mensagemAtual, String busca, Grupo grupoSelecionado, Integer limite);
	
	public void treinar(List<Mensagem> mensagens);
	
	public void iniciarNovaSugestao();
	
	public Integer getLimiteTeclado();
	
	public Integer getLimite();
	
	public Grupo sugerirGrupo(Mensagem mensagemAtual);
	
}
