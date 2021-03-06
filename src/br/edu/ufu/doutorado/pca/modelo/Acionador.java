package br.edu.ufu.doutorado.pca.modelo;

import java.util.List;

public class Acionador {

	private static final long CURTO = 400;
	private static final long LONGO = 800;
	private static final long CANCELAR = 1200;
	private static final long DWELL = 700;
	
	public static Comando classificar(long tempo) {
		if (tempo < CURTO)
			return Comando.INVOLUNTARIA;
		else if (tempo >= CURTO && tempo < LONGO)
			return Comando.CURTO;
		else if (tempo >= LONGO && tempo < CANCELAR)
			return Comando.LONGO;
		else return Comando.CANCELAR;
	}
	
	public static boolean isDwellTime(long tempo) {
		return tempo >= DWELL;
	}
	
	public static String palavra(List<Comando> comandos) {
		String comando = "";
		for (Comando c : comandos) {
			comando += c.getSigla();
		}
		return comando;
	}
	
}
