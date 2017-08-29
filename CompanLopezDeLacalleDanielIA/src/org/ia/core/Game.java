package org.ia.core;

import java.util.Random;

/**
 * Clase implementa los metodos necesarios para jugar una partida.
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public class Game {
	
	public static final int COLS=7;
	public static final int MAX_MOVES=42;
	
	/** Jugador 1 */
	private Player player1;
	/** Jugador 2 */
	private Player player2;
	/** ¿Juegan ambos en el mismo tablero? */
	private boolean isSameBoard;
	/** Jugador que ha hecho el primer mpvimiento */
	private int firstMove;
	/** Jugador al que le corresponde el siguiente movimiento */
	private int nextTurn;
	/** Numero de movimientos en total (los del jugador 1 y los del jugador 2) */
	private int nMoves;
	/** ¿Se ha terminado la partida? */
	private boolean isEnd;
	/** ¿Ha habido algun error? */
	private boolean isError;
	/** Marcador [0]=Partidas empatadas; [1]=Partidas ganadas por el jugador 1; [2]=Partidas ganadas por el jugador 2. */
	private int[] score;
	/** Numero de partidas */
	private int nGames;
	
	/**
	 * Constructor de la clase.
	 * @param p1 Jugador 1.
	 * @param p2 Jugador 2.
	 */
	public Game(Player p1, Player p2){
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
		this.score=new int[3];
		this.score[0]=0;
		this.score[1]=0;
		this.score[2]=0;
		this.nGames=0;
	}
	
	/**
	 * Le da el turno de tirar ficha a al jugador que le corresponda.
	 * @return <code>true</code> si se ha llegado al final de la partida (o ha habido un error).
	 */
	public boolean turn(){
		int iTurn,iWait;
		Player pTurn;
		Board bTurn,bWait;
		String nTurn,nWait;
		char fTurn,fWait;
		int column;
		if(isEnd) return true;
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
			fWait=player2.getPiece();
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
			fWait=player1.getPiece();
		}
		if(nMoves==0){
			bTurn.show();
			Log.msg("Primer movimiento para el jugador "+iTurn+" ("+nTurn+") ["+fTurn+"].",Log.CONSOLE);
		}
		column=pTurn.move();
		if(column==-1){
			Log.msg("ERROR: El jugador "+iTurn+" ("+nTurn+") ["+fTurn+"] no ha podido mover.",Log.ERROR);
			isEnd=true;
			isError=true;
			return true;
		}
		if(column<1 || column>COLS){
			Log.msg("ERROR: El jugador "+iTurn+" ("+nTurn+") ["+fTurn+"] ha hecho un movimiento no permitido (columna devuelta "+column+".",Log.ERROR);
			isEnd=true;
			isError=true;
			return true;
		}
		if(!bTurn.put(fTurn,column)){
			Log.msg("ERROR: No se ha podido trasladar el movimiento del jugador "+iTurn+" ("+nTurn+") ["+fTurn+"] en la columna "+column+" a su tablero.",Log.ERROR);
			isEnd=true;
			isError=true;
			return true;
		}
		if(!isSameBoard){
			if(!bWait.put(fTurn,column)){
				Log.msg("ERROR: No se ha podido trasladar el movimiento del jugador "+iTurn+" ("+nTurn+") ["+fTurn+"] en la columna "+column+" al tablero del oponente, jugador "+iWait+" ("+nWait+") ["+fWait+"] .",Log.ERROR);
				isEnd=true;
				isError=true;
				return true;
			}
		}
		nMoves=nMoves+1;
		Log.msg(nMoves+" - El jugador "+iTurn+" ("+nTurn+") ["+fTurn+"] juega en la columna "+column+".",Log.INFO);
		bTurn.show();
		if(iTurn==1) nextTurn=2;
		else nextTurn=1;
		if(bTurn.areFourConsecutive(column)){
			Log.msg("¡Victoria del jugador "+iTurn+" ("+nTurn+") ["+fTurn+"]!",Log.CONSOLE);
			score[iTurn]=score[iTurn]+1;
			nGames=nGames+1;
			Log.msg("MARCADOR:",Log.INFO);
			Log.msg("  Victorias P1 ("+player1.getName()+") ["+player1.getPiece()+"]: "+score[1]+"/"+nGames+".",Log.INFO);
			Log.msg("  Victorias P2 ("+player2.getName()+") ["+player2.getPiece()+"]: "+score[2]+"/"+nGames+".",Log.INFO);
			Log.msg("  Empates: "+score[0]+"/"+nGames+".",Log.INFO);
			isEnd=true;
			return true;
		}
		if(nMoves==MAX_MOVES){
			Log.msg("¡Empate!",Log.INFO);
			score[0]=score[0]+1;
			nGames=nGames+1;
			Log.msg("MARCADOR:",Log.INFO);
			Log.msg("  Victorias P1 ("+player1.getName()+") con "+player1.getPiece()+"s: "+score[1]+"/"+nGames+".",Log.INFO);
			Log.msg("  Victorias P2 ("+player2.getName()+") con "+player2.getPiece()+"s: "+score[2]+"/"+nGames+".",Log.INFO);
			Log.msg("  Empates: "+score[0]+"/"+nGames+".",Log.INFO);
			isEnd=true;
			return true;
		}
		return false;
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
		if(firstMove==1) firstMove=2;
		else firstMove=1;
		nextTurn=firstMove;
		this.nMoves=0;
		this.isEnd=false;
		this.isError=false;
	}
	
}
