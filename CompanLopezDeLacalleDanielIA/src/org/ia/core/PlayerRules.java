package org.ia.core;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Clase que representa un jugador que mueve basandose en unas reglas tomadas de un archivo de texto.
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public class PlayerRules extends Player {
	
	/** Nivel maximo del jugador. */
	public static final int MAX_LEVEL=3;
	/** Intervalos de puntuacion para el nivel 1. */
	public static final long[] LEVEL1_INTERVALS={-2000L,-1500L,-1000L,-500L,-300L,-100L,-50L,-20L,0L,20L,50L,100L,300L,500L,1000L,1500L,2000L};
	/** Intervalos de puntuacion para el nivel 2. */
	public static final long[] LEVEL2_INTERVALS={-1000L,-900L,-800L,-700L,-600L,-500L,-400L,-300L,-200L,-100L,-90L,-80L,-70L,-60L,-50L,-40L,-30L,-20L,-10L,0L,10L,20L,30L,40L,50L,60L,70L,80L,90L,100L,200L,300L,400L,500L,600L,700L,800L,900L,1000L};
	/** Nombre por defecto del archivo de reglas. */
	static final String DEF_RULES_FILE_NAME="rules.txt";
	/** Ruta por defecto donde se debe buscar el archivo de reglas. */
	static final String DEF_RULES_FILE_PATH="recursos";
	
	/** Nombre del archivo de reglas. **/
	private String rulesFileName;
	/** Directorio del archivo de reglas. **/
	private String rulesFilePath;
	/** Lista de reglas tomadas del archivo de reglas. */
	ArrayList<Rule> rules;
	/** Indica si se quiere mostrar informacion por pantalla */
	private boolean showInfo;
		
	/**
	 * Constructor de la clase.
	 * Crea un nuevo jugador random.
	 * ¡Atencion! Despues de crearlo hay que, opcionalmente, asignarle un archivo de reglas choseRulesFile() y, obligatoriamente, abrirlo openRulesFile().
	 * @param bcaBoard Tablero avanzado (Base de Conocimiento Aumentada).
	 * @param piece Ficha del jugador ('O' o 'X').
	 */
	public PlayerRules(BCA bcaBoard, char piece){
		this.name="Ruler";
		setType(RULES_PLAYER);
		setPiece(piece);
		setLevel(MAX_LEVEL);
		this.isReady=false;
		this.board=bcaBoard;
		this.rulesFileName=null;
		this.rulesFilePath=null;
		this.rules=null;
		this.showInfo=true;
	}
	
	/**
	 * Establece si el jugador esta listo para jugar.
	 * @param withLog Si se quiere guardar registro de los posibles errores.
	 * @return <code>true</code> si esta listo. <code>false</code> si no.
	 */
	private boolean setIsReady(boolean withLog){
		if(Rule.checkRules(rules,withLog) && super.setIsReady()) isReady=true;
		else isReady=false;
		return isReady;
	}
	@Override
	public boolean setIsReady(){ return setIsReady(false); }
	
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
	 * Establece si se debe mostrar informacion por consola de las jugadas.
	 * @param si <code>true</code> si se quiere mostrar informacion de las jugadas.
	 */
	public void setShowInfo(boolean si){
		this.showInfo=si;
	}
	
	/**
	 * Establece el archivo de texto del que se tomaran las reglas.
	 * @param path Ruta del archivo.
	 * @param fileName Nombre del archivo de reglas.
	 */
	public void choseRulesFile(String path, String fileName){
		this.rulesFileName=fileName;
		this.rulesFilePath=path;
	}
	
	/**
	 * Lee el archivo de reglas.
	 * @return <code>true</code> si se ha podido abrir. <code>false</code> si no.
	 */
	public boolean openRulesFile(){
		if(rulesFileName==null || rulesFileName.length()==0) rules=Rule.openRulesFile(DEF_RULES_FILE_NAME,DEF_RULES_FILE_PATH);
		else rules=Rule.openRulesFile(rulesFileName,rulesFilePath);
		if(rules!=null){
			if(showInfo) Log.msg("Archivo de reglas \""+rulesFileName+"\" ("+rules.size()+" líneas) leido.",Log.INFO);
			if(setIsReady(true)){
				if(getPiece()=='X'){
					if(showInfo) Log.msg("Elementos de ficha propia (O) y ficha del oponente (X) intercambiados en las reglas.",Log.WARNING);
					Rule.changePieces(rules);
				}
				return true;
			}
			else Rule.printRules(rules);
		}
		return false;
	}
		
	/**
	 * Toma un array de Valores y a aquellos que esten dentro de un intervalo (definido por otro array de Valores) les asigna el menor Valor del intervalo:
	 * OUT <intervals[0]<= ... <intervals[1]<= [...] <intervals[n-2]<= ... <intervals[n-1]<= OUT     
	 * @param scoring Array de Valores a comprobar y modificar.
	 * @param intervals Array de Valores que define los intervalos. Ha de tener, al menos, un intervalo (length>=2) y estar ordenado de forma estrictamente creciente (sin valores repetidos).
	 * @return <code>true</code> si la operacion se ha realizado (se ha modificado agun valor). <code>false</code> si no.
	 */
	public static boolean blurScore(Value[] scoring, Value[] intervals){
		if(scoring==null || scoring.length==0) return false;
		if(intervals==null || intervals.length<2) return false;
		for(int i=1;i<intervals.length;i++){
			if(Value.greaterEqual(intervals[i-1],intervals[i])) return false;
		}
		boolean test=false;
		for(int c=0;c<scoring.length;c++){
			if(Value.greaterEqual(scoring[c],intervals[0]) && Value.lessThan(scoring[c],intervals[intervals.length-1])){
				for(int i=1;i<intervals.length;i++){
					if(Value.lessThan(scoring[c],intervals[i])){
						scoring[c].set(intervals[i-1]);
						test=true;
						break;
					}
				}
			}
		}
		return test;
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
		char piece=getPiece();
		// Evaluar reglas
		Value[] scoring=new Value[BCA.COLS-2];
		for(int c=0;c<scoring.length;c++){
			if(bca.isFull(c+1)) scoring[c]=new Value();
			else scoring[c]=new Value(0L);
		}
		Value[] intervals=null;
		if(getLevel()==1){ intervals=Value.getArrayValues(LEVEL1_INTERVALS); }
		else if(getLevel()==2){ intervals=Value.getArrayValues(LEVEL2_INTERVALS); }
		Sequence[][] trySequences=bca.getTrySequences();
		Sequence[][] nextMoveTrySequences=bca.getNextMoveTrySequences(piece);
		Iterator<Rule> iter=rules.iterator();
		while(iter.hasNext()){
			Rule r=iter.next();
			if(r.getState()==Rule.VALID){
				Value[] ruleVals;
				if(r.isNextMoveRule()) ruleVals=r.getMatchValues(nextMoveTrySequences);
				else ruleVals=r.getMatchValues(trySequences);
				for(int c=0;c<Integer.min(scoring.length,ruleVals.length);c++) scoring[c]=Value.sum(scoring[c],ruleVals[c]); 
			}
		}
		blurScore(scoring,intervals);
		Value maxValue=Value.getMaxValue(scoring);
		if(showInfo){
			String str="PUNTUACIONES:";
			for(int c=0;c<scoring.length;c++) str=str+"\n Columna "+(c+1)+": "+scoring[c];
			Log.msg(str,Log.INFO);
			Log.msg("Puntuacion mas alta: "+maxValue,Log.INFO);
		}
		return Value.getIndexOf(scoring,maxValue)+1;
	}
	
}
