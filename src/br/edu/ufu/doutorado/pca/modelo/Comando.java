package br.edu.ufu.doutorado.pca.modelo;

public enum Comando {

	CANCELAR("/fxml/sound/cancelar.wav", ""),
	INVOLUNTARIA("/fxml/sound/cancelar.wav", ""),
	DWELL("/fxml/sound/acerto.wav", ""),
	CURTO("/fxml/sound/curto.wav", "p"),
	LONGO("/fxml/sound/longo.wav", "L");
	
	private String caminho;
	private String sigla;
	
	private Comando(String caminho, String sigla) {
		this.caminho = caminho;
		this.sigla = sigla;
	}
	
	public String getSigla() {
		return sigla;
	}

	public String getCaminho() {
		return caminho;
	}
	
	
	
}
