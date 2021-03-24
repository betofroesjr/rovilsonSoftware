package br.edu.ufu.doutorado.pca.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtil {

	public static void setValues(Object objeto, String[] fields, Object[] values) {
		try {
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					if (values[i] != null) {
						setValue(objeto, fields[i], values[i]);
					}				
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Class[] getClasses(List<Object> objects) {
		List<Class> classes = new ArrayList<Class>();
		
		if (objects != null) {
			for (Object object : objects) {
				classes.add(object.getClass());
			}
		}	
		
		if (classes.size() == 0) 
			return new Class[]{};
		else {
			Class[] retorno = new Class[classes.size()];
			for (int i = 0; i <  classes.size(); i++) {
				retorno[i] = classes.get(i);
			}
			return retorno;
		}
			
	}
	
	public static Method searchMethod(String name, Object object, Class<?>[] params) throws NoSuchMethodException {
		if (object != null && (name != null)) {
			for (Method method : object.getClass().getMethods()) {
				if (method.getName().equals(name)) {
					if (params.length == method.getParameterTypes().length) {
						boolean continua = true;
						for (int i = 0; i < params.length; i++) {
							if (! method.getParameterTypes()[i].isAssignableFrom(params[i])) {
								continua = false;
							}
						}
						if (continua)
							return method;
					}					
					System.out.println(method.getName() + " - " + Arrays.toString(method.getParameterTypes()));
				}				
			}
		}		
		throw new NoSuchMethodException();	
		
	}
	
	public static void setValue(Object objeto, String field, Object value)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		String fields[] = field.split("\\.");

		if (fields.length == 1) {
			Method metodoGet = objeto.getClass().getMethod(
					buildSetMethodName(field), new Class[] {value.getClass()});

			metodoGet.invoke(objeto, new Object[] {value});

		} else {
			String novoField = "";
			for (int i = 1; i < fields.length; i++) {
				novoField += fields[i];
				if (i != fields.length - 1) {
					novoField += ".";
				}
			}

			fields[0] = buildGetMethodName(fields[0]);
			Method metodoGet = objeto.getClass().getMethod(fields[0],
					new Class[] {});

			setValue(metodoGet
					.invoke(objeto, new Object[] {}), novoField, value);
		}
	}
	
	
	/**
	 * Obtém o valor de um atributo de um objeto a partir do nome deste atributo
	 * 
	 * @author Renato Gomide
	 * @param field
	 *            atributo a ser procurado
	 * @param objeto
	 *            objeto a ser percorrido
	 * @return valor do atributo procurado
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object getValue(String field, Object objeto)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		String fields[] = field.split("\\.");

		if (fields.length == 1) {			
			if (objeto == null)
				return null;
			
			Method metodoGet = objeto.getClass().getMethod(
					buildGetMethodName(field), new Class[] {});

			return metodoGet.invoke(objeto, new Object[] {});

		} else {
			String novoField = "";
			for (int i = 1; i < fields.length; i++) {
				novoField += fields[i];
				if (i != fields.length - 1) {
					novoField += ".";
				}
			}

			fields[0] = buildGetMethodName(fields[0]);
			Method metodoGet = objeto.getClass().getMethod(fields[0],
					new Class[] {});

			return getValue(novoField, metodoGet
					.invoke(objeto, new Object[] {}));
		}
	}

	public static String buildGetMethodName(String fieldName) {
		StringBuilder methodName = new StringBuilder();
		
		// id.dataInicio
		String[] names = fieldName.split(".");
		if (names == null || names.length == 0) {
			names = new String[1];
			names[0] = fieldName;
		}
		
		for (String currentMethod:names) {
			methodName.append("get");
			methodName.append(currentMethod.substring(0, 1).toUpperCase());
			methodName.append(currentMethod.substring(1, currentMethod.length()));
		}
		
		return methodName.toString();
	}
	
	public static String buildSetMethodName(String fieldName) {
		StringBuilder methodName = new StringBuilder("set");
		methodName.append(fieldName.substring(0, 1).toUpperCase());
		methodName.append(fieldName.substring(1, fieldName.length()));
		return methodName.toString();
	}

	public static String buildAttributeName(String methodName) {
		String replace = new String();
		if (methodName.startsWith("get")) {
			replace = "get";
		} else if (methodName.startsWith("get")) {
			replace = "set";
		}

		methodName = methodName.replace(replace, "");

		StringBuilder retorno = new StringBuilder();

		retorno.append(methodName.substring(0, 1).toLowerCase());
		retorno.append(methodName.substring(1, methodName.length()));

		return retorno.toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object executarMetodo(Object executor, Class clazz,
			String nomeMetodo, Class[] tipos, Object[] parametros) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Method metodo = clazz.getMethod(nomeMetodo, tipos);
		return metodo.invoke(executor, parametros);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object executarMetodo(Object executor, Class clazz,
			String nomeMetodo, Object[] parametros) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Class[] tipos = new Class[parametros.length];

		for (int i = 0; i < parametros.length; i++)
			tipos[i] = parametros[i].getClass();

		Method metodo = clazz.getMethod(nomeMetodo, tipos);
		return metodo.invoke(executor, parametros);
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object transferirAtributos(Object origem, Object destino)
			throws InstantiationException, IllegalAccessException,
			SecurityException, IllegalArgumentException,
			InvocationTargetException {

		if (origem != null) {
			Class classOrigem = origem.getClass();
			Class classDestino = destino.getClass();

			Object retorno = classDestino.newInstance();

			for (Method metodoDestino : classDestino.getMethods()) {
				if (metodoDestino.getName().startsWith("set")) {
					
					String nomeGetter = new String();
					
					if (metodoDestino.getParameterTypes()[0].getClass().equals(Boolean.class) ||
							metodoDestino.getParameterTypes()[0].getClass().equals(boolean.class)) {
						nomeGetter = metodoDestino.getName().replace("set",
						"is");
					} else {
						nomeGetter = metodoDestino.getName().replace("set",
						"get");
					}
					
					
					
					try {
						Method metodoOrigem = classOrigem.getMethod(nomeGetter,
								new Class[] {});

						if (metodoOrigem != null) {

							if (metodoOrigem.getReturnType().equals(
									metodoDestino.getParameterTypes()[0])) {
								if (metodoOrigem.getReturnType().equals(
										Double.class)
										&& (metodoOrigem.invoke(origem,
												new Object[] {}) == null || ((Double) metodoOrigem
												.invoke(origem, new Object[] {}))
												.isNaN())) {

									if (metodoOrigem.invoke(origem,
											new Object[] {}) == null) {
										metodoDestino.invoke(retorno,
												new Object[] { Double.NaN });
									} else {
										metodoDestino.invoke(retorno,
												new Object[] { null });
									}

								} else {
									metodoDestino.invoke(retorno, metodoOrigem
											.invoke(origem, new Object[] {}));
								}
							} else {
								// CARREGA O MÉTODO GET DE CADA UM E FAZ O
								// ADAPTER RECURSIVO
								if (!classDestino.getMethod(nomeGetter,
										new Class[] {}).getReturnType()
										.isEnum()) {									
									Object origemDerivado = metodoOrigem
											.invoke(origem, new Object[] {});
									Object destinoDerivado = classDestino
											.getMethod(nomeGetter,
													new Class[] {})
											.getReturnType().newInstance();

									if (origemDerivado != null) {
										destinoDerivado = transferirAtributos(
												origemDerivado, destinoDerivado);

										metodoDestino.invoke(retorno,
												destinoDerivado);
									}
								}
							}

						}
					} catch (NoSuchMethodException e) {

					}
				}
			}

			return retorno;
		}
		return null;
	}
	
	/**
     * Returns a copy of the object, or null if the object cannot
     * be serialized.
     */
//    public static Object copiar(Object orig) {
//        Object obj = null;
//        try {
//            // Write the object out to a byte array
//            FastByteArrayOutputStream fbos =
//                    new FastByteArrayOutputStream();
//            ObjectOutputStream out = new ObjectOutputStream(fbos);
//            out.writeObject(orig);
//            out.flush();
//            out.close();
//
//            // Retrieve an input stream from the byte array and read
//            // a copy of the object back in.
//            ObjectInputStream in =
//                new ObjectInputStream(fbos.getInputStream());
//            obj = in.readObject();
//        }
//        catch(IOException e) {
//            e.printStackTrace();
//        }
//        catch(ClassNotFoundException cnfe) {
//            cnfe.printStackTrace();
//        }
//        return obj;
//    }	
	

    @SuppressWarnings("rawtypes")
	public static Object objectToType(Object value, String type) {
    	try {
    		Class clazz = Class.forName(type);
    		
    		if (clazz.equals(Integer.class)) {
    			return Integer.parseInt(value.toString());
    		} else if (clazz.equals(Double.class)) {
    			return Double.parseDouble(value.toString());
    		}
    		
    		return value;
    	} catch (Exception e) {
    		
    	}

    	return null;
    }
    
    
    
}
