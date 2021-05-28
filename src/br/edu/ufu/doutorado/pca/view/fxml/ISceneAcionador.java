package br.edu.ufu.doutorado.pca.view.fxml;

import java.util.List;

public interface ISceneAcionador {

	void processarCancelar();
	void processarPalavra(String palavra) throws Exception;
	void processarCaractere(String comando);
	void abrir() throws Exception;
	List<Object> getParametro();
	void setParametro(List<Object> obj);
	
}
