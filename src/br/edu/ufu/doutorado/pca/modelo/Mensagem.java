package br.edu.ufu.doutorado.pca.modelo;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Mensagem {
	
	public static final int DEFAULT_BUFFER_SIZE = 8192;
	
	private List<Pictograma> pictogramas;

	public List<Pictograma> getPictogramas() {
		return pictogramas;
	}

	public void setPictogramas(List<Pictograma> pictogramas) {
		this.pictogramas = pictogramas;
	}
	
	public String toString() {
		String retorno = "";
		for (Pictograma pictograma : this.pictogramas) {
			retorno += pictograma + " ";
		}
		return retorno;
	}
	
	public Mensagem() {
		this.pictogramas = new ArrayList<Pictograma>();
	}
	
	public static List<Mensagem> agruparTextoColeta() {
		return agruparMensagens("textoColeta.txt");
	}
	
	public static Mensagem agruparTextoEntrada(String arquivo) {
		Mensagem mensagem = new Mensagem();
		List<Pictograma> dicionario = Pictograma.getDicionario();
		
		try {
//			URL fileUrl = Mensagem.class.getResource("/"+arquivo);
//			File csv = new File(arquivo);
//			BufferedReader reader = new BufferedReader(new FileReader(csv));

			BufferedReader reader = obterBufferReader(arquivo);
			
			String linha;
			while ((linha = reader.readLine()) != null) {
				linha = linha.replace(",", " ").trim();
				if (! linha.equals("")) {
					String[] frases = linha.split(Pattern.quote("."));
					
					for (String frase : frases) {
						String[] palavras = frase.split(" ");
						for (String palavra : palavras) {
							palavra = palavra.trim();
							Pictograma pictograma = Pictograma.buscar(palavra, dicionario);
							if (pictograma != null) {
								mensagem.getPictogramas().add(pictograma);								
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mensagem;
	}
	
	public static List<Mensagem> agruparMensagens(String arquivoTexto) {
		List<Mensagem> mensagens = new ArrayList<Mensagem>();
		List<Pictograma> dicionario = Pictograma.getDicionario();
		
		try {
			Mensagem mensagem = new Mensagem();

			BufferedReader reader = obterBufferReader(arquivoTexto);
			
			String linha;
			while ((linha = reader.readLine()) != null) {
				linha = linha.replace(",", " ").trim();
				if (! linha.equals("")) {
					String[] frases = linha.split(Pattern.quote("."));
					
					for (String frase : frases) {
						mensagem = new Mensagem();
						String[] palavras = frase.split(" ");
						for (String palavra : palavras) {
							palavra = palavra.trim();
							Pictograma pictograma = Pictograma.buscar(palavra, dicionario);
							if (pictograma != null) {
								mensagem.getPictogramas().add(pictograma);								
							}
						}
						if (mensagem.getPictogramas().size() > 0)
							mensagens.add(mensagem);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mensagens;
	}

	public static BufferedReader obterBufferReader(String arquivoTexto) {

		InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(arquivoTexto);
		
		return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	}
	
	public static InputStream obterInputStream(String caminho) {

		return ClassLoader.getSystemClassLoader().getResourceAsStream(caminho);
	}

	public static String formatoArquivo(Mensagem mensagem) {
		String retorno = "";
		for (int i = 0; i < mensagem.getPictogramas().size(); i++) {
			Pictograma pic = mensagem.getPictogramas().get(i);
			retorno += pic.toString();
			if (i < mensagem.getPictogramas().size() - 1) {
				retorno += " | ";
			}
		}
		return retorno;
	}
}
