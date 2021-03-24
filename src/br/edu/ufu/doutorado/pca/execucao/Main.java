package br.edu.ufu.doutorado.pca.execucao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

import br.edu.ufu.doutorado.pca.modelo.Mensagem;
import br.edu.ufu.doutorado.pca.modelo.Pictograma;

public class Main {

	public static void main(String[] args) throws Exception {
		URL fileUrl = Mensagem.class.getResource("/fxmlTeste/coletaFreq05.txt");
		File csv = new File(fileUrl.getFile());
		BufferedReader reader = new BufferedReader(new FileReader(csv));
		String linha;
		while ((linha = reader.readLine()) != null) {
			String[] acoes = linha.split("\\(");
			System.out.println(acoes.length-1);
		}

	}

}
