package org.ia.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Clase que implementa el procedimiento Minimax.
 * 
 * @author Daniel Compan Lopez de Lacalle
 */
public class Minimax {
	
	/** Numero de columnas (posibles movimientos) que define el factor de ramificación máximo. */
	public static final int COLS=7;
	/** Profundidad del arbol por defecto. Niveles = (profundidad x 2) + 1. */
	public static final int DEF_DEPTH=1;
	/** Posible valor para el campo <CODE>type</CODE>. Tipo de nodo. */
	public static final int MIN=1;
	/** Posible valor para el campo <CODE>type</CODE>. Tipo de nodo. */
	public static final int MAX=2;
	/** Nombre por defecto del archivo de heuristica. */
	static final String DEF_HEURISTIC_FILE_NAME="heuristic.txt";
	/** Ruta por defecto donde se debe buscar el archivo de heuristica. */
	static final String DEF_HEURISTIC_FILE_PATH="recursos";
	
	/** Tipo de nodo (MIN/MAX). */
	private int type;
	/** Ficha del jugador ('O' o 'X'). */
	private char piece;
	/** Columna en la que se ha de jugar (1~7) para obtener este estado de la BCA. */
	private int column;
	/** Base de Conocimiento Aumentada (BCA). Guarda un posible estado del juego. */
	private BCA bca;
	/** Indica si la BDA es simetrica. En cuyo caso solo se evaluan las columnas 1~4. */
	private boolean isSymmetric;
	/** Referencias a los nodos hijos. Cada hijo se corresponde con una jugada en cada una de las columnas. */
	private Minimax[] sons;
	/** Valor resultantes de la evaluación del nodo. */
	private Value value;
	/** Lista de reglas para evaluar los nodos hoja. */
	private static ArrayList<Rule> rules;
	/** Nivel del nodo actual en el arbol. Raiz=1 , Hoja=2xdepht+1. */
	private int level;
	/** Profundidad del arbol. */
	private static int depth;
	
	/**
	 * Constructor de la clase.
	 * Crea un nodo raiz.
	 * ¡Atencion! Despues de crearlo hay que establecer la heuristica setHeuristic() o loadHeuristic(), expandir el arbol expand()/alphaBeta() y evaluarlo propagateValues().
	 * @param bca Base de Conocimiento Aumentada (BCA) en la que se dispone del estado actual del tablero.
	 * @param piece Ficha del jugador ('O' o 'X').
	 */
	public Minimax(BCA bca, char piece){
		this.type=MAX;
		this.piece=piece;
		this.column=0;
		this.bca=new BCA(bca);
		this.isSymmetric=bca.isSymmetric();
		this.sons=null;
		this.value=null;
		rules=null;
		this.level=1;
		depth=DEF_DEPTH;
	}
	
	/**
	 * Constructor de la clase.
	 * Crea un nuevo nodo hijo.
	 * @param fatherNode Referencia al nodo padre.
	 * @param column Columna en la que se ha de jugar para obtener el estado de este nodo hijo (1~7).
	 */
	private Minimax(Minimax fatherNode, int column){
		if(fatherNode.type==MIN) this.type=MAX;
		else this.type=MIN;
		if(fatherNode.piece=='O') this.piece='X';
		else this.piece='O';
		this.column=column;
		this.bca=new BCA(fatherNode.bca);
		this.bca.put(fatherNode.piece,column);
		this.isSymmetric=bca.isSymmetric();
		this.sons=null;
		this.value=null;
		this.level=fatherNode.level+1;
	}
	
	/**
	 * Establece el Valor del nodo a otro Valor.
	 * @param v Valor al que se desea establecer el valor del nodo.
	 */
	private void setValue(Value v){
		if(v==null) value=null;
		else{
			if(value==null) value=new Value(v);
			else value.set(v);
		}
	}
	
	/**
	 * Establece el Valor del nodo a un entero largo.
	 * @param val Valor al que se desea establecer el valor del nodo.
	 */
	private void setValue(long val){
		if(value==null) value=new Value(val);
		else value.set(val);
	}
	
	/**
	 * Establece el Valor del nodo a +Infinito.
	 */
	private void setValuePosInf(){
		if(value==null) value=new Value();
		value.setPositiveInfinity();
	}
	
	/**
	 * Establece el Valor del nodo a -Infinito.
	 */
	private void setValueNegInf(){
		if(value==null) value=new Value();
		value.setNegativeInfinity();
	}
	
