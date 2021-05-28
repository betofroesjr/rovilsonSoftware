package br.edu.ufu.doutorado.pca.view.fxml;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import br.edu.ufu.doutorado.pca.modelo.Grupo;

public class PictogramaGrupo extends VBox {

	@FXML
	public Label lbLegenda;
	@FXML
	public Label lbPalavra;	
	@FXML
	public VBox vbGrupoExterno;
	@FXML
	public VBox vbGrupoInterno;
	
	private Grupo grupo;
	
	private Boolean selecionado = false;
	
	public PictogramaGrupo(Grupo grupo) {
		
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PictogramaGrupo.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();
            
            this.grupo = grupo;
            lbLegenda.setText(this.grupo.getLegenda());
            
            atualizarLayout();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }
	
	public void setLegenda(String legenda) {
		lbLegenda.setText(legenda);
	}
	
	public String getLegenda() {
		return lbLegenda.getText();
	}
	
	public void setPalavra(String palavra) {
		this.lbPalavra.setText(palavra);
	}
	
	public String getPalavra() {
		return this.lbPalavra.getText();
	}
	
	private void atualizarLayout() {
		String style = "";
		
		style += String.format("-fx-background-color: #%s;", this.getGrupo().getCor());
		style += "-fx-border-style: solid;";
		if (selecionado)
			style += "-fx-border-color: red;-fx-border-width: 3px;";
		 
		vbGrupoExterno.setStyle(style);
		vbGrupoInterno.setStyle("-fx-border-style: solid;-fx-background-color: white;");
	}

	
	public void selecionar(Boolean selecionado) {
		this.selecionado = selecionado;
		atualizarLayout();
	}
	
	

	public Boolean getSelecionado() {
		return selecionado;
	}

	public Grupo getGrupo() {
		return grupo;
	}


	
	
	
}
