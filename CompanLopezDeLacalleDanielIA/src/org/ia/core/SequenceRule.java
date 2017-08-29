package org.ia.core;

/**
 * Clase que guarda una secuencia de elementos de una regla junto con su posición de sincronizacion para un elemento de casilla vacia jugable elegida (#/=).
 * La posicion de sincronizcion de la secuencia de busqueda con la secuencia de pruaba nos indica con que parte de la secuencia de prueba hemos de comparar.
 * Se da por supuesto que la secuencia esta bien escrita.
 * Posibles elementos de sincronizacion (casilla vacia jugable): _ , # , =
 * Otros elementos de la secuencia: O , X , z , I , E , ?
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public class SequenceRule extends Sequence {
	
	/**
	 * Constructor de la clase.
	 * Crea una nueva secuencia de busqueda.
	 * @param seq Cadena con la secuencia de elementos.
	 */
	SequenceRule(String seq){
		super(seq,findSincPos(seq));
	}
	
	/**
	 * Busca en una sentencia de busqueda la posición de sincronizacion. Posicion del elemento de casilla vacia jugable elegida (#/=).
	 * @param seq Cadena con una secuencia de busqueda que se supone bien formada.
	 * @return Posicion (indice) del elemento de sincronizcion de la secuencia de busqueda con la secuencia de pruaba. -1 si no se ha encontrado.
	 */
	public static int findSincPos(String seq){
		int i=0;
		char c;
		if(seq==null || seq.length()==0) return -1;
		while(i<seq.length()){
			c=seq.charAt(i);
			if(c=='#' || c=='=') return i;
			i++;
		}
		return -1;
	}
	
	/** Devuelve si la secuencia es o no valida.
	 * @return <code>true</code> si la sentencia es valida.
	 */
	@Override
	public boolean isValid(){
		if(seq==null || seq.length()==0 || sincPos<0) return false;
		else return true;
	}
	
	/**
	 * Compara las cadenas de dos secuencias teniendo en cuenta los puntos de sincronizacion de cada una.
	 * @param trySeq Secuencia de prueba a comparar.
	 * @return <code>true</code> si hay coincidencia.
	 */
	public boolean match(Sequence trySeq){
		if(!this.isValid()) return false;
		if(trySeq==null || !trySeq.isValid()) return false;
		String f=seq;
		int fpos=sincPos;
		String t=trySeq.getSeq();
		int tpos=trySeq.getSincPos();
		if(f.length()>t.length()) return false;
		if(tpos<fpos) return false;
		int a=tpos-fpos;
		int b=a+f.length();
		if(b>t.length()) return false;
		t=t.substring(a,b);
		for(int i=0;i<f.length();i++){
			char c=f.charAt(i);
			char d=t.charAt(i);
			switch(c){
				case 'E':
					if(d!='E' && d!='_' && d!='z') return false;
					break;
				case '#':
					if(d!='_') return false;
					break;
				case '=':
					if(d!='_') return false;
					break;
				case '?':
					break;
				default:
					if(c!=d) return false;
			}
		}
		return true;
	}
	
}