	/**
	 * Establece el Valor del nodo a NaN.
	 */
	private void setValueNaN(){
		if(value==null) value=new Value();
		value.setNaN();
	}
	
	/**
	 * Establece el Valor del nodo al mayor de los introducidos por parametro.
	 * @param a Valor 1.
	 * @param b Valor 2.
	 * @return Referencia al mayor Valor.
	 */
	private Value setValueMax(Value a, Value b){
		if(Value.greaterThan(a,b)){
			setValue(a);
			return a;
		}
		else{
			setValue(b);
			return b;
		}
	}
	
	/**
	 * Establece el Valor del nodo al menor de los introducidos por parametro.
	 * @param a Valor 1.
	 * @param b Valor 2.
	 * @return Referencia al menor Valor.
	 */
	private Value setValueMin(Value a, Value b){
		if(Value.lessThan(a,b)){
			setValue(a);
			return a;
		}
		else{
			setValue(b);
			return b;
		}
	}
	
	/**
	 * Establece el valor de un nodo hoja usando las reglas heuristicas.
	 */
	private void setValueHeuristic(){
		if(rules==null || rules.isEmpty()) setValueNaN();
		else{
			setValue(0L);
			if(bca.areFourConsecutive(column)){
				if(type==MAX) setValueNegInf();
				else setValuePosInf();
			}
			else{
				Value[] scoring=new Value[BCA.COLS-2];
				for(int c=0;c<scoring.length;c++) scoring[c]=new Value(0L);
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
				for(int c=0;c<scoring.length;c++) value.sum(scoring[c]);
			}
		}
	}
	
	/**
	 * Establece la heuristica para evaluar los nodos hoja.
	 * @param heuristicRules Lista de reglas heuristicas.
	 */
	public void setHeuristic(ArrayList<Rule> heuristicRules){
		rules=heuristicRules;
	}
	
	/**
	 * Lee el archivo de reglas heuristicas y las establece.
	 * @param heuristicRulesFileName Nombre del archivo de reglas heuristicas.
	 * @param heuristicRulesFilePath Directorio del archivo de reglas heuristicas.
	 * @return Lista de reglas heuristicas.
	 */
	public ArrayList<Rule> loadHeuristic(String heuristicRulesFileName, String heuristicRulesFilePath){
		rules=Rule.openRulesFile(heuristicRulesFileName,heuristicRulesFilePath);
		if(Rule.checkRules(rules,true)){
			if(piece=='X') Rule.changePieces(rules);
			return rules;
		}
		if(rules!=null && !rules.isEmpty()) Rule.printRules(rules);
		rules=null;
		return null;
	}
	
	/**
	 * Lee el archivo de reglas heuristicas por defecto y las establece.
	 * @return Lista de reglas heuristicas.
	 */
	public ArrayList<Rule> loadHeuristic(){ return loadHeuristic(DEF_HEURISTIC_FILE_NAME,DEF_HEURISTIC_FILE_PATH); }
	
	/**
	 * Establece la profundidad del arbol.
	 * Niveles = (profundidad x 2) + 1.
	 * @param d Profundidad del arbol (>0).
	 */
	public void setDepth(int d){
		if(d>0) depth=d;
	}
	
	/**
	 * Expade el arbol.
	 * Crea los nodos hijos haciendo que estos, a su vez, se expandan y se genere el arbol hasta alcanzar la profundidad indicada con setDepth().
	 */
	public void expand(){
		int maxLevel=(2*depth)+1;
		if(level<maxLevel && !bca.isFull() && !bca.areFourConsecutive(column)){
			sons=new Minimax[COLS];
			boolean[] candidates=new boolean[sons.length];
			for(int c=0;c<sons.length;c++){
				sons[c]=null;
				candidates[c]=!bca.isFull(c+1);
			}
			if(isSymmetric){
				Random random=new Random();
				int mid=sons.length/2;
				for(int c=0;c<mid;c++){
					if(candidates[c]){
						int k=COLS-1-c;
						if(random.nextBoolean()) candidates[c]=false;
						else candidates[k]=false;
					}
				}
			}
			for(int c=0;c<sons.length;c++){
				if(candidates[c]){
					sons[c]=new Minimax(this,c+1);
					sons[c].expand();
				}
			}
		}
	}
	
