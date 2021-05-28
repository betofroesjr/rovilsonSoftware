package br.edu.ufu.doutorado.pca.modelo;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;

public class Arquivo {

	
	public static void escrever(String arquivo, String conteudo) {
		try {
			String pathProject = System.getProperty("user.dir");
			File file = new File(pathProject+"/"+arquivo);
			FileWriter writer = new FileWriter(file, true);
			writer.append(conteudo + System.lineSeparator());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
