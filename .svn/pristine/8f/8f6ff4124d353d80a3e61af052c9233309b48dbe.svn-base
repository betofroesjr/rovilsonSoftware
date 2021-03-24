package br.edu.ufu.doutorado.pca.modelo;

import java.util.ArrayList;
import java.util.List;

public class MatrizGrupo {

	private List<Grupo> ngram;
	private Grupo vizinhanca;
	private Integer peso;
	
	public List<Grupo> getNgram() {
		return ngram;
	}
	public void setNgram(List<Grupo> ngram) {
		this.ngram = ngram;
	}
	public Grupo getVizinhanca() {
		return vizinhanca;
	}
	public void setVizinhanca(Grupo vizinhanca) {
		this.vizinhanca = vizinhanca;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	
	public MatrizGrupo() {
		this.ngram = new ArrayList<Grupo>();
		this.peso = 0;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ngram == null) ? 0 : ngram.hashCode());
		result = prime * result
				+ ((vizinhanca == null) ? 0 : vizinhanca.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatrizGrupo other = (MatrizGrupo) obj;
		if (ngram == null) {
			if (other.ngram != null)
				return false;
		} else if (!ngram.equals(other.ngram))
			return false;
		if (vizinhanca != other.vizinhanca)
			return false;
		return true;
	}
	@Override
	public String toString() {
		String retorno = "";
		for (Grupo grupo : this.ngram) {
			retorno += grupo + "\t";
		}
		retorno += "\t\t";
		retorno += this.vizinhanca;
		retorno += "\t\t" + this.peso; 
		return retorno;
	}
	
	public static List<MatrizGrupo> filtrar(List<MatrizGrupo> fonte, List<Grupo> filtro) {
		List<MatrizGrupo> retorno = new ArrayList<MatrizGrupo>();
		
		for (MatrizGrupo matriz : fonte) {
			if (filtro.equals(matriz.getNgram())) {
				retorno.add(matriz);
			}
		}
		
		return retorno;
	}
	
	
}
