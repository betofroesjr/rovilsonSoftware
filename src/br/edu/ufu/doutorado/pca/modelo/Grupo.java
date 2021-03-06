package br.edu.ufu.doutorado.pca.modelo;

public enum Grupo {
	PESSOA("Pessoas", "FDFFBD"),
	VERBO("Verbos", "BDFFC4"),
	SUBSTANTIVO("Substantivos", "FFD8AD"),
	QUALIFICADOR("Qualificadores", "A1DFF5"),
	ELEMENTO_SOCIAL("Elementos Sociais", "FFD9FC"),
	LETRA_NUMERO("Letras/Números", "FFFFFF"),
	OPCAO("", "EEEEEE");
	
	private String cor;
	
	private String legenda;
	
	private Grupo(String legenda, String cor) {
		this.cor = cor;
		this.legenda = legenda;
	}

	public String getCor() {
		return cor;
	}

	public String getLegenda() {
		return legenda;
	}
}
