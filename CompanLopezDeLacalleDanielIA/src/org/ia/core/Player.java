package org.ia.core;

/**
 * Clase abstracta que define los metodos comunes de los que ha de disponer cualquier jugador.
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public abstract class Player {
	
	/** Posible valor para el campo <CODE>type</CODE>. Tipo de jugador no establecido. */
	public static final int UNDEFINED_PLAYER=0;
	/** Posible valor para el campo <CODE>type</CODE>. Jugador humano. */
	public static final int HUMAN_PLAYER=1;
	/** Posible valor para el campo <CODE>type</CODE>. Jugador aleatorio. */
	public static final int RANDOM_PLAYER=2;
	/** Posible valor para el campo <CODE>type</CODE>. Jugador con reglas. */
	public static final int RULES_PLAYER=3;
	/** Posible valor para el campo <CODE>type</CODE>. Jugador con min-max. */
	public static final int MINMAX_PLAYER=4;
	public static final String[] PLAYERS={"UNDEFINED","Human","Random","Rules","Minimax"};
	
	/** Tiempo de espera por defecto para jugadores no humanos */
	public static final long DEF_WAIT_TIME=1250;
	
	/** Nombre del jugador */
	protected String name=null;
	/** Tipo de jugador */
	private int type=UNDEFINED_PLAYER;
	/** Piezas con las que juega el jugador */
	private char piece='\0';
	/** Nivel actual de un jugador no humano. El minimo es 1 y cuanto mas alto sea mayor es la dificultad. */
	private int level=1;
	/** Tiempo de espera para un jugador no humano (ms). */
	private long waitTime=DEF_WAIT_TIME;
	/** Tablero en el que juega */
	protected Board board=null;
	/** ¿Esta el jugador listo para mover? */
	protected boolean isReady=false;
	
	/**
	 * Establece el nombre del jugador.
	 * @param name Nombre del jugador.
	 */
	public void setName(String name){
		this.name=name;
	}
	
	/**
	 * Establece el tipo de jugador.
	 * @param type Tipo de jugador (1~5).
	 * @return <code>true</code> si ha podido establecer el tipo de jugador indicado. <code>false</code> si no.
	 */
	protected boolean setType(int type){
		if(type>=1 && type<=5){
			this.type=type;
			return true;
		}
		else return false;
	}
	
	/**
	 * Establece la ficha con la que juega el jugador.
	 * @param piece Ficha del jugador ('O' o 'X').
	 * @return <code>true</code> si ha podido establecer la ficha indicada. <code>false</code> si no.
	 */
	protected boolean setPiece(char piece){
		if(piece=='O' || piece=='X'){
			this.piece=piece;
			return true;
		}
		else return false;
	}
	
	/**
	 * Comprueba si la ficha asignada al jugador es valida.
	 * @return <code>true</code> si la ficha asignada es valida. <code>false</code> si no.
	 */
	public boolean isPieceValid(){
		if(piece=='O' || piece=='X') return true;
		return false;
	}
	
	/**
	 * Establece el nivel de juego del jugador (minimo 1 y mayor cuanto mas alto).
	 * @param level Nivel de juego que se quiere establecer.
	 * @return <code>true</code> si ha podido establecer el nivel indicado. <code>false</code> si no.
	 */
	public boolean setLevel(int level) {
		if(hasLevels()){
			if(level>0 && level<=getMaxLevel()){
				this.level=level;
				return true;
			}
		}
		return false;
	}
		
	/**
	 * Establece el tiempo de espera para jugadores no humanos.
	 * @param ms Tiempo en milisegundos.
	 */
	public void setWaitTime(long ms){
		if(ms<0) waitTime=0;
		else waitTime=ms;
	}
	
	/**
	 * Devuelve el nombre del jugador.
	 * @return Nombre del jugador.
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Devuelve el tipo de jugador.
	 * @return Tipo de jugador.
	 */
	public int getType(){
		return this.type;
	}
	
	/**
	 * Devuelve la ficha con la que juega el jugador. 
	 * @return Ficha con la que juga el jugador.
	 */
	public char getPiece(){
		return this.piece;
	}
	
	/**
	 * Devuelve el nivel actual de juego (minimo 1 y mayor cuanto mas alto).
	 * @return Entero que representa el nivel actual de juego.
	 */
	public int getLevel(){
		return this.level;
	}
		
	/**
	 * Devuelve el tiempo de espera para jugadores no humanos.
	 * @return Tiempo de espera en milisegundos.
	 */
	public long getWaitTime(){
		return this.waitTime;
	}
	
	/**
	 * Devuelve el tablero del jugador.
	 * @return Tablero.
	 */
	public Board getBoard(){
		return this.board;
	}
	
	/**
	 * Establece si el jugador esta listo para jugar.
	 * Esto depende de cada tipo de jugador, pero este metodo comprueba si se cumplen las condiciones basicas para todos.
	 * @return <code>true</code> si esta listo. <code>false</code> si no.
	 */
	public boolean setIsReady(){
		if(isPieceValid() && board!=null && !board.isFull()) isReady=true;
		else isReady=false;
		return isReady;
	}
	
	/**
	 * Comprueba si el jugador esta listo para jugar.
	 * @return <code>true</code> si el jugador esta listo para jugar.
	 */
	public boolean isReady(){
		return isReady;
	}
	
	/**
	 * Comprueba si el jugador dispone de varios niveles de juego.
	 * @return <code>true</code> si el jugador dispone de varios niveles de juego. <code>false</code> si no.
	 */
	public abstract boolean hasLevels();
	
	/**
	 * Devuelve el nivel maximo del jugador.
	 * @return Nivel maximo del jugador.
	 */
	public abstract int getMaxLevel();
	
	/**
	 * Elige una columna donde mover.
	 * @return Numero de columna elegida (1~7). -1 si no puede mover.
	 */
	public abstract int move();
	
}
