package org.ia.core;

import java.util.Scanner;
import java.util.NoSuchElementException;

/**
 * Clase que maneja la entrada por teclado.
 * 
 * @author Daniel Compan Lopez de Lacalle
 */
public class Console {
	
	/** Buffer de lectura para tomar las entrada por teclado. */
	private Scanner scanner;
	
	/**
	 * Constructor de la clase.
	 */
	Console(){
		this.scanner=new Scanner(System.in);
	}
		
	/**
	 * Devuelve el jugador que se ha solicitado por consola.
	 * @param num Numero de jugador (1 o 2).
	 * @param piece Ficha del jugador ('O' o 'X').
	 * @param bcBoard Tablero simple (base de conocimiento).
	 * @param bcBoard Tablero avanzado (Base de Conocimiento Aumentada).
	 * @param console Referencia a la consola que maneja la entrada por teclado.
	 * @return Nuevo jugador. <code>null</code> en caso de error.
	 */
	public Player scanNewPlayer(int num, char piece, BC bcBoard, BCA bcaBoard, Console console){
		int choice=console.scanChoosePlayer(num);
		switch(choice){
			case Player.HUMAN_PLAYER:
				PlayerHuman ph=new PlayerHuman(bcBoard,piece);
				if(console.scanLevel(ph)==-1) System.exit(-1);
				ph.setConsole(console);
				return ph;
			case Player.RANDOM_PLAYER:
				PlayerRandom pd=new PlayerRandom(bcBoard,piece);
				if(console.scanLevel(pd)==-1) System.exit(-1);
				return pd;
			case Player.RULES_PLAYER:
				PlayerRules pr=new PlayerRules(bcaBoard,piece);
				if(console.scanLevel(pr)==-1) System.exit(-1);
				pr.openRulesFile();
				return pr;
			case Player.MINMAX_PLAYER:
				PlayerMinimax pm=new PlayerMinimax(bcaBoard,piece);
				if(console.scanLevel(pm)==-1) System.exit(-1);
				return pm;
			default:
				return null;	
		}
	}
	
	/**
	 * Devuelve una respuesta introducida por teclado para la eleccion del jugador en una nueva partida.
	 * @param num Numero de jugador (1 o 2).
	 * @return Entero con el tipo de jugador escogido (1~4). -1 si ha habido un error.
	 */
	public int scanChoosePlayer(int num){
		int choice;
		if(scanner==null){
			Log.msg("¡¡EXCEPCION!! Entrada por consola no disponible.",Log.EXCEPTION);
			return -1;
		}
		Log.msg("Escoja jugador "+num+":",Log.CONSOLE);
		for(int i=1;i<Player.PLAYERS.length;i++) Log.msg(" "+i+" - "+Player.PLAYERS[i]+".",Log.CONSOLE);
		//Log.msg(" 2 - Random.",Log.CONSOLE);
		//Log.msg(" 3 - Rules.",Log.CONSOLE);
		//Log.msg(" 4 - Minimax.",Log.CONSOLE);
		do{
			Log.msg("¿Eleccion? (1~"+(Player.PLAYERS.length-1)+") ",Log.CONSOLE);
			choice=scanDigit();
			switch(choice){
				case 1:
					return Player.HUMAN_PLAYER;
				case 2:
					return Player.RANDOM_PLAYER;
				case 3:
					return Player.RULES_PLAYER;
				case 4:
					return Player.MINMAX_PLAYER;
				case -1:
					return -1;	
			}
		}while(true);
	}
	
	/**
	 * Devuelve una respuesta introducida por teclado a la pregunta de que nivel desea para el jugador (y se lo asigna).
	 * @param p Jugador;
	 * @return Numero con el nivel elegido (ya establecido). 0 si no tiene niveles. -1 si ha habido un error.
	 */
	public int scanLevel(Player p){
		int choice;
		if(scanner==null){
			Log.msg("¡¡EXCEPCION!! Entrada por consola no disponible.",Log.EXCEPTION);
			return -1;
		}
		if(!p.hasLevels()) return 0;
		do{
			Log.msg("¿Nivel? (1~"+p.getMaxLevel()+") ",Log.CONSOLE);
			choice=scanDigit();
			if(choice>=1 && choice<=p.getMaxLevel()){
				p.setLevel(choice);
				return choice;
			}
		}while(true);
	}
	
