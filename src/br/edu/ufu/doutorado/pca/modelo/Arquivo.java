package br.edu.ufu.doutorado.pca.modelo;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;

public class Arquivo {

	
	public static void escrever(String arquivo, String conteudo) {
		try {
			URL fileUrl = Mensagem.class.getResource("/"+arquivo);
			File file = new File(fileUrl.getFile());
			FileWriter writer = new FileWriter(file, true);
			writer.append(conteudo + System.lineSeparator());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
