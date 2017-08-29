package org.ia.core;

/**
 * Clase que representa un jugador humano que mueve con el teclado.
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public class PlayerHuman extends Player {
	
	/** Nivel maximo del jugador. */
	public static final int MAX_LEVEL=1;
	
	/** Referencia a la consola que esta gestionando la entrada por teclado. */
	private Console console;
	
	/**
	 * Constructor de la clase.
	 * Crea un nuevo jugador humano.
	 * ¡Atencion! Despues de crearlo hay que asignarle obligatoriamente la consola que se esta usando para leer del teclado.
	 * @param bcBoard Tablero simple (base de conocimiento).
	 * @param piece Ficha del jugador ('O' o 'X').
	 */
	public PlayerHuman(BC bcBoard, char piece){
		this.name="Humano";
		setType(HUMAN_PLAYER);
		setPiece(piece);
		setLevel(MAX_LEVEL);
		isReady=false;
		this.board=bcBoard;
		this.console=null;
	}
	
	/**
	 * Establece si el jugador esta listo para jugar.
	 */
	@Override
	public boolean setIsReady(){
		if(console!=null && super.setIsReady()) isReady=true;
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
	public void setConsole(Console console){
		this.console=console;
		setIsReady();
	}
	
	/**
	 * Elige una columna donde mover.
	 */
	public int move(){
		if(!isReady) return -1;
		BC bc=(BC) board;
		int column;
		do{
			Log.msg("¿Columna? (1~7) ",Log.CONSOLE);
			column=console.scanDigit();
			if(column>=1 && column<=7){
				if(bc.isFull(column)) Log.msg("Columna llena.",Log.CONSOLE);
				else break; 
			}
			if(column==-1) return -1;
		}while(true);
		return column;
	}
		
}
