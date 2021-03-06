package br.edu.ufu.doutorado.pca.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import br.edu.ufu.doutorado.pca.execucao.Core;
import br.edu.ufu.doutorado.pca.modelo.Arquivo;
import br.edu.ufu.doutorado.pca.modelo.Mensagem;
import br.edu.ufu.doutorado.pca.modelo.Palavra;
import br.edu.ufu.doutorado.pca.modelo.Pictograma;
import br.edu.ufu.doutorado.pca.view.fxml.ISceneAcionador;
import br.edu.ufu.doutorado.pca.view.fxml.PictogramaGrupo;
import br.edu.ufu.doutorado.pca.view.fxml.PictogramaImagem;
import br.edu.ufu.doutorado.pca.modelo.Grupo;

public class Principal extends VBox implements Initializable, ISceneAcionador {

	@FXML
	public HBox hbGrupo;
	@FXML
	public Label lbPalavraAtual;
	@FXML
	public Label lbPesquisar;
	@FXML
	public Label lbBack;
	@FXML
	public HBox hbAtual;
	@FXML
	public HBox hbSugestoes;
	@FXML
	public HBox hbColeta;
	@FXML
	public Pane hbIniciarColeta;
	@FXML
	public Pane pnPesquisar;
	@FXML
	public AnchorPane pnBack;
	
	private Mensagem mensagemAtual;
	
	private List<Mensagem> historicoMensagem;
	
	private Mensagem mensagemColeta;
	
	private List<String> palavras;
	
	private Core core;
	
	private Pictograma pictogramaTemp;
	
	private Mensagem coletaAtual;
	
	private int idxColeta;
	
	private String arquivoColeta;
	
	private boolean coleta;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			core = new Core();
			core.init();
			palavras = Palavra.palavras(SimuladorView.TAMANHO_PALAVRA);
			hbGrupo.setSpacing(10);
			hbSugestoes.setSpacing(10);
			lbPalavraAtual.setText("");
			for (Grupo grupo : Grupo.values()) {
				if (! grupo.equals(Grupo.OPCAO)) {
					PictogramaGrupo hbPicGrupo = new PictogramaGrupo(grupo);
					hbPicGrupo.setPalavra(palavras.get(0));
					palavras.remove(0);
					hbGrupo.getChildren().add(hbPicGrupo);
					hbPicGrupo.setOnMouseClicked(e -> processarGrupo(hbPicGrupo));
				}
			}
			
			((PictogramaGrupo)hbGrupo.getChildren().get(0)).selecionar(true);
			
			lbPesquisar.setText(palavras.get(0));
			pnPesquisar.setOnMouseClicked(e -> processarPesquisar());
			palavras.remove(0);
			lbBack.setText(palavras.get(0));
			pnBack.setOnMouseClicked(e -> processarBackspace());
			palavras.remove(0);
			
			this.mensagemAtual = new Mensagem();
			
			int limitePictograma = palavras.size();
			
			// CARREGAR LISTA DE PICTOGRAMAS INICIAL
			List<Pictograma> sugestaoInicial = core.listaSugestao(mensagemAtual, getGrupoSelecionado(), limitePictograma);
			carregarSugestao(sugestaoInicial);
			
