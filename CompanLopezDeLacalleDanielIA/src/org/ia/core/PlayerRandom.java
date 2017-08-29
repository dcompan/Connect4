package org.ia.core;

import java.util.Random;

/**
 * Clase que representa un jugador "Dummy" que mueve de forma aleatoria.
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public class PlayerRandom extends Player {
	
	/** Nivel maximo del jugador. */
	public static final int MAX_LEVEL=1;
	
	/** Objeto de la clase Random para obtener numeros aleatorios donde mover. */
	private Random random;
	
	/**
	 * Constructor de la clase.
	 * Crea un nuevo jugador random.
	 * @param bcBoard Tablero simple (base de conocimiento).
	 * @param piece Ficha del jugador ('O' o 'X').
	 */
	public PlayerRandom(BC bcBoard, char piece){
		name="DummyTester";
		setType(RANDOM_PLAYER);
		setPiece(piece);
		setLevel(MAX_LEVEL);
		this.board=bcBoard;
		random=new Random();
		setIsReady();
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
	 * Elige una columna donde mover de forma aleatoria.
	 */
	public int move(){
		if(!isReady) return -1;
		try{
			Thread.sleep(this.getWaitTime());
		}catch (InterruptedException e){
			Log.msg("¡¡EXCEPCION!! Fallo al intentar demorar el moviento. "+e.getMessage(),Log.EXCEPTION);
		}
		BC bc=(BC) board;
		boolean[] trys=new boolean[BC.COLS];
		for(int i=0;i<BC.COLS;i++) trys[i]=false;
		int fails=0;
		while(fails<BC.COLS){
			int c;
			do{
				c=random.nextInt(BC.COLS);
			}while(trys[c]);
			if(!bc.isFull(c+1)) return c+1;
			else trys[c]=true;
			fails=fails+1;
		}
		return -1;
	}
	
}
