package org.ia.core;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Clase guarda la información asociada a una regla.
 * Una regla tiene la estructura: sequence [direction] value
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public class Rule {
	
	/** Si se va a empaquetar en un archivo .JAR ha de ponerse a <code>true</code> */
	public static final boolean TOJAR=false;
	
	/** Estados de la regla */
	public static final int ERROR=-1;
	public static final int NONE=0;
	public static final int BLANK=1;
	public static final int COMMENT=2;
	public static final int VALID=3;
	
	/** Numero de linea de la regla en el archivo. */
	private int lineNumber;
	/** Secuencia de elementos a comparar. */
	private SequenceRule sequence;
	/** Secuencia de elementos invertida. */
	private SequenceRule reversedSequence;
	/** Direcciones a comparar [0]=H , [1]=V , [2]=D . */
	private boolean[] directions;
	/** Valor de la regla. */
	private Value value;
	/** Comentario de la regla. */
	private String comment;
	/** Estado de la regla. */
	private int state;
	/** Mensaje de error, en caso de que haya ocurrido alguno. */
	private String error;
	
	/**
	 * Inicializa los valores de las variables de clase.
	 */
	private void init(){
		this.lineNumber=0;
		this.sequence=null;
		this.reversedSequence=null;
		this.directions=new boolean[3];
		directions[0]=false;
		directions[1]=false;
		directions[2]=false;
		this.value=new Value();
		this.comment=null;
		this.state=NONE;
		this.error=null;
	}
	
	/**
	 * Constructor de la clase a partir de los componentes basicos de la regla introducidos como cadenas.
	 * @param seq Cadena con la secuencia de busqueda.
	 * @param dir Cadena con la direccion.
	 * @param val Cadena con el valor.
	 */
	Rule(String seq, String dir, String val){
		init();
		boolean isAllCorrect=true;
		if(!setSequence(seq)) isAllCorrect=false;
		if(!setDirections(dir)) isAllCorrect=false;
		if(!setValue(val)) isAllCorrect=false;
		if(isAllCorrect) this.state=VALID;
	}
	
	/**
	 * Constructor de la clase a partir de una linea de texto del archivo de reglas.
	 * @param line Linea de texto del archivo de reglas.
	 */
	public Rule(String line){
		init();
		if(line==null || line.length()==0){
			setError("Línea de texto vacía.");
		}
		else{
			char c='\0';
			int i=0;
			int s=AFD.initialState();
			String seq="";
			String dir="";
			String val="";
			while(i<line.length() && (AFD.isTransitionState(s) || AFD.isInitialState(s))){
				c=line.charAt(i);
				s=AFD.next(s,c);
				switch(s){
					case 2:
						seq=seq+c;
						break;
					case 4:
						dir=dir+c;
						break;
					case 6:
						val=val+c;
						break;
					case 7:
						val=val+c;
						break;
					case 8:
						val=val+c;
						break;
					case 9:
						val=val+c;
						break;
					case 10:
						val=val+c;
						break;
					case 11:
						val=val+c;
						break;
					case 12:
						val=val+c;
						break;
				}
				i++;
			}
			if(AFD.isFinalState(s)){
				if(s==AFD.BLANK_LINE_ACCEPTED){ state=BLANK; }
				else if(s==AFD.COMMENT_ACCEPTED){
					comment=AFD.getComment(line);
					state=COMMENT;
				}
				else{
					comment=AFD.getComment(line);
					boolean isAllCorrect=true;
					if(!setSequence(seq)) isAllCorrect=false;
					if(!setDirections(dir)) isAllCorrect=false;
					if(!setValue(val)) isAllCorrect=false;
					if(isAllCorrect) this.state=VALID;
				}
			}
			else{
				if(AFD.isErrorState(s)){
					if(c=='\n') setError("Error (pos "+i+" '\\n'): "+AFD.getErrorMessage(s));
					else setError("Error (pos "+i+" '"+c+"'): "+AFD.getErrorMessage(s));
				}
				else setError("Lectura incompleta (interrumpida en el estado"+s+").");
			}
		}
	}
	
	/**
	 * Lee un archivo de reglas y devuelve su contenido en una lista de reglas.
	 * @param rulesFileName Nombre del archivo de reglas.
	 * @param rulesFilePath Directorio del archivo de reglas.
	 * @return Lista de reglas del archivo. <code>null</code> si ha habido un error.
	 */
	public static ArrayList<Rule> openRulesFile(String rulesFileName, String rulesFilePath){
        int nLines=0;
		String pf;
		File f=null;
		FileReader fr=null;
		BufferedReader br=null;
        String line;
        Rule r;
        ArrayList<Rule> ruleList=null;
		if(rulesFileName==null || rulesFileName.length()==0){
			Log.msg("¡¡EXCEPCION!! No se pudo abrir el archivo. No hay nombre de archivo.",Log.EXCEPTION);
			return null;
		}
		pf=rulesFileName;
		if(!TOJAR){
			if(rulesFilePath!=null && rulesFilePath.length()!=0) pf=rulesFilePath+"\\"+rulesFileName;
		}
		else{
			if(rulesFilePath!=null && rulesFilePath.length()!=0) pf=rulesFilePath+"/"+rulesFileName;
			pf="/"+pf;
		}
		if(!TOJAR){
			f=new File(pf);
			if(!f.exists() || !f.isFile()){
				Log.msg("¡¡EXCEPCION!! No se pudo abrir el archivo \""+rulesFileName+"\". No se encuentra. ",Log.EXCEPTION);
				return null;
			}
		}
		try{
			ruleList=new ArrayList<Rule>();
			if(!TOJAR){
				fr=new FileReader(f);
				br=new BufferedReader(fr);
			}
			else br=new BufferedReader(new InputStreamReader(Rule.class.getResourceAsStream(pf)));
			line=br.readLine();
			if(line==null){
				Log.msg("¡¡EXCEPCION!! Fallo al leer el archivo \""+rulesFileName+"\". Archivo vacio.",Log.EXCEPTION);
				return null;
			}
			while(line!=null){
				nLines=nLines+1;
				line=line+"\n";
				r=new Rule(line);
				r.setLineNumber(nLines);
				ruleList.add(r);
				line=br.readLine();
			}
		}catch(Exception e){
			Log.msg("¡¡EXCEPCION!! Fallo al leer el archivo \""+rulesFileName+"\". "+e.getMessage(),Log.EXCEPTION);
			return null;
		}finally{
	         try{
	        	if(br!=null) br.close();
	            if(fr!=null) fr.close();                  
	         }catch(Exception e2){ 
	        	 Log.msg("Fallo al cerrar el archivo \""+rulesFileName+"\". "+e2.getMessage(),Log.EXCEPTION);
	        	 return null;
	         }
	    }
		return ruleList;
	}
	
	/**
	 * Comprueba una lista de reglas.
	 * Se considera correcta si no hay reglas con errores y, al menos, hay una regla valida.
	 * @param ruleList Lista de reglas.
	 * @param withLog Si se quiere guardar registro de los posibles errores.
	 * @return <code>true</code> si la lista de reglas es correcta. <code>false</code> si no.
	 */
	public static boolean checkRules(ArrayList<Rule> ruleList, boolean withLog){
		if(ruleList==null || ruleList.isEmpty()){
			if(withLog) Log.msg("ERROR: No hay reglas.",Log.ERROR);
			return false;
		}
        int ruleState;
        int nErrorRules=0;
        int nValidRules=0;
        String error=null;
		Iterator<Rule> iter=ruleList.iterator();
		while(iter.hasNext()){
			Rule r=iter.next();
			ruleState=r.getState();
			if(ruleState==Rule.ERROR) nErrorRules=nErrorRules+1;
			if(ruleState==Rule.VALID) nValidRules=nValidRules+1;
		}
		if(nErrorRules>0){
			if(nErrorRules==1) error="Hay 1 regla con error.";
			else error="Hay "+nErrorRules+" reglas con error.";
		}
		else{
			if(nValidRules==0) error="No hay reglas validas.";
		}
		if(error!=null){
			if(withLog) Log.msg(error,Log.ERROR);
			return false;
		}
		else return true;		
	}
	
	/**
	 * Establece el numero de linea de la regla en el archivo de reglas.
	 * @param ln Numero de linea.
	 */
	public void setLineNumber(int ln){
		this.lineNumber=ln;
	}
	
	/**
	 * Establece la secuencia de busqueda de la regla.
	 * Si la secuencia es correcta tambien se genera y se establece la secuencia inversa.
	 * @param seq Cadena con la secuencia de busqueda.
	 * @return <code>true</code> si la secuencia se ha podido establecer (es correcta).
	 */
	private boolean setSequence(String seq){
		if(checkSequence(seq)){
			this.sequence=new SequenceRule(seq);
			this.reversedSequence=new SequenceRule(reverseString(seq));
			return true;
		}
		else return false;
	}
	
	/**
	 * Comprueba que la secuencia esta bien escrita.
	 * @return <code>true</code> si la sentencia esta bien escrita.
	 */
	private boolean checkSequence(String seq){
		int sharps=0;
		int equals=0;
		char c;
		if(seq==null || seq.length()==0){
			setError("Secuencia vacía.");
			return false;
		}
		for(int i=0;i<seq.length();i++){
			c=seq.charAt(i);
			if(c=='#'){ sharps=sharps+1; }
			else if(c=='='){ equals=equals+1; }
			else if(c!='O' && c!='X' && c!='_' && c!='z' && c!='I' && c!='E' && c!='?'){
				setError("Elemento inesperado en la secuencia ('"+c+"').");
				return false;
			}
		}
		if(sharps==0 && equals==0){
			setError("En la secuencia no se ha encontrado ningun elemento de casilla vacia jugable elegida (#/=).");
			return false;
		}
		if(sharps>0 && equals>0){
			setError("En la secuencia se han encontrado dos tipos distintos de elementos de casilla vacia jugable elegida (#/=).");
			return false;
		}
		if(sharps>1){
			setError("En la secuencia se han encontrado más de un elemento de casilla vacia jugable elegida (#).");
			return false;
		}
		if(equals>1){
			setError("En la secuencia se han encontrado más de un elemento de nueva casilla vacia a jugable elegida (=).");
			return false;
		}
		return true;
	}
	
	/**
	 * Invierte una cadena (ejemplo "abc"->"cba").
	 * @param str Cadena a invertir.
	 * @return Cadena invertida.
	 */
	public static String reverseString(String str){
		if(str==null) return null;
		String invStr="";
		for(int i=0;i<str.length();i++) invStr=str.charAt(i)+invStr;
		return invStr;
	}
	
	/**
	 * Establece los especificadores de direccion a partir de una cadena con solo esa parte de la regla.
	 * Si una cadena vacia (null/"") se presupone equivalente a "VHD".
	 * @param dir Cadena con la direccion tomada de la linea del archivo de reglas.
	 * @return <code>true</code> si se ha establecido correctamente.
	 */
	private boolean setDirections(String dir){
		char c;
		if(dir==null || dir.length()==0){
			directions[0]=true;
			directions[1]=true;
			directions[2]=true;
			return true;
		}
		for(int i=0;i<dir.length();i++){
			c=dir.charAt(i);
			if(c=='H'){ directions[0]=true; }
			else if(c=='V'){ directions[1]=true; }
			else if(c=='D'){ directions[2]=true; }
			else{
				setError("Caracter no valido en la secuencia de direcciones ("+c+").");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Establece el valor de la regla a partir de una cadena con solo esa parte de la regla.
	 * @param val Cadena con el valor tomado de la linea del archivo de reglas.
	 * @return <code>true</code> si se ha establecido correctamente.
	 */
	private boolean setValue(String val){
		if(val==null || val.length()==0){
			setError("Valor vacío.");
			return false;
		}
		if(AFD.isMax(val) || AFD.isMin(val) || AFD.isInteger(val)){
			value.set(val);
			return true;
		}
		setError("El valor no es ni un numero entero ni un limite (max/min).");
		return false;
	}
	
	/**
	 * Establece un mensaje de error y pone el estado de la regla a "ERROR".
	 * @param error Cadena con el mensaje de error.
	 * @return <code>true</false> si se ha podido establecer. <code>false</code> si no se ha hecho porque ya habia otro anterior.
	 */
	private boolean setError(String error){
		if(this.error==null){
			this.error=error;
			this.state=ERROR;
			return true;
		}
		return false;
	}
	
	/**
	 * Devuelve el numero de linea de la regla en el archivo de reglas.
	 * @return Numero de linea.
	 */
	public int getLineNumber(){
		return this.lineNumber;
	}
	
	/**
	 * Devuelve si una regla es o no valida.
	 * @return <code>true</code> si la regla es valida.
	 */
	public boolean isValid(){
		if(state==VALID) return true;
		else return false;
	}
	
	/**
	 * Devuelve el estado de la regla.
	 * @return Estado.
	 */
	public int getState(){
		return this.state;
	}
	
	/**
	 * Devuelve el mensaje de error.
	 * @return Cadena con el mensaje de error. <code>null</code> si no ha habido error.
	 */
	public String getError(){
		return this.error;
	}
	
	/**
	 * Comprueba si la secuencia de la regla tiene un elemento de nueva casilla vacia jugable (=), en cuyo caso la regla hace referencia a la situacion despues de haber movido.
	 * @return <code>true</code> si la regla es para evaluar la jugada del oponente. <code>false</code> si es una regla normal que evalua nuestra jugada.
	 */
	public boolean isNextMoveRule(){
		if(sequence.getSeq().charAt(sequence.getSincPos())=='=') return true;
		else return false;
	}
	
	/**
	 * Comprueba si la regla tiene el especificador de direccion horizontal (H).
	 * @return <code>true</code> si la regla debe evaluarse en horizontal.
	 */
	public boolean isH(){
		return this.directions[0];
	}
	
	/**
	 * Comprueba si la regla tiene el especificador de direccion vertical (V).
	 * @return <code>true</code> si la regla debe evaluarse en vertical.
	 */
	public boolean isV(){
		return this.directions[1];
	}
	
	/**
	 * Comprueba si la regla tiene el especificador de direccion diagonal (D).
	 * @return <code>true</code> si la regla debe evaluarse en diagonal.
	 */
	public boolean isD(){
		return this.directions[2];
	}
	
	/**
	 *  Cambia las fichas de la secuencia, 'O' por 'X' y viveversa.
	 */
	public void changePieces(){
		if(sequence!=null) sequence.changePieces();
		if(reversedSequence!=null) reversedSequence.changePieces();
	}
	
	/**
	 *  Cambia los elementos de fichas en la secuencia ('O' por 'X' y viveversa) en todas las reglas.
	 */
	public static void changePieces(ArrayList<Rule> ruleList){
		if(ruleList!=null && !ruleList.isEmpty()){
			Iterator<Rule> iter=ruleList.iterator();
			while(iter.hasNext()){
				Rule r=iter.next();
				r.changePieces();
			}
		}
	}
	
	/**
	 * Comprueba si la secuencia de busqueda y la secuencia invertida, coinciden con una secuencia de prueba.
	 * Si coinciden (una o ambas) se devuelve la puntuacion parcial obtenida que ha de sumarse a la puntuacion total de la regla.
	 * @param trySeq Secuencia de prueba.
	 * @return Puntuacion parcial obtenida que ha de sumarse a la total de la columna. 0 si no hay coincidencia.
	 */
	public Value getMatchValue(Sequence trySeq){
		Value v=new Value(0L);
		if(sequence.match(trySeq)) v.set(value);
		if(sequence.getLength()>1){
			if(reversedSequence.match(trySeq)) v.sum(value);
		}
		return v;
	}
	
	/**
	 * Comprueba si hay coincidencias con un array de secuencias de prueba de una columna.
	 * El indice del array que ha de pasarse ha de corresponder con la direccion de la secuencia de prueba: [0]=H , [1]=V , [2]=Da , [3]=Dd .
	 * Solo se comprueban las direcciones especificadas en la regla.
	 * @param trySequences Array con las 4 secuencias de prueba de una columna.
	 * @return Puntuacion parcial obtenida que ha de sumarse a la puntuacion total de la columna. 0 cuando no haya coincidencia.
	 */
	public Value getMatchValue(Sequence[] trySequences){
		Value v=new Value(0L);
		if(trySequences!=null && trySequences.length==4){
			if(isH()) v.sum(getMatchValue(trySequences[0]));
			if(isV()) v.sum(getMatchValue(trySequences[1]));
			if(isD()){
				v.sum(getMatchValue(trySequences[2]));
				v.sum(getMatchValue(trySequences[3]));
			}
		}
		return v;
	}
	
	/**
	 * Comprueba si hay coincidencias con una matriz de secuencias de prueba de la BCA.
	 * El primer indice de la matriz que ha de pasarse ha de ser el correspondiente a la columna [0~6].
	 * El segundo indice de la matriz que ha de pasarse ha de corresponder con la direccion de la secuencia de prueba: [0]=H , [1]=V , [2]=Da , [3]=Dd .
	 * Solo se comprueban las direcciones especificadas en la regla.
	 * @param trySequences Matriz con las sentencias de prueba de la BCA.
	 * @return Array con las puntuaciones parciales obtenidas para cada columna, que han de sumarse a las del resto de reglas. 0 cuando no haya coincidencia.
	 */
	public Value[] getMatchValues(Sequence[][] trySequences){
		Value[] values=null;
		if(trySequences!=null && trySequences.length!=0){
			values=new Value[trySequences.length];
			for(int c=0;c<trySequences.length;c++){
				values[c]=getMatchValue(trySequences[c]);
			}
		}
		return values;
	}
	
	/**
	 * Devuelve una cadena con el contenido de la Regla.
	 */
	@Override
	public String toString(){
		String str="";
		if(!isValid()){
			switch(state){
				case ERROR:
					str=error;
					break;
				case NONE:
					str="Regla no inicializada.";
					break;
				case BLANK:
					//str="Linea en blanco.";
					break;
				case COMMENT:
					str=comment;
					break;
				default:
					str="Regla no valida.";
					break;
			}
		}
		else{
			if(sequence==null) str=str+"null ";
			else str=str+sequence.getSeq()+" ";
			if(directions[0]==true) str=str+"H";
			if(directions[1]==true) str=str+"V";
			if(directions[2]==true) str=str+"D";
			str=str+" "+value.toStringForRules();
			if(comment!=null) str=str+" "+comment;
		}
		return str;
	}
	
	/**
	 * Imprime por consola una lista de reglas.
	 * @param ruleList Lista de reglas a imprimir.
	 */
	public static void printRules(ArrayList<Rule> ruleList){
		if(ruleList==null || ruleList.isEmpty()) Log.msg("ERROR: No hay reglas.",Log.ERROR);
		else{
			Iterator<Rule> iter=ruleList.iterator();
			while(iter.hasNext()){
				Rule r=iter.next();
				Log.msg("["+r.getLineNumber()+"] "+r.toString(),Log.INFO);
			}
		}
	}
	
}
