package br.edu.ufu.doutorado.pca.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import br.edu.ufu.doutorado.pca.execucao.Core;
import br.edu.ufu.doutorado.pca.modelo.Grupo;
import br.edu.ufu.doutorado.pca.modelo.Mensagem;
import br.edu.ufu.doutorado.pca.modelo.Palavra;
import br.edu.ufu.doutorado.pca.modelo.Pictograma;
import br.edu.ufu.doutorado.pca.servico.impl.TecladoVirtual;
import br.edu.ufu.doutorado.pca.view.fxml.ISceneAcionador;
import br.edu.ufu.doutorado.pca.view.fxml.PictogramaGrupo;
import br.edu.ufu.doutorado.pca.view.fxml.PictogramaImagem;

public class BuscaPictograma extends VBox implements Initializable, ISceneAcionador {

	@FXML
	protected TilePane hbSugestao;
	@FXML
	protected HBox hbBusca;
	@FXML
	protected Label lbBack;
	@FXML
	protected Label lbPalavra;
	@FXML
	protected Label lbVoltar;
	@FXML
	protected Label lbTecla1;
	@FXML
	protected Label lbTecla2;
	@FXML
	protected Label lbTecla3;
	@FXML
	protected Label lbTecla4;
	@FXML
	protected Label lbTecla5;
	@FXML
	protected VBox vbxTecla1;
	@FXML
	protected VBox vbxTecla2;
	@FXML
	protected VBox vbxTecla3;
	@FXML
	protected VBox vbxTecla4;
	@FXML
	protected VBox vbxTecla5;
	@FXML
	protected Pane pnVoltar;
	@FXML
	protected Pane pnBack;
	
	private Mensagem mensagemColeta;
	
	private boolean coleta;
	
	private Mensagem mensagemAtual;
	
	private Grupo grupo;
	
	private Core core;
	
	private List<String> palavras;
	
	private List<String> textoAtual;
	
	
	public void abrir() {
		core.iniciarPesquisa();
		textoAtual = new ArrayList<String>();
		hbBusca.getChildren().clear();
		hbSugestao.getChildren().clear();
		sugerir();
	}
	
	private void sugerir() {
		// CARREGAR LISTA DE PICTOGRAMAS INICIAL
		int limitePictograma = palavras.size();
		List<Pictograma> sugestaoInicial = core.listaSugestao(mensagemAtual, grupo, TecladoVirtual.getPalavra(textoAtual), limitePictograma);
		carregarSugestao(sugestaoInicial);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		palavras = Palavra.palavras(SimuladorView.TAMANHO_PALAVRA);
		
		lbPalavra.setText("");
		
		lbTecla1.setText(palavras.get(0));
		vbxTecla1.setOnMouseClicked(e -> processarTecla(TecladoVirtual.getCaracteres(0)));
		palavras.remove(0);
		lbTecla2.setText(palavras.get(0));
		vbxTecla2.setOnMouseClicked(e -> processarTecla(TecladoVirtual.getCaracteres(1)));
		palavras.remove(0);
		lbTecla3.setText(palavras.get(0));
		vbxTecla3.setOnMouseClicked(e -> processarTecla(TecladoVirtual.getCaracteres(2)));
		palavras.remove(0);
		lbTecla4.setText(palavras.get(0));
		vbxTecla4.setOnMouseClicked(e -> processarTecla(TecladoVirtual.getCaracteres(3)));
		palavras.remove(0);
		lbTecla5.setText(palavras.get(0));
		vbxTecla5.setOnMouseClicked(e -> processarTecla(" "));
		palavras.remove(0);
		
		lbVoltar.setText(palavras.get(0));
		pnVoltar.setOnMouseClicked(e -> processarVoltar());
		palavras.remove(0);
		lbBack.setText(palavras.get(0));
		pnBack.setOnMouseClicked(e -> processarBackspace());
		palavras.remove(0);
		
		textoAtual = new ArrayList<String>();
	}
	
	private void carregarSugestao(List<Pictograma> pictogramas) {
		hbSugestao.getChildren().clear();
		for (int i = 0; i < pictogramas.size(); i++) {
			if (i < palavras.size()) {
				Pictograma pictograma = pictogramas.get(i);
				PictogramaImagem hbPicImagem = new PictogramaImagem(pictograma);
				hbPicImagem.setPalavra(palavras.get(i));
				
				hbSugestao.getChildren().add(hbPicImagem);
				hbPicImagem.setOnMouseClicked(e -> processarPictograma(hbPicImagem));				
			}
		}
	}
	
	
	@Override
	public void processarCancelar() {
		lbPalavra.setText("");
	}
	
	private void processarTecla(String tecla) {
		if (this.coleta) {
			this.mensagemColeta.getPictogramas().add(new Pictograma(tecla, Grupo.OPCAO));
		}
		
		textoAtual.add(tecla);
		atualizarMensagemAtual();
		sugerir();
	}

	@Override
	public void processarPalavra(String comando) {
		lbPalavra.setText("");
		if (comando.equals(lbVoltar.getText())) {
			processarVoltar();
		} else if (comando.equals(lbBack.getText())) {
			processarBackspace();
		} else if (comando.equals(lbTecla1.getText())) {
			processarTecla(TecladoVirtual.getCaracteres(0));
		} else if (comando.equals(lbTecla2.getText())) {
			processarTecla(TecladoVirtual.getCaracteres(1));
		} else if (comando.equals(lbTecla3.getText())) {
			processarTecla(TecladoVirtual.getCaracteres(2));
		} else if (comando.equals(lbTecla4.getText())) {
			processarTecla(TecladoVirtual.getCaracteres(3));
		} else if (comando.equals(lbTecla5.getText())) {
			processarTecla(" ");
		}else {
			// pesquisar sugestao
			for (Node node : hbSugestao.getChildren()) {
				PictogramaImagem hb = (PictogramaImagem) node;
				if (hb.getPalavra().equals(comando)) {
					processarPictograma(hb);
					return;
				}
			}
		}		
	}
	
	private void processarPictograma(PictogramaImagem hb) {
		SimuladorView.carregarPrincipal(hb.getPictograma());
	}
	
	private void atualizarMensagemAtual() {
		hbBusca.getChildren().clear();
		for (String str : this.textoAtual) {
			PictogramaGrupo grupo = new PictogramaGrupo(Grupo.OPCAO);
			grupo.setPalavra("");
			grupo.setLegenda(str);
			hbBusca.getChildren().add(grupo);
		}
	}
	
	private void processarBackspace() {
		if (this.coleta) {
			this.mensagemColeta.getPictogramas().add(new Pictograma("BACKSPACE", Grupo.OPCAO));
		}
		
		this.core.iniciarPesquisa();
		int i = this.textoAtual.size();
		if (i > 0) {
			this.textoAtual.remove(i - 1);
		}
		atualizarMensagemAtual();
		sugerir();
	}
	
	private void processarVoltar() {
		SimuladorView.carregarPrincipal();
	}

	@Override
	public void processarCaractere(String comando) {
		lbPalavra.setText(comando);
	}

	@Override
	public List<Object> getParametro() {
		return null;
	}

	@Override
	public void setParametro(List<Object> obj) {
		this.core = (Core) obj.get(0);
		this.mensagemAtual = (Mensagem) obj.get(1);
		this.grupo = (Grupo) obj.get(2);
		this.mensagemColeta = (Mensagem) obj.get(3);
		this.coleta = (boolean) obj.get(4);
	}
	
	
	
	
}
