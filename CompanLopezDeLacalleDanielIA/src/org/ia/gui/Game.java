package org.ia.gui;

import java.util.Random;
import org.ia.core.Board;
import org.ia.core.Player;

/**
 * Clase implementa los metodos necesarios para jugar una partida.
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public class Game {
	
	public static final int COLS=7;
	public static final int MAX_MOVES=42;
	
	/** Tablero a actualizar */
	private Board board;
	/** Jugador 1. */
	private Player player1;
	/** Jugador 2. */
	private Player player2;
	/** ¿Juegan ambos en el mismo tablero? */
	private boolean isSameBoard;
	/** Jugador que ha hecho el primer mpvimiento. */
	private int firstMove;
	/** Jugador al que le corresponde el siguiente movimiento. */
	private int nextTurn;
	/** Numero de movimientos en total (los del jugador 1 y los del jugador 2). */
	private int nMoves;
	/** ¿Se ha terminado la partida? */
	private boolean isEnd;
	/** ¿Ha habido algun error? */
	private boolean isError;
	/** ¿Ha ganado el jugador que acaba de mover? */
	private boolean isVictory;
	/** ¿Ha ganado el jugador que acaba de mover? */
	private boolean isDraw;
	/** Mensaje de error (si ha habido alguno).*/
	private String error;
	/** Marcador [0]=Partidas empatadas; [1]=Partidas ganadas por el jugador 1; [2]=Partidas ganadas por el jugador 2. */
	private int[] score;
	/** Numero de partidas. */
	private int nGames;
	
	/**
	 * Constructor de la clase.
	 * @param p1 Jugador 1.
	 * @param p2 Jugador 2.
	 */
	public Game(Player p1, Player p2){
		this.board=null;
		this.player1=p1;
		this.player2=p2;
		if(p1.getBoard()==p2.getBoard()) this.isSameBoard=true;
		else this.isSameBoard=false;
		Random random=new Random();
		this.firstMove=random.nextInt(2)+1;
		this.nextTurn=firstMove;
		this.nMoves=0;
		this.isEnd=false;
		this.isError=false;
		this.isVictory=false;
		this.isDraw=false;
		this.error=null;
		this.score=new int[3];
		this.score[0]=0;
		this.score[1]=0;
		this.score[2]=0;
		this.nGames=0;
	}
	
	/**
	 * Le da el turno de tirar ficha al jugador que le corresponda.
	 * @return Columna en la que se ha movido. -1 si ha habido un error.
	 */
	public int turn(){
		int iTurn,iWait;
		Player pTurn;
		Board bTurn,bWait;
		String nTurn,nWait;
		char fTurn;
		int column;
		if(isEnd) return -1;
		if(nextTurn==1){
			iTurn=1;
			iWait=2;
			pTurn=player1;
			//pWait=player2;
			bTurn=player1.getBoard();
			bWait=player2.getBoard();
			nTurn=player1.getName();
			nWait=player2.getName();
			fTurn=player1.getPiece();
			//fWait=player2.getPiece();
		}
		else{
			iTurn=2;
			iWait=1;
			pTurn=player2;
			//pWait=player1;
			bTurn=player2.getBoard();
			bWait=player1.getBoard();
			nTurn=player2.getName();
			nWait=player1.getName();
			fTurn=player2.getPiece();
			//fWait=player1.getPiece();
		}
		column=pTurn.move();
		
		if(column==-1){
			error="ERROR: El jugador "+iTurn+" ("+nTurn+") no ha podido mover.";
			isEnd=true;
			isError=true;
			return -1;
		}
		if(column<1 || column>COLS){
			error="ERROR: El jugador "+iTurn+" ("+nTurn+") ha hecho un movimiento no permitido (columna devuelta "+column+".";
			isEnd=true;
			isError=true;
			return -1;
		}
		if(!bTurn.put(fTurn,column)){
			error="ERROR: No se ha podido trasladar el movimiento del jugador "+iTurn+" ("+nTurn+") en la columna "+column+" a su tablero.";
			isEnd=true;
			isError=true;
			return -1;
		}
		if(!isSameBoard){
			if(!bWait.put(fTurn,column)){
				error="ERROR: No se ha podido trasladar el movimiento del jugador "+iTurn+" ("+nTurn+") en la columna "+column+" al tablero del oponente, jugador "+iWait+" ("+nWait+").";
				isEnd=true;
				isError=true;
				return -1;
			}
		}
		if(board!=null){
			if(!board.put(fTurn,column)){
				error="ERROR: No se ha podido trasladar el movimiento del jugador "+iTurn+" ("+nTurn+") en la columna "+column+" al tablero independiente.";
				isEnd=true;
				isError=true;
				return -1;
			}
		}
		//bTurn.show();
		nMoves=nMoves+1;
		if(iTurn==1) nextTurn=2;
		else nextTurn=1;
		if(bTurn.areFourConsecutive(column)){
			score[iTurn]=score[iTurn]+1;
			nGames=nGames+1;
			isEnd=true;
			isVictory=true;
			return column;
		}
		if(nMoves==MAX_MOVES){
			score[0]=score[0]+1;
			nGames=nGames+1;
			isEnd=true;
			isDraw=true;
			return column;
		}
		return column;
	}
	
	/**
	 * Juega la partida hasta el final.
	 * @return <code>true</code> si la partida ha acabado correctamente. <code>false</code> si se ha interrumpido por algun error.
	 */
	public boolean play(){
		while(!isEnd){
			turn();
		}
		return !isError;
	}
	
	/**
	 * Crea una nueva partida con los mismos jugadores.
	 */
	public void restart(){
		player1.getBoard().clear();
		if(!isSameBoard) player2.getBoard().clear();
		if(board!=null) board.clear();
		if(firstMove==1) firstMove=2;
		else firstMove=1;
		nextTurn=firstMove;
		this.nMoves=0;
		this.isEnd=false;
		this.isError=false;
		this.isVictory=false;
		this.isDraw=false;
		this.error=null;
	}
	
	/**
	 * Devuelve el numero del jugador al que le corresponde hacer el primer movimiento.
	 * @return Numero del jugador que ha iniciado la partida.
	 */
	public int getFirstMove(){
		return this.firstMove;
	}
	
	/**
	 * Devuelve el numero del jugador al que le corresponde el siguiente movimiento.
	 * @return Numero del jugador al que le toca mover.
	 */
	public int getNextTurn(){
		return this.nextTurn;
	}
	
	/**
	 * Devuelve el numero de movimientos (de ambos jugadores).
	 * @return Numero de movimientos.
	 */
	public int getNumberOfMoves(){
		return this.nMoves;
	}
	
	/**
	 * Comrueba si la partida ha finalizado.
	 * @return <code>true</code> si se ha acabado la partida.
	 */
	public boolean isEnd(){
		return this.isEnd;
	}
	
	/**
	 * Comrueba si ha habido algun error.
	 * @return <code>true</code> si ha habido un error.
	 */
	public boolean isError(){
		return this.isError;
	}
	
	/**
	 * Comrueba si ha habido algun vencedor en la ultima jugada.
	 * @return <code>true</code> si ha habido un ganador.
	 */
	public boolean isVictory(){
		return this.isVictory;
	}
	
	/**
	 * Comrueba si ha habido un empate en la ultima jugada (tablero lleno).
	 * @return <code>true</code> si ha habido un empate.
	 */
	public boolean isDraw(){
		return this.isDraw;
	}
	
	/**
	 * Devuelve el mensaje de error.
	 * @return Cadena con el mensaje de error.
	 */
	public String getError(){
		return this.error;
	}
	
	/**
	 * Devuelve el marcador. [0]=Partidas empatadas; [1]=Partidas ganadas por el jugador 1; [2]=Partidas ganadas por el jugador 2.
	 * @return Array de enteros con el marcador.
	 */
	public int[] getScore(){
		return this.score;
	}
	
	/**
	 * Devuelve el numero de partidas jugadas hasta el momento.
	 * @return Numero de partidas.
	 */
	public int getNumGames(){
		return this.nGames;
	}
	
	/**
	 * Establece un tablero extra a actualizar.
	 * @param b Tablero independiente.
	 */
	public void setBoard(Board b){
		this.board=b;
	}
	
}