			hbIniciarColeta.setOnMouseClicked(e -> iniciarColeta());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void iniciarColeta() {
		try {
			arquivoColeta = System.currentTimeMillis() + ".txt";
			
			idxColeta = 0;
			this.historicoMensagem = new ArrayList<Mensagem>();
			this.mensagemAtual = new Mensagem();
			hbAtual.getChildren().clear();
			
			List<Mensagem> mensagens = Mensagem.agruparTextoColeta();
			
			if(mensagens != null && mensagens.size() > 0) {
				coletaAtual = mensagens.get(idxColeta);
				configurarColeta(coletaAtual);				
				this.coleta = true;		
				this.mensagemColeta = new Mensagem();
			}			
			
			Arquivo.escrever(arquivoColeta, this.core.getPreditorSelecionado().getClass().getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void carregarSugestao(List<Pictograma> pictogramas) {
		hbSugestoes.getChildren().clear();
		for (int i = 0; i < pictogramas.size(); i++) {
			Pictograma pictograma = pictogramas.get(i);
			PictogramaImagem hbPicImagem = new PictogramaImagem(pictograma);
			hbPicImagem.setPalavra(palavras.get(i));
			
			hbSugestoes.getChildren().add(hbPicImagem);
			hbPicImagem.setOnMouseClicked(e -> processarPictograma(hbPicImagem));
		}
	}
	
	private void concluirFraseColeta() throws Exception {
		this.historicoMensagem.add(mensagemColeta);
		
		Arquivo.escrever(arquivoColeta, Mensagem.formatoArquivo(mensagemColeta));
		
		idxColeta++;
		mensagemColeta = new Mensagem();
		mensagemAtual = new Mensagem();
		hbAtual.getChildren().clear();
		List<Mensagem> mensagens = Mensagem.agruparTextoColeta();
		if (idxColeta < mensagens.size()) {
			coletaAtual = mensagens.get(idxColeta);
			configurarColeta(coletaAtual);		
		} else {
			hbColeta.getChildren().clear();
			coleta = false;
			Text lb = new Text("FIM DA COLETA");
			lb.setFont(Font.font("System", FontWeight.BOLD, 42));
			hbColeta.getChildren().add(lb);
		}
	}
	
	private void configurarColeta(Mensagem mensagem) {
		try {
			hbColeta.getChildren().clear();
			int j = 0;
			for (int i = 0; i < mensagem.getPictogramas().size(); i++) {
				Pictograma pic = mensagem.getPictogramas().get(i);
				Text lb = new Text(pic.getRotulo());
				lb.setFont(Font.font("System", FontWeight.BOLD, 42));
				
				if (i < this.mensagemAtual.getPictogramas().size()) {
					Pictograma comparacao = this.mensagemAtual.getPictogramas().get(i);
					if (comparacao.equals(pic)) {
						j++;
						lb.setFill(Color.web("#000000"));
					} else {
						lb.setFill(Color.web("#" + pic.getGrupo().getCor()));
						lb.setStyle("-fx-stroke: black;-fx-stroke-width: 1px;");
					}
				} else {
					lb.setFill(Color.web("#" + pic.getGrupo().getCor()));
					lb.setStyle("-fx-stroke: black;-fx-stroke-width: 1px;");
				}
				
				hbColeta.getChildren().add(lb);
			}
			
			if (j == mensagem.getPictogramas().size()) {
				concluirFraseColeta();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Grupo getGrupoSelecionado() {
		for (Node node : hbGrupo.getChildren()) {
			PictogramaGrupo hb = (PictogramaGrupo) node;
			if (hb.getSelecionado()) {
				return hb.getGrupo();
			}
		}
		return null;
	}
	
	public void processarCancelar() {
		lbPalavraAtual.setText("");
	}
	
	private void removerSelecaoGrupo() {
		for (Node node : hbGrupo.getChildren()) {
			PictogramaGrupo hb = (PictogramaGrupo) node;
			hb.selecionar(false);
		}
	}
	
	private void processarGrupo(PictogramaGrupo hb) {		
		try {
			selecionarGrupo(hb.getGrupo());
			carregarSugestao(core.listaSugestao(mensagemAtual, getGrupoSelecionado(), palavras.size()));
			
			if (this.coleta) {
				this.mensagemColeta.getPictogramas().add(new Pictograma("GRUPO", hb.getGrupo()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void selecionarGrupo(Grupo grupo) {
		removerSelecaoGrupo();
		for (Node node : hbGrupo.getChildren()) {
			PictogramaGrupo hb = (PictogramaGrupo) node;
			if (hb.getGrupo().equals(grupo)) {
				hb.selecionar(true);
				return;
			}
		}
		
	}
	
	private void processarPictograma(PictogramaImagem hb) {
		try {
			this.mensagemAtual.getPictogramas().add(hb.getPictograma());
			Grupo grupo = core.sugerirGrupo(this.mensagemAtual);
			if (! grupo.equals(this.getGrupoSelecionado())) {
				selecionarGrupo(grupo);
				carregarSugestao(core.listaSugestao(mensagemAtual, getGrupoSelecionado(), palavras.size()));
			}
					
			atualizarMensagemAtual();
			
			if (this.coleta) {
				this.mensagemColeta.getPictogramas().add(hb.getPictograma());
				configurarColeta(this.coletaAtual);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void atualizarMensagemAtual() {
		hbAtual.getChildren().clear();
		for (Pictograma pic : this.mensagemAtual.getPictogramas()) {
			hbAtual.getChildren().add(new PictogramaImagem(pic));
		}
	}
	
	private void processarBackspace() {
		int i = this.mensagemAtual.getPictogramas().size();
		if (i > 0) {
			this.mensagemAtual.getPictogramas().remove(i - 1);
		}
		atualizarMensagemAtual();
		
		if (this.coleta) {
			this.mensagemColeta.getPictogramas().add(new Pictograma("BACKSPACE", Grupo.OPCAO));
			configurarColeta(this.coletaAtual);
		}
	}
	
	public void processarPalavra(String comando) throws Exception {
		if (comando.equals(lbPesquisar.getText())) {
			processarPesquisar();
		} else if (comando.equals(lbBack.getText())) {
			processarBackspace();
		} else {
			// pesquisar grupo
			for (Node node : hbGrupo.getChildren()) {
				PictogramaGrupo hb = (PictogramaGrupo) node;
				if (hb.getPalavra().equals(comando)) {
					processarGrupo(hb);
					return;
				}
			}
			// pesquisar sugestao
			for (Node node : hbSugestoes.getChildren()) {
				PictogramaImagem hb = (PictogramaImagem) node;
				if (hb.getPalavra().equals(comando)) {
					processarPictograma(hb);
					return;
				}
			}
		}
		lbPalavraAtual.setText("");
	}
	
	private void processarPesquisar() {
		if (this.coleta) {
			this.mensagemColeta.getPictogramas().add(new Pictograma("PESQUISAR", Grupo.OPCAO));
		}
		
		SimuladorView.carregarPesquisa();
	}
	
	public void processarCaractere(String comando) {
		lbPalavraAtual.setText(comando);
	}

	@Override
	public void abrir() {
		if (pictogramaTemp != null) {
			PictogramaImagem pic = new PictogramaImagem(pictogramaTemp);
			processarPictograma(pic);			
			pictogramaTemp = null;
		}
	}

	@Override
	public List<Object> getParametro() {
		List<Object> retorno = new ArrayList<Object>();
		retorno.add(this.core);
		retorno.add(this.mensagemAtual);
		retorno.add(this.getGrupoSelecionado());
		retorno.add(this.mensagemColeta);
		retorno.add(this.coleta);
		return retorno;
	}

	@Override
	public void setParametro(List<Object> obj) {
		pictogramaTemp = (Pictograma) obj.get(0);
	}
	
	
	
}