	/**
	 * Implementa el procedimiento de poda Alfa-Beta.
	 * @param alpha Valor alfa.
	 * @param beta Valor beta.
	 * @return Valor devuelto.
	 */
	private Value alphaBeta(Value alpha, Value beta){
		int maxLevel=(2*depth)+1;
		if(level>=maxLevel || bca.isFull() || bca.areFourConsecutive(column)){
			// Si es un nodo hoja, calcular su valor con una heuristica
			setValueHeuristic();
			return value;
		}
		else{
			// Si es un nodo intermedio
			sons=new Minimax[COLS];
			boolean[] candidates=new boolean[sons.length];
			for(int c=0;c<sons.length;c++){
				sons[c]=null;
				candidates[c]=!bca.isFull(c+1);
			}
			if(isSymmetric){
				Random random=new Random();
				int mid=sons.length/2;
				for(int c=0;c<mid;c++){
					if(candidates[c]){
						int k=COLS-1-c;
						if(random.nextBoolean()) candidates[c]=false;
						else candidates[k]=false;
					}
				}
			}
			if(type==MAX){
				// Si es un nodo MAX
				setValue(alpha);
				for(int c=0;c<sons.length;c++){
					if(candidates[c]){
						sons[c]=new Minimax(this,c+1);
						setValueMax(value,sons[c].alphaBeta(value,beta));
						if(Value.greaterEqual(value,beta)){
							setValue(beta);
							return beta;
						}
					}
				}
				return value;
			}
			else{
				// Si es un nodo MIN
				setValue(beta);
				for(int c=0;c<sons.length;c++){
					if(candidates[c]){
						sons[c]=new Minimax(this,c+1);
						setValueMin(value,sons[c].alphaBeta(alpha,value));
						if(Value.lessEqual(value,alpha)){
							setValue(alpha);
							return alpha;
						}
					}
				}
				return value;
			}
		}
		
	}
	
	/**
	 * Inicia el procedimiento de poda Alfa-Beta.
	 */
	public void alphaBeta(){
		Value alpha=new Value();
		Value beta=new Value();
		alpha.setNegativeInfinity();
		beta.setPositiveInfinity();
		alphaBeta(alpha,beta);
	}
	
	/**
	 * Propaga los valores desde los nodos hoja a la raiz, y devuelve la columna asociada al valor elegido.
	 * @return Columna en la que se sugiere mover (1~7).
	 */
	public int propagateValues(){
		if(sons==null){
			// Si es un nodo hoja, calcular su valor con una heuristica
			setValueHeuristic();
			return 0;
		}
		else{
			setValue(0L);
			// Pedir a los nodos hijos que obtengan sus valores
			for(int i=0;i<sons.length;i++){
				if(sons[i]!=null) sons[i].propagateValues(); 
			}
			// Tomar los valores de los nodos hijos
			int size=0;
			for(int c=0;c<sons.length;c++){
				if(sons[c]!=null) size=size+1;
			}
			if(size==0) return -1;
			Value[] results=new Value[size];
			int[] columns=new int[size];
			int i=0;
			for(int c=0;c<sons.length;c++){
				if(sons[c]!=null){
					results[i]=sons[c].value;
					columns[i]=c+1;
					i++;
				}
			}
			// Elegir y guardar el valor que mas nos conviene
			if(type==MAX) setValue(Value.getMaxValue(results));
			else setValue(Value.getMinValue(results));
			if(value==null || value.isNaN()) return -1;
			// Devolver Columna
			return columns[Value.getIndexOf(results,value)];
		}
	}
	
	/**
	 * Imprime la BCA de este nodo.
	 */
	public void print(){
		bca.show();
		if(column>0) Log.msg(" Columna: "+column,Log.INFO);
		if(value!=null) Log.msg(" Valor: "+value.toString()+"\n",Log.INFO);
		else Log.msg("",Log.INFO);
	}
	
	/**
	 * Imprime todo el arbol.
	 */
	public void printAll(){
		if(level==1){
			Log.msg(" NIVEL 1 ",1);
			Log.msg("---------\n",1);
			print();
		}
		if(sons!=null){
			Log.msg("\n NIVEL "+(level+1),1);
			Log.msg("---------\n",1);
			for(int i=0;i<sons.length;i++){
				if(sons[i]!=null) sons[i].print(); 
			}
			for(int i=0;i<sons.length;i++){
				if(sons[i]!=null) sons[i].printAll(); 
			}
		}
	}
	
}
