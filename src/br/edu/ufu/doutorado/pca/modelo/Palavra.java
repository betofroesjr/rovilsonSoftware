package br.edu.ufu.doutorado.pca.modelo;

import java.util.ArrayList;
import java.util.List;

public class Palavra {

	
	public static List<String> palavras(int n) {
		List<String> retorno = new ArrayList<String>();
		
		
		int i = 0;
		retorno.add("p");
		retorno.add("L");
		while (i < n - 1) {
			
			
			List<String> temp = new ArrayList<String>();
			for (String ret : retorno) {
				temp.add("p" + ret);
				temp.add("L" + ret);
			}
			
			for (String t : temp) {
				if (! retorno.contains(t)) {
					retorno.add(t);
				}
			}
			
			i++;
		}
		
		
		
		return retorno;
	}
	
	public static void main(String args[]) {
		System.out.println(palavras(3));
	}
	
}
