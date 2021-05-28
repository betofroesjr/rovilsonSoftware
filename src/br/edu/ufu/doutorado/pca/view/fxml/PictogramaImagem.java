package br.edu.ufu.doutorado.pca.view.fxml;

import java.io.File;
import java.io.IOException;

import br.edu.ufu.doutorado.pca.modelo.Mensagem;
import br.edu.ufu.doutorado.pca.modelo.Pictograma;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PictogramaImagem extends VBox {

	public static final String DIR = "pictogramas/";

	@FXML
	protected VBox vbGrupoExterno;
	@FXML
	protected VBox vbGrupoInterno;

	@FXML
	protected Label lbLegenda;

	@FXML
	protected Label lbPalavra;

	@FXML
	protected ImageView imPictograma;

	private Pictograma pictograma;

	public PictogramaImagem(Pictograma pictograma) {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PictogramaImagem.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();

			this.pictograma = pictograma;
			this.lbPalavra.setText("");
			atualizarLayout();

		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

	}

	public void setPalavra(String palavra) {
		this.lbPalavra.setText(palavra);
	}

	public String getPalavra() {
		return this.lbPalavra.getText();
	}

	private void atualizarLayout() {
		String style = "";

		style += String.format("-fx-background-color: #%s;", this.pictograma.getGrupo().getCor());
		style += "-fx-border-style: solid;";

		vbGrupoExterno.setStyle(style);
		vbGrupoInterno.setStyle("-fx-border-style: solid;-fx-background-color: white;");

		if (pictograma != null) {
			lbLegenda.setText(pictograma.getRotulo().toUpperCase());

			String caminho = DIR + pictograma.getRotulo() + ".png";
			try {
				
				imPictograma.setImage(new Image(Mensagem.obterInputStream(caminho)));
			} catch (Exception e) {
				System.out.println("Imagem não encontrada: " + caminho);
			}

		}
	}

	public Pictograma getPictograma() {
		return pictograma;
	}

}
