package org.ia.core;

/**
 * Clase que guarda una secuencia de elementos junto con su posición de sincronizacion para un elemento de casilla vacia jugable (_/#/=).
 * La posicion de sincronizcion de la secuencia de busqueda con la secuencia de pruaba nos indica con que parte de la secuencia de prueba hemos de comparar.
 * Se da por supuesto que la secuencia esta bien escrita.
 * Posibles elementos de sincronizacion (casilla vacia jugable): _ , # , =
 * Otros elementos de la secuencia: O , X , z , I , E , ?
 * 
 * @author Daniel Compan Lopez de Lacalle
 */
public class Sequence {
	
	/** Cadena con la secuencia de elementos. */
	protected String seq;
	/** Posicion de sincronizacion en la secuencia para el elemento de casilla vacia jugable ( _ , # , = ). */
	protected int sincPos;
	
	/**
	 * Constructor de la clase.
	 * Crea una nueva Secuencia.
	 * @param seq Cadena con la secuencia de elementos.
	 * @param spos Posicion de sincronizacion.
	 */
	Sequence(String seq, int spos){
		this.seq=seq;
		this.sincPos=spos;
	}
	
	/**
	 * Devuelve la cadena con la secuencia de elementos introducida.
	 * @return Cadena de la secuencia.
	 */
	public String getSeq(){
		return this.seq;
	}
	
	/**
	 * Devuelve la longitud de la secuencia.
	 * @return Entero con la longitd de la secuencia. -1 si es <code>null</code>.
	 */
	public int getLength(){
		if(seq==null) return -1;
		return seq.length(); 
	}
	
	/**
	 * Devuelve la posicion de sincronizacion.
	 * @return Poscion de sincronizacion.
	 */
	public int getSincPos(){
		return this.sincPos;
	}
	
	/** Devuelve si la secuencia es o no valida.
	 * @return <code>true</code> si la sentencia es valida.
	 */
	public boolean isValid(){
		if(seq==null || seq.length()==0 || sincPos<0  || sincPos>=seq.length()) return false;
		char c=seq.charAt(sincPos);
		if(c!='_' && c!='#' && c!='=') return false;
		return true;
	}
	
	/**
	 * Cambia las fichas de una secuencia, 'O' por 'X' y viveversa.
	 * @param seq Cadena con una secuencia de elementos.
	 * @return Cadena con las fichas cambiadas.
	 */
	public static String changePieces(String seq){
		if(seq==null) return null;
		String newSeq="";
		for(int i=0;i<seq.length();i++){
			char c=seq.charAt(i);
			if(c=='O'){ newSeq=newSeq+"X"; }
			else if(c=='X'){ newSeq=newSeq+"O"; }
			else{ newSeq=newSeq+c; }
		}
		return newSeq;
	}
	
	/**
	 *  Cambia las fichas de la secuencia, 'O' por 'X' y viveversa.
	 */
	public void changePieces(){
		seq=changePieces(seq);
	}
	
	/**
	 * Devuele una cadena con la secuencia, poniendo entre parentesis el elemento que esta en la posicion de sincronizacion.
	 */
	@Override
	public String toString(){
		if(seq==null) return null;
		if(sincPos<0 || sincPos>=seq.length()) return seq;
		char c;
		String str="";
		for(int i=0;i<seq.length();i++){
			c=seq.charAt(i);
			if(i==sincPos) str=str+"("+c+")";
			else str=str+c;
		}
		return str;
	}
	
}
