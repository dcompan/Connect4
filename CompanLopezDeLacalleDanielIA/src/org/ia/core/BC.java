package org.ia.core;

/**
 * Clase que implementa una Base de Conocimiento (BCA).
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public class BC extends Board {

	public static final int ROWS=6;
	public static final int COLS=7;
	
	/** Matriz de 8x9 que representa un tablero de 6 filas y 7 columnas. */
	private char[][] bc;

	/**
	 * Constructor de la clase.
	 */
	public BC(){
		setType(BC_BOARD);
		this.bc=new char[ROWS][COLS];
		clear();
	}
	
	/**
	 * Inicializa la BC, lo cual se corresponde con un tablero sin fichas. 
	 */
	public void clear(){
		for(int r=0;r<ROWS;r++){
			for(int c=0;c<COLS;c++){
				bc[r][c]='E';
			}
		}
	}
	
	/**
	 * Coloca una nueva ficha.
	 * @param piece 'O' o 'X'.
	 * @param column Numero de columna (1~7).
	 * @return <code>true</code> si se ha podido colocar la nueva ficha.
	 */
	public boolean put(char piece, int column){
		if(piece!='O' && piece!='X') return false;
		if(column<1 || column>COLS) return false;
		int c=column-1;
		if(bc[0][c]=='O' || bc[0][c]=='X') return false;
		for(int r=ROWS-1;r>=0;r--){
			if(bc[r][c]=='E'){
				bc[r][c]=piece;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Elimina la ultima ficha colocada en una columna.
	 * @param column Columna de la que se quiere quitar la ultima ficha (1~7).
	 * @return <code>true</code> si se ha podido quitar.
	 */
	public boolean remove(int column){
		if(column<1 || column>COLS) return false;
		int c=column-1;
		if(bc[ROWS-1][c]=='E') return false;
		for(int r=0;r<ROWS;r++){
			if(bc[r][c]=='O' || bc[r][c]=='X'){
				bc[r][c]='E';
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Comprueba si una columna esta llena de fichas.
	 * @param column Numero de columna que se quiere comprobar (1~7).
	 * @return <code>true</code> si esta llena.
	 */
	public boolean isFull(int column){
		if(column<1 || column>COLS) return true;
		int c=column-1;
		if(bc[0][c]=='E') return false;
		return true;
	}
	
	/**
	 * Comprueba si el tablero esta lleno de fichas.
	 * @return <code>true</code> si esta lleno.
	 */
	public boolean isFull(){
		for(int c=0;c<COLS;c++){
			if(bc[0][c]=='E') return false;
		}
		return true;
	}
	
	/**
	 * Indica si el estado del tablero es simetrico.
	 * Puede usarse para evaluar solo la mitad de las columnas.
	 * @return <code>true</code> si las fichas estan colocadas de forma simetrica. 
	 */
	public boolean isSymmetric(){
		int column=0;
		int mirrowColumn=COLS-1;
		while(column<mirrowColumn){
			for(int row=ROWS-1;row>=0;row--){
				if(bc[row][column]!=bc[row][mirrowColumn]) return false;
				if(bc[row][column]=='E') break;
			}
			column=column+1;
			mirrowColumn=mirrowColumn-1;
		}
		return true;
	}
	
	/**
	 * Devuelve la fila en la que se encuentra la última ficha que se ha colocado en una columna.
	 * @param column Numero de columna (usar indice).
	 * @return Fila donde se encuentra la ultima ficha colocada en la columna indicada. -1 si no hay ninguna.
	 */
	private int getLastPieceRow(int column){
		if(column<0 || column>=COLS) return -2;
		for(int r=0;r<ROWS;r++){
			if(bc[r][column]=='O' || bc[r][column]=='X') return r;
		}
		return -1;
	}
	
	/**
	 * Devuelve una fila de la BC (de izquierda a derecha).
	 * @param row Numero de fila que se desea obtener (usar indice).
	 * @return Cadena con la fila solicitada.
	 */
	public String getRow(int row){
		if(row<0 || row>=ROWS) return null;
		String str="";
		for(int c=0;c<COLS;c++)	str=str+bc[row][c];
		return str;
	}
	
	/**
	 * Devuelve una columna de la BC (de arriba a abajo).
	 * @param column Numero de columna que se desea obtener (usar indice).
	 * @return Cadena con la columna solicitada.
	 */
	public String getColumn(int column){
		if(column<0 || column>=COLS) return null;
		String str="";
		for(int r=0;r<ROWS;r++)	str=str+bc[r][column];
		return str;		
	}
	
	/**
	 * Devuelve la diagonal ascendente (de izquierda a derecha, y de abajo a arriba) que pasa por un elemento de la BC.
	 * @param row Fila del elemento por el cual pasa la diagonal ascendente que se desea obtener (usar indice).
	 * @param column Columna del elemento por el cual pasa la diagonal ascendente que se desea obtener (usar indice).
	 * @return Cadena con la diagonal ascendente solicitada.
	 */
	public String getDiagonalAscending(int row, int column){
		int r,c;
		String str="";
		if(row<0 || row>=ROWS) return null;
		if(column<0 || column>=COLS) return null;
		r=row;
		c=column;
		while(r>=0 && c<COLS){
			str=str+bc[r][c];
			r=r-1;
			c=c+1;
		}
		r=row+1;
		c=column-1;
		while(r<ROWS && c>=0){
			str=bc[r][c]+str;
			r=r+1;
			c=c-1;
		}
		return str;
	}
	
	/**
	 * Devuelve la diagonal descendente (de izquierda a derecha, y de arriba a abajo) que pasa por un elemento de la BC.
	 * @param row Fila del elemento por el cual pasa la diagonal descendente que se desea obtener (usar indice).
	 * @param column Columna del elemento por el cual pasa la diagonal descendente que se desea obtener (usar indice).
	 * @return Cadena con la diagonal descendente solicitada.
	 */
	public String getDiagonalDescending(int row, int column){
		int r,c;
		String str="";
		if(row<0 || row>=ROWS) return null;
		if(column<0 || column>=COLS) return null;
		r=row;
		c=column;
		while(r<ROWS && c<COLS){
			str=str+bc[r][c];
			r=r+1;
			c=c+1;
		}
		r=row-1;
		c=column-1;
		while(r>=0 && c>=0){
			str=bc[r][c]+str;
			r=r-1;
			c=c-1;
		}
		return str;
	}
	
	/**
	 * Comprueva si hay 4 fichas del mismo tipo en una cadena de caracteres.
	 * @param str Cadena de caracteres a comprobar.
	 * @param piece Ficha a comprobar.
	 * @return <code>true</code> si hay 4 iguales seguidas.
	 */
	private boolean areFourConsecutive(String str, char piece){
		if(str==null || str.length()==0) return false;
		char c;
		int count=0;
		for(int i=0;i<str.length(); i++){
			c=str.charAt(i);
			if(c!=piece) count=0;
			else{
				count=count+1;
				if(count>=4) return true;
			}
		}
		return false;
	}
	
	/**
	 * Comprueva si la ultima ficha que se ha colocado en una columna forma 4 en raya.
	 * @param column Numero de columna en la que se acaba de colocar la ultima ficha (1~7).
	 * @return <code>true</code> si hay 4 iguales seguidas.
	 */
	public boolean areFourConsecutive(int column){
		if(column<1 || column>COLS) return false;
		int c=column-1;
		int r=getLastPieceRow(c);
		if(r<0 || r>=ROWS) return false;
		char piece=bc[r][c];
		if(piece!='O' && piece!='X') return false;
		String str=this.getRow(r);
		if(areFourConsecutive(str,piece)) return true;
		str=this.getColumn(c);
		if(areFourConsecutive(str,piece)) return true;
		str=this.getDiagonalAscending(r,c);
		if(areFourConsecutive(str,piece)) return true;
		str=this.getDiagonalDescending(r,c);
		if(areFourConsecutive(str,piece)) return true;
		return false;
	}
	
	/**
	 * Devuelve una cadena con el contenido de la BC.
	 */
	@Override
	public String toString(){
		String str="";
		for(int r=0;r<ROWS;r++){
			for(int c=0;c<COLS;c++){
				str=str+bc[r][c];
			}
			str=str+'\n';
		}
		return str;
	}
	
	/**
	 * Imprime por consola el tablero.
	 */
	void printBoard(){
		String str=" 1234567 \n+-------+\n";
		for(int r=0;r<ROWS;r++){
			str=str+"|";
			for(int c=0;c<COLS;c++){
				if(bc[r][c]=='O' || bc[r][c]=='X') str=str+bc[r][c];
				else str=str+" ";
			}
			str=str+"|\n";
		}
		str=str+"+-------+\n 1234567 ";
		Log.msg(str,Log.CONSOLE);
	}
	
	/**
	 * Mostrar los cambios.
	 */
	public void show(){
		printBoard();
	}
	
}
