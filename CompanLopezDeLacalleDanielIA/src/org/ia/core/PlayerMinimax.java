package org.ia.core;

import java.util.ArrayList;

/**
 * Clase que representa un jugador que mueve basandose en el metodo Minimax.
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public class PlayerMinimax extends Player {
	
	/** Nivel maximo del jugador. */
	public static final int MAX_LEVEL=3;
	
	/** Nombre del archivo de heuristica. **/
	private String heuristicFileName;
	/** Directorio del archivo de heuristica. **/
	private String heuristicFilePath;
	/** Lista de reglas para evaluar los nodos hoja. */
	private static ArrayList<Rule> rules;
	
	/**
	 * Constructor de la clase.
	 * Crea un nuevo jugador Minimax.
	 * @param bcaBoard Tablero avanzado (Base de Conocimiento Aumentada).
	 * @param piece Ficha del jugador ('O' o 'X').
	 */
	public PlayerMinimax(BCA bcaBoard, char piece){
		this.name="Minimax";
		setType(MINMAX_PLAYER);
		setPiece(piece);
		setLevel(MAX_LEVEL);
		this.board=bcaBoard;
		heuristicFileName=null;
		heuristicFilePath=null;
		setIsReady();
	}
	
	/**
	 * Comprueba si el jugador dispone de varios niveles de juego.
	 * @return <code>true</code> si el jugador dispone de varios niveles de juego. <code>false</code> si no.
	 */
	public boolean hasLevels(){
		return true;
	}
	
	/**
	 * Devuelve el nivel maximo del jugador.
	 * @return Nivel maximo del jugador.
	 */
	public int getMaxLevel(){
		return MAX_LEVEL;
	}
	
	/**
	 * Establece el archivo de texto del que se tomaran las reglas heuristicas.
	 * @param path Ruta del archivo.
	 * @param fileName Nombre del archivo.
	 */
	public void choseRulesFile(String path, String fileName){
		this.heuristicFileName=fileName;
		this.heuristicFilePath=path;
	}
	
	/**
	 * Elige una columna donde mover.
	 */
	public int move(){
		if(!isReady) return -1;
		try{
			Thread.sleep(this.getWaitTime());
		}catch (InterruptedException e){
			Log.msg("¡¡EXCEPCION!! Fallo al intentar demorar el moviento. "+e.getMessage(),Log.EXCEPTION);
		}
		BCA bca=(BCA) board;
		Minimax minimax=new Minimax(bca,getPiece());
		minimax.setDepth(getLevel());
		if(rules==null){
			if(heuristicFileName==null) rules=minimax.loadHeuristic();
			else rules=minimax.loadHeuristic(heuristicFileName,heuristicFilePath);
		}
		else minimax.setHeuristic(rules);
		//minimax.expand();
		minimax.alphaBeta();
		return minimax.propagateValues();
	}
	
}
