package org.ia.core;

/**
 * Clase que implementa una Base de Conocimiento Aumentada (BCA) para ser usada con reglas.
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public class BCA extends Board {
	
	public static final int ROWS=8;
	public static final int COLS=9;
	
	/** Matriz de 8x9 que representa un tablero de 6 filas y 7 columnas. */
	private char[][] bca;
	
	/**
	 * Constructor de la clase.
	 */
	public BCA(){
		setType(BCA_BOARD);
		this.bca=new char[ROWS][COLS];
		clear();
	}
	
	/**
	 * Constructor de la clase.
	 * Crea una nueva BCA a partir de otra.
	 * @param sample BCA de la que se quiere duplicar su estado.
	 */
	public BCA(BCA sample){
		setType(BCA_BOARD);
		this.bca=new char[ROWS][COLS];
		if(sample==null) clear();
		else copy(sample);
	}
	
	/**
	 * Inicializa la BCA, lo cual se corresponde con un tablero sin fichas. 
	 */
	public void clear(){
		int r,c;
		r=0;
		for(c=0;c<COLS;c++) bca[r][c]='I';
		for(r=1;r<ROWS-2;r++){
			for(c=1;c<COLS-1;c++){
				bca[r][c]='z';
			}
		}
		r=ROWS-2;
		for(c=1;c<COLS-1;c++) bca[r][c]='_';
		r=ROWS-1;
		for(c=0;c<COLS;c++) bca[r][c]='I';
		c=0;
		for(r=1;r<ROWS-1;r++) bca[r][c]='I';
		c=COLS-1;
		for(r=1;r<ROWS-1;r++) bca[r][c]='I';
	}
	
	/**
	 * Toma el estado de otra BCA.
	 * @param sample BCA que se quiere copiar.
	 */
	public void copy(BCA sample){
		if(bca!=null){ 
			for(int r=0;r<ROWS;r++){
				for(int c=0;c<COLS;c++){
					this.bca[r][c]=sample.bca[r][c];
				}
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
		if(column<1 || column>COLS-2) return false;
		if(bca[1][column]=='O' || bca[1][column]=='X') return false;
		for(int r=ROWS-2;r>0;r--){
			if(bca[r][column]=='_'){
				bca[r][column]=piece;
				if(r>1) bca[r-1][column]='_';
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
		if(column<1 || column>COLS-2) return false;
		if(bca[ROWS-2][column]=='_') return false;
		for(int r=1;r<ROWS-1;r++){
			if(bca[r][column]=='O' || bca[r][column]=='X'){
				bca[r][column]='_';
				if(r>1) bca[r-1][column]='z';
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
		if(column<1 || column>COLS-2) return true;
		if(bca[1][column]=='z' || bca[1][column]=='_') return false;
		return true;
	}
	
	/**
	 * Comprueba si el tablero esta lleno de fichas.
	 * @return <code>true</code> si esta lleno.
	 */
	public boolean isFull(){
		for(int c=1;c<COLS-1;c++){
			if(bca[1][c]=='z' || bca[1][c]=='_') return false;
		}
		return true;
	}
	
	/**
	 * Indica si el estado del tablero es simetrico.
	 * Puede usarse para evaluar solo la mitad de las columnas.
	 * @return <code>true</code> si las fichas estan colocadas de forma simetrica. 
	 */
	public boolean isSymmetric(){
		int column=1;
		int mirrowColumn=COLS-2;
		while(column<mirrowColumn){
			for(int row=ROWS-2;row>0;row--){
				if(bca[row][column]!=bca[row][mirrowColumn]) return false;
				if(bca[row][column]=='_') break;
			}
			column=column+1;
			mirrowColumn=mirrowColumn-1;
		}
		return true;
	}
	
	/**
	 * Devuelve la fila en la que se encuentra la última ficha que se ha colocado en una columna.
	 * @param column Numero de columna (1~7).
	 * @return Fila donde se encuentra la ultima ficha colocada en la columna indicada. -1 si no hay ninguna.
	 */
	private int getLastPieceRow(int column){
		if(column<1 || column>COLS-2) return -2;
		for(int r=1;r<ROWS-1;r++){
			if(bca[r][column]=='O' || bca[r][column]=='X') return r;
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
		for(int c=0;c<COLS;c++)	str=str+bca[row][c];
		return str;
	}
	
	/**
	 * Devuelve una columna de la BCA (de arriba a abajo).
	 * @param column Numero de columna que se desea obtener (usar indice).
	 * @return Cadena con la columna solicitada.
	 */
	public String getColumn(int column){
		if(column<0 || column>=COLS) return null;
		String str="";
		for(int r=0;r<ROWS;r++)	str=str+bca[r][column];
		return str;		
	}
	
	/**
	 * Devuelve la diagonal ascendente (de izquierda a derecha, y de abajo a arriba) que pasa por un elemento de la BCA.
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
			str=str+bca[r][c];
			r=r-1;
			c=c+1;
		}
		r=row+1;
		c=column-1;
		while(r<ROWS && c>=0){
			str=bca[r][c]+str;
			r=r+1;
			c=c-1;
		}
		return str;
	}
	
	/**
	 * Devuelve la diagonal descendente (de izquierda a derecha, y de arriba a abajo) que pasa por un elemento de la BCA.
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
			str=str+bca[r][c];
			r=r+1;
			c=c+1;
		}
		r=row-1;
		c=column-1;
		while(r>=0 && c>=0){
			str=bca[r][c]+str;
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
		if(column<1 || column>COLS-2) return false;
		int row=getLastPieceRow(column);
		if(row<1 || row>ROWS-2) return false;
		char piece=bca[row][column];
		if(piece!='O' && piece!='X') return false;
		String str=this.getRow(row);
		if(areFourConsecutive(str,piece)) return true;
		str=this.getColumn(column);
		if(areFourConsecutive(str,piece)) return true;
		str=this.getDiagonalAscending(row,column);
		if(areFourConsecutive(str,piece)) return true;
		str=this.getDiagonalDescending(row,column);
		if(areFourConsecutive(str,piece)) return true;
		return false;
	}
	
	/**
	 * Devuelve un array con las 4 secuencias de prueba. [0]=H , [1]=V , [2]=Da , [3]=Dd . 
	 * @param column Numero de columna de la que se quieren obtener las secuencias de prueba (1~7).
	 * @return Array de <code>Sequence</code> con las 4 secuencias de prueba. <code>null</code> si se ha introducido una columna no valida o esta llena.
	 */
	public Sequence[] getTrySequences(int column){
		if(isFull(column)) return null;
		Sequence[] trySequences=new Sequence[4];
		for(int i=0;i<4;i++) trySequences[i]=null;
		int r,c;
		int row=-1;
		int sincPos;
		String str;
		// Vertical
		str="";
		for(r=0;r<ROWS;r++){
			str=str+bca[r][column];
			if(bca[r][column]=='_') row=r;
		}
		trySequences[1]=new Sequence(str,row);
		// Horizontal
		str="";
		for(c=0;c<COLS;c++)	str=str+bca[row][c];
		trySequences[0]=new Sequence(str,column);
		// Diagonal ascendente (de izq a dcha)
		r=row;
		c=column;
		str="";
		while(r>=0 && c<COLS){
			str=str+bca[r][c];
			r=r-1;
			c=c+1;
		}
		r=row+1;
		c=column-1;
		sincPos=0;
		while(r<ROWS && c>=0){
			str=bca[r][c]+str;
			r=r+1;
			c=c-1;
			sincPos=sincPos+1;
		}
		trySequences[2]=new Sequence(str,sincPos);
		// Diagonal descendente (de izq a dcha)
		r=row;
		c=column;
		str="";
		while(r<ROWS && c<COLS){
			str=str+bca[r][c];
			r=r+1;
			c=c+1;
		}
		r=row-1;
		c=column-1;
		sincPos=0;
		while(r>=0 && c>=0){
			str=bca[r][c]+str;
			r=r-1;
			c=c-1;
			sincPos=sincPos+1;
		}
		trySequences[3]=new Sequence(str,sincPos);
		return trySequences;
	}
	
	/**
	 * Devuelve un array con las 4 secuencias de prueba que se obtendrian despues de haber colocado una ficha. [0]=H , [1]=V , [2]=Da , [3]=Dd .
	 * El estado de la BCA no cambia.
	 * @param column Numero de columna de la que se quieren obtener las secuencias de prueba despues de colocar una ficha (1~7).
	 * @param piece Ficha que se quiere colocar.
	 * @return Array de <code>Sequence</code> con las 4 secuencias de prueba. <code>null</code> si se ha introducido una columna no valida o esta llena antes o despues de colocar la ficha.
	 */
	public Sequence[] getNextMoveTrySequences(int column, char piece){
		if(column<1 || column>COLS-2) return null;
		if(piece!='O' && piece!='X') return null;
		if(bca[1][column]!='z') return null;
		Sequence[] nextMoveTrySequences=new Sequence[4];
		for(int i=0;i<4;i++) nextMoveTrySequences[i]=null;
		int r,c;
		int row=-1;
		int sincPos;
		String str;
		// Vertical
		str="";
		for(r=0;r<ROWS;r++){
			if(bca[r][column]=='z' && bca[r+1][column]=='_'){
				str=str+"_";
				row=r;
			}
			else if(bca[r][column]=='_'){ str=str+piece; }
			else{ str=str+bca[r][column]; }
		}
		nextMoveTrySequences[1]=new Sequence(str,row);
		// Horizontal
		str="";
		for(c=0;c<COLS;c++){
			if(c==column) str=str+"_";
			else str=str+bca[row][c];
		}
		nextMoveTrySequences[0]=new Sequence(str,column);
		// Diagonal ascendente (de izq a dcha)
		r=row-1;
		c=column+1;
		str="_";
		while(r>=0 && c<COLS){
			str=str+bca[r][c];
			r=r-1;
			c=c+1;
		}
		r=row+1;
		c=column-1;
		sincPos=0;
		while(r<ROWS && c>=0){
			str=bca[r][c]+str;
			r=r+1;
			c=c-1;
			sincPos=sincPos+1;
		}
		nextMoveTrySequences[2]=new Sequence(str,sincPos);
		// Diagonal descendente (de izq a dcha)
		r=row+1;
		c=column+1;
		str="_";
		while(r<ROWS && c<COLS){
			str=str+bca[r][c];
			r=r+1;
			c=c+1;
		}
		r=row-1;
		c=column-1;
		sincPos=0;
		while(r>=0 && c>=0){
			str=bca[r][c]+str;
			r=r-1;
			c=c-1;
			sincPos=sincPos+1;
		}
		nextMoveTrySequences[3]=new Sequence(str,sincPos);
		return nextMoveTrySequences;
	}
	
	/**
	 * Devuelve una matriz con todas las secuencias de prueba de la BCA en el momento actual.
	 * El primer indice es el correspondiente a la columna [0~6].
	 * El segundo indice es la direccion de la secuencia de prueba: [0]=H , [1]=V , [2]=Da , [3]=Dd .
	 * @return Matriz con todas las sentencias de prueba de la BCA.
	 */
	public Sequence[][] getTrySequences(){
		Sequence[][] trySequences=new Sequence[COLS-2][4];
		for(int c=1;c<COLS-1;c++) trySequences[c-1]=getTrySequences(c);
		return trySequences;
	}
	
	/**
	 * Devuelve una matriz con las 4 secuencias de prueba de todas las columnas de la BCA despues de haber colocado una ficha en cada una de ellas pero no en el resto.
	 * El primer indice hace referencia a la columna [0~6].
	 * el segundo a la direccion de la secuencia de prueba: [0]=H , [1]=V , [2]=Da , [3]=Dd .
	 * El estado de la BCA no cambia.
	 * @param piece Ficha que se quiere colocar.
	 * @return Matriz con todas las sentencias de prueba de la BCA despues de colocar la ficha.
	 */
	public Sequence[][] getNextMoveTrySequences(char piece){
		if(piece!='O' && piece!='X') return null;
		Sequence[][] nextMoveTrySequences=new Sequence[COLS-2][4];
		for(int c=1;c<COLS-1;c++) nextMoveTrySequences[c-1]=getNextMoveTrySequences(c,piece);
		return nextMoveTrySequences;
	}
	
	/**
	 * Imprime por pantalla una matriz de secuencias de prueba.
	 * @param sequences Matriz de secuencias de prueba con indices [columna(0~6)][direccion(0~3)].
	 */
	public static void printTrySequences(Sequence[][] trySequences){
		if(trySequences==null) Log.msg("Null",1);
		else{
			for(int c=0;c<trySequences.length;c++){
				if(trySequences[c]==null) Log.msg("Columna "+(c+1)+" llena.",1);
				else{
					Log.msg("Columna "+(c+1)+":",1);
					for(int d=0;d<trySequences[c].length;d++){
						String dir;
						switch(d){
							case 0:
								dir="H ";
								break;
							case 1:
								dir="V ";
								break;
							case 2:
								dir="Da";
								break;
							case 3:
								dir="Dd";
								break;
							default:
								if(d<10) dir=d+" "; 
								else dir=d+"";
						}
						if(trySequences[c][d]==null) Log.msg("  "+dir+": ERROR",1);
						else Log.msg("  "+dir+": "+trySequences[c][d].toString(),1);
					}
				}
			}
		}
	}
	
	/**
	 * Imprime por pantalla las secuencias de prueba de la BCA en el momento actual.
	 */
	public void printTrySequences(){
		printTrySequences(getTrySequences());
	}
	
	/**
	 * Imprime por pantalla las secuencias de prueba de la BCA despues de haber colocado una ficha en cada una de las columnas peron no en el resto.
	 */
	public void printNextMoveTrySequences(char piece){
		printTrySequences(getNextMoveTrySequences(piece));
	}
	
	/**
	 * Devuelve una cadena con el contenido de la BCA en el momento actual.
	 */
	@Override
	public String toString(){
		String str="";
		for(int r=0;r<ROWS;r++){
			for(int c=0;c<COLS;c++){
				str=str+bca[r][c];
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
		for(int r=1;r<ROWS-1;r++){
			for(int c=0;c<COLS;c++){
				if(bca[r][c]=='I') str=str+"|";
				else{
					if(bca[r][c]=='O' || bca[r][c]=='X') str=str+bca[r][c];
					else str=str+" ";
				}
			}
			str=str+"\n";
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
