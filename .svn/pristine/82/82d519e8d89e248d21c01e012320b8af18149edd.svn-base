package br.edu.ufu.doutorado.pca.modelo;

import java.util.ArrayList;
import java.util.List;

public class Matriz {

	
	private List<Pictograma> ngram;
	private Pictograma vizinhanca;
	private Integer peso;
	
	public List<Pictograma> getNgram() {
		return ngram;
	}
	public void setNgram(List<Pictograma> ngram) {
		this.ngram = ngram;
	}
	public Pictograma getVizinhanca() {
		return vizinhanca;
	}
	public void setVizinhanca(Pictograma vizinhanca) {
		this.vizinhanca = vizinhanca;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	
	public Matriz() {
		this.ngram = new ArrayList<Pictograma>();
		this.peso = 0;
	}
	
	
	public static List<Matriz> filtrar(List<Matriz> fonte, List<Pictograma> filtro) {
		List<Matriz> retorno = new ArrayList<Matriz>();
		
		for (Matriz matriz : fonte) {
			if (filtro.equals(matriz.getNgram())) {
				retorno.add(matriz);
			}
		}
		
		return retorno;
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
		Matriz other = (Matriz) obj;
		if (ngram == null) {
			if (other.ngram != null)
				return false;
		} else if (!ngram.equals(other.ngram))
			return false;
		if (vizinhanca == null) {
			if (other.vizinhanca != null)
				return false;
		} else if (!vizinhanca.equals(other.vizinhanca))
			return false;
		return true;
	}
	@Override
	public String toString() {
		String retorno = "";
		for (Pictograma pict : this.ngram) {
			retorno += pict + "\t";
		}
		retorno += "\t\t";
		retorno += this.vizinhanca;
		retorno += "\t\t" + this.peso; 
		return retorno;
	}
	
	
	
	
}