	/**
	 * Lee un digito por consola.
	 * @return Digito introducido. 10 si se ha introducido otra cosa. -1 si ha habido un error.
	 */
	public int scanDigit(){
		String input=null;
		if(scanner==null){
			Log.msg("¡¡EXCEPCION!! Entrada por consola no disponible.",Log.EXCEPTION);
			return -1;
		}
		try{
			input=scanner.nextLine();
		}catch(NoSuchElementException nsee){
			// El buffer esta vacio y la entrada por consola cerrada (al cerrar el Scanner).
			Log.msg("¡¡EXCEPCION!! Entrada por consola no disponible.",Log.EXCEPTION);
			return -1;
		}catch(IllegalStateException ise){
			// Scanner no creado o cerrado de forma prematura.
			Log.msg("¡¡EXCEPCION!! Entrada por consola no disponible.",Log.EXCEPTION);
			return -1;
		}
		if(input.length()!=1) return 10;
		char c=input.charAt(0);
		switch(c){
			case '0':
				return 0;
			case '1':
				return 1;
			case '2':
				return 2;
			case '3':
				return 3;
			case '4':
				return 4;
			case '5':
				return 5;
			case '6':
				return 6;
			case '7':
				return 7;
			case '8':
				return 8;
			case '9':
				return 9;
			default:
				return 10;
		}
	}
	
	/**
	 * Devuelve una respuesta introducida por teclado a la pregunta de si se desea jugar una nueva partida.
	 * @return  1=SI , 0=NO , -1=ERROR.
	 */
	public int scanPlayAgain(){
		String input=null;
		char c;
		if(scanner==null){
			Log.msg("¡¡EXCEPCION!! Entrada por consola no disponible.",Log.EXCEPTION);
			return -1;
		}
		do{
			Log.msg("¿Volver a jugar? (s/n) ",Log.CONSOLE);
			try{
				input=scanner.nextLine();
			}catch(NoSuchElementException nsee){
				// El buffer esta vacio y la entrada por consola cerrada (al cerrar el Scanner).
				Log.msg("¡¡EXCEPCION!! Entrada por consola no disponible.",Log.EXCEPTION);
				return -1;
			}catch(IllegalStateException ise){
				// Scanner no creado o cerrado de forma prematura.
				Log.msg("¡¡EXCEPCION!! Entrada por consola no disponible.",Log.EXCEPTION);
				return -1;
			}
			if(input.length()==1){
				c=input.charAt(0);
				if(c=='n' || c=='N') return 0;
				else{
					if(c=='s' || c=='S') return 1;
				}
			}
		}while(true);	
	}
	
	/**
	 * Comprueba si el buffer de lectura esta inicializdo.
	 * @return <code>true</code> si es null. <code>false</code> si no.
	 */
	public boolean isNull(){
		if(scanner==null) return true;
		return false;
	}
	
	/**
	 * Cierra el buffer de lectura.
	 * ¡Atencion!: Esto tambien deshabilita la entrada por teclado del sistema. Después no será posible realizar ninguna otra lectura, incluso si se crea un nuevo Scanner.
	 * A pesar de lo cual es necesario cerrar el buffer antes de terminar el programa para liberar el recurso.   
	 */
	public void close(){
		try{
			scanner.close();
		}catch(IllegalStateException ise){
			// Scanner no creado o cerrado de forma prematura.
			//Log.msg("¡¡EXCEPCION!! Entrada por consola no abierta o ya cerrada.",Log.EXCEPTION);
		}
	}
	
}
