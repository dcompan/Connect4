package org.ia.gui;

import org.ia.core.BC;
import org.ia.core.Player;

/**
 * Clase que representa un jugador humano que mueve lo que ha sido guardado.
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public class PlayerEcho extends Player {
	
	/** Nivel maximo del jugador. */
	public static final int MAX_LEVEL=1;
	
	/** Columna en la que ha de mover en la proxima tirada. */
	private int column;
	
	/**
	 * Constructor de la clase.
	 * Crea un nuevo jugador humano.
	 * ¡Atencion! Despues de crearlo hay que asignarle obligatoriamente la consola que se esta usando para leer del teclado.
	 * @param bcBoard Tablero simple (base de conocimiento).
	 * @param piece Ficha del jugador ('O' o 'X').
	 */
	public PlayerEcho(BC bcBoard, char piece){
		this.name="Humano";
		setType(HUMAN_PLAYER);
		setPiece(piece);
		setLevel(MAX_LEVEL);
		isReady=false;
		this.board=bcBoard;
		column=-1;
	}
	
	/**
	 * Establece si el jugador esta listo para jugar.
	 */
	@Override
	public boolean setIsReady(){
		if(super.setIsReady() && checkColumn()) isReady=true;
		else isReady=false;
		return isReady;
	}
	
	/**
	 * Comprueba si el jugador dispone de varios niveles de juego.
	 * @return <code>true</code> si el jugador dispone de varios niveles de juego. <code>false</code> si no.
	 */
	public boolean hasLevels(){
		return false;
	}
	
	/**
	 * Devuelve el nivel maximo del jugador.
	 * @return Nivel maximo del jugador.
	 */
	public int getMaxLevel(){
		return MAX_LEVEL;
	}
	
	/**
	 * Establece la consola de lectura por teclado.
	 * @param console Referencia a la consola que maneja la entrada por teclado.
	 */
	public void setColumn(int col){
		this.column=col;
		setIsReady();
	}
	
	/**
	 * Comprueba si el valor de la columna esta establecido correctamente.
	 * @return <code>true</code> si esta bien establecido.
	 */
	public boolean checkColumn(){
		if(column>0 && column<=BC.ROWS+1) return true;
		return false;
	}
	
	/**
	 * Elige una columna donde mover.
	 */
	public int move(){
		if(!isReady) return -1;
		BC bc=(BC) board;
		if(bc.isFull(column)) return -1;
		int col=column;
		column=-1;
		return col;
	}
	
}

