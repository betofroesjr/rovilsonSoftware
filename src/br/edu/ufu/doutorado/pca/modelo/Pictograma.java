package br.edu.ufu.doutorado.pca.modelo;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Pictograma {
	
	public static String DIR = "fxmlTeste/";

	private static final String ARQUIVO = DIR+"pictogramaNovo.csv";
	
	private static Set<Pictograma> dicionario;
	
	private Grupo grupo;
	private String rotulo;
	private Integer peso;
	
	public Pictograma(String rotulo, Grupo grupo) {
		this.peso = 0;
		this.rotulo = rotulo;
		this.grupo = grupo;
	}
	
	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	public String getRotulo() {
		return rotulo;
	}
	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", this.rotulo, this.grupo);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((grupo == null) ? 0 : grupo.hashCode());
		result = prime * result + ((rotulo == null) ? 0 : rotulo.hashCode());
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
		Pictograma other = (Pictograma) obj;
		if (grupo != other.grupo)
			return false;
		if (rotulo == null) {
			if (other.rotulo != null)
				return false;
		} else if (!rotulo.equalsIgnoreCase(other.rotulo))
			return false;
		return true;
	}
	

	public static List<Pictograma> filtrar(List<Pictograma> pictogramas, Grupo grupo) {
		List<Pictograma> retorno = new ArrayList<Pictograma>();
		
		for (Pictograma pictograma : pictogramas) {
			if (pictograma.getGrupo().equals(grupo)) {
				retorno.add(pictograma);
			}
		}
		
		return retorno;		
	}
	
	public static Pictograma buscar(String rotulo, List<Pictograma> lista) {
		for (Pictograma pictograma : lista) {
			if (pictograma.getRotulo().equalsIgnoreCase(rotulo))
				return pictograma;
		}
		return null;
	}
	
	public static void limparDicionario() {
		dicionario = new HashSet<Pictograma>();
	}
	
	public static List<Pictograma> getDicionario() throws Exception {
		if (dicionario == null) {
			dicionario = new HashSet<Pictograma>();
		}
		
		// TODO: CARREGAR O DICIONÁRIO COMPLETO
		List<Pictograma> lista = new ArrayList<Pictograma>();
		try {			
			if (dicionario.size() == 0) {
				//File csv = new File(DIR + "\\" + ARQUIVO);
//				URL fileUrl = Pictograma.class.getResource(ARQUIVO);
//				File csv = new File(fileUrl.getFile());
//				BufferedReader reader = new BufferedReader(new FileReader(csv));
				
				BufferedReader reader = Mensagem.obterBufferReader(ARQUIVO);
				
				
				String linha;
				while ((linha = reader.readLine()) != null) {
					String[] atributos = linha.split(";");
					try {
						Pictograma pictograma = new Pictograma(atributos[0], Grupo.valueOf(atributos[1]));
						dicionario.add(pictograma); 
					} catch (Exception e) {
						//System.out.println(atributos[1]);
					}
					
				}
			}
		} catch (Exception e) {
			throw e;
		}
		lista.addAll(dicionario);
		return lista;
	}
	
	public static List<Pictograma> sortear(List<Pictograma> lista, Integer limite) {
		List<Pictograma> retorno = new ArrayList<Pictograma>();
		List<Double> pesos = new ArrayList<Double>();
		double total = 0;
		
		for (Pictograma pi : lista) {
			total += pi.getPeso();
		}
		
		int ant = -1;
		for (Pictograma pi : lista) {
			if (ant == - 1) {
				pesos.add(pi.getPeso() / total);
			} else {
				pesos.add(pesos.get(ant) + (pi.getPeso() / total));
			}
			ant++;
		}
		
		Random rand = new Random();
		for (int i = 0; i < limite; i++) {
			Double sorteio = rand.nextDouble();
			
			for (int j = 0; j < pesos.size(); j++) {
				if (todosPesosNaN(pesos)) {
					int idx = rand.nextInt(pesos.size());
					retorno.add(lista.get(idx));
					total -= lista.get(idx).getPeso();
					lista.remove(idx);
					
					pesos.clear();
					ant = -1;
					for (Pictograma pi : lista) {
						if (ant == - 1) {
							pesos.add(pi.getPeso() / total);
						} else {
							pesos.add(pesos.get(ant) + (pi.getPeso() / total));
						}
						ant++;
					}
				} else {
					if (pesos.get(j) >= sorteio) {
						retorno.add(lista.get(j));
						total -= lista.get(j).getPeso();
						lista.remove(j);
						
						pesos.clear();
						ant = -1;
						for (Pictograma pi : lista) {
							if (ant == - 1) {
								pesos.add(pi.getPeso() / total);
							} else {
								pesos.add(pesos.get(ant) + (pi.getPeso() / total));
							}
							ant++;
						}
						
						break;
					}
				}
			}
		}		
		return retorno;
	}
	
	private static Boolean todosPesosNaN(List<Double> pesos) {
		for (Double peso : pesos) {
			if (!peso.isNaN())
				return false;
		}
		return true;
	}
	
	
	public static List<Pictograma> parse(List<Matriz> matrizes) {
		if (matrizes == null)
			return null;
		
		List<Pictograma> retorno = new ArrayList<>();
		for (Matriz matriz : matrizes) {
			Pictograma pictograma = matriz.getVizinhanca();
			pictograma.setPeso(matriz.getPeso());
			retorno.add(pictograma);
		}
		return retorno;
	}
	
	
	public static List<Pictograma> removerElementos(List<Pictograma> origem, List<Pictograma> filtro) {
		List<Pictograma> destino = new ArrayList<Pictograma>();
		
		for (Pictograma pic : origem) {
			if (! filtro.contains(pic)) {
				destino.add(pic);
			}
		}
		
		return destino;
	}
	

}









