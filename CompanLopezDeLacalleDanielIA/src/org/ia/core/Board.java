package org.ia.core;

/**
 * Clase abstracta que define los metodos comunes de los que ha de disponer cualquier tablero.
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public abstract class Board {
	
	/** Posible valor para el campo <CODE>type</CODE>. Tipo de tablero no establecido. */
	public static final int UNDEFINED_BOARD=0;
	/** Posible valor para el campo <CODE>type</CODE>. Tablero simple (Base de Conocimiento). */
	public static final int BC_BOARD=1;
	/** Posible valor para el campo <CODE>type</CODE>. Tablero avanzado (Base de Conocimiento Aumentada). */
	public static final int BCA_BOARD=2;
	
	/** Tipo de tablero */
	private int type=UNDEFINED_BOARD;
	
	/**
	 * Establece el tipo de tablero.
	 * @param type Entero con el tipo de tablero: 1=BC , 2=BCA .
	 */
	protected void setType(int type){
		if(type>=1 && type<=2) this.type=type;
	}
	
	/**
	 * Devuelve el tipo de tablero.
	 * @return Entero con el tipo de tablero: 1=BC , 2=BCA .
	 */
	public int getType(){
		return this.type;
	}
	
	/**
	 * Inicializa el tablero dejandolo vacio (sin fichas). 
	 */
	public abstract void clear();
	
	/**
	 * Coloca una nueva ficha.
	 * @param piece 'O' o 'X'.
	 * @param column Numero de columna (1~7).
	 * @return <code>true</code> si se ha podido colocar la nueva ficha.
	 */
	public abstract boolean put(char piece, int column);
	
	/**
	 * Comprueba si una columna esta llena de fichas.
	 * @param column Numero de columna que se quiere comprobar (1~7).
	 * @return <code>true</code> si esta llena.
	 */
	public abstract boolean isFull(int column);
	
	/**
	 * Comprueba si el tablero esta lleno de fichas.
	 * @return <code>true</code> si esta lleno.
	 */
	public abstract boolean isFull();
	
	/**
	 * Comprueva si la ultima ficha que se ha colocado en una columna forma 4 en raya.
	 * @param column Numero de columna en la que se acaba de colocar la ultima ficha (1~7).
	 * @return <code>true</code> si hay 4 iguales seguidas.
	 */
	public abstract boolean areFourConsecutive(int column);
	
	/**
	 * Mostrar los cambios.
	 */
	public abstract void show();
	
}
