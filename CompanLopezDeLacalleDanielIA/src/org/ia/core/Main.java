package org.ia.core;

public class Main {
	
	/**
	 * Clase que empieza una partida por consola.
	 */
	public static void consolePlay(){
		Player p1,p2;
		BC bc=new BC();
		BCA bca=new BCA();
		Console console=new Console();
		Log.msg("IA - 4 en Raya",Log.CONSOLE);
		Log.msg("==============",Log.CONSOLE);
		p1=console.scanNewPlayer(1,'O',bc,bca,console);
		if(p1==null) System.exit(-1);
		if(!p1.isReady()){
			Log.msg("ERROR: Jugador 1 no está listo.",Log.ERROR);
			System.exit(-1);
		}
		Log.msg("Jugador 1 listo.",Log.INFO);
		p2=console.scanNewPlayer(2,'X',bc,bca,console);
		if(p2==null) System.exit(-1);
		if(!p2.isReady()){
			Log.msg("ERROR: Jugador 2 no está listo.",Log.ERROR);
			System.exit(-1);
		}
		Log.msg("Jugador 2 listo.",Log.INFO);
		Game g=new Game(p1,p2);
		do{
			if(!g.play()) break;
			if(console.scanPlayAgain()==1) g.restart();
			else break;
		}while(true);
		console.close();
		Log.msg("Bye!",Log.INFO);
	}
	
	public static void main(String[] args){
		consolePlay();
	}
	
}
