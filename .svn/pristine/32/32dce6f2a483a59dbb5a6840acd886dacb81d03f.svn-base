package br.edu.ufu.doutorado.pca.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



/** 
 * Comparator genérico para ordenar qualquer Entity 
 * @author afviriato 
 * 
 * @param <T> Classe a ser ordenada 
 */  
public class GenericComparator<T extends Object> implements Comparator<T> {  
     
   private SortType sortType   = null;  
   private String   methodName = null;  
     
   /** 
    * Constrói um GenericComparator de acordo com o campo e o tipo  
    * @param sortField Campo para ordenação 
    * @param sortType Tipo de ordenação, ascendente (ASC) ou descendente (DESC) 
    */  
   public GenericComparator(String sortField, SortType sortType) {  
      this.sortType   = sortType;  
      this.methodName = ReflectionUtil.buildGetMethodName(sortField);  
   }  
  
  
   @SuppressWarnings({ "rawtypes", "unchecked" })
@Override  
   public int compare(T o1, T o2) {  
      try {  
         Method method1 = o1.getClass().getMethod(this.methodName,new Class[]{});  
         Comparable comp1 = (Comparable) method1.invoke(o1, new Object[]{});  
           
         Method method2 = o1.getClass().getMethod(this.methodName, new Class[]{});  
         Comparable comp2 = (Comparable) method2.invoke(o2, new Object[]{});  
         
         
        if (comp1 == null && comp2 == null) {
        	return 0;
        } else if (comp1 == null) {
        	return 1;
        } else if (comp2 == null) {
        	return -1;
        } else {
            return comp1.compareTo(comp2) * this.sortType.getIndex();
        }    
           
      } catch (IllegalArgumentException e) {
    	return 0;  
      } catch (Exception e) {  
         e.printStackTrace();
         return 0;
         //throw new RuntimeException(e.getMessage());  
      }  
   }  
     
   /** 
    * Organiza um List<T> de acordo com o campo e o tipo (ASC ou DESC) 
    *  
    * @param <T> Classe dos objetos que serão ordenados 
    * @param list List<T> a ser ordenada 
    * @param sortField Campo para ordenação 
    * @param sortType Tipo da ordenação (ASC ou DESC) 
    */  
   public static <T extends Object> void sortList(List<T> list, String sortField,   
         SortType sortType) {  
		if (list != null) {
			GenericComparator<T> comparator = new GenericComparator<T>(
					sortField, sortType);
			Collections.sort(list, comparator);
		}
        
   }  

   
   
     
   /** 
    * Organiza um T[] de acorco com o campo e o tipo (ASC ou DESC) 
    *  
    * @param <T> Classe dos objetos que serão ordenados 
    * @param array  T[] a ser ordenado 
    * @param sortField Campo para ordenação 
    * @param sortType Tipo da ordenação (ASC ou DESC) 
    */  
   public static <T extends Object> void sortArray(T[] array, String sortField,   
         SortType sortType) {  
      GenericComparator<T> comparator = new GenericComparator<T>(sortField,   
            sortType);  
      Arrays.sort(array, comparator);  
   }  
}  