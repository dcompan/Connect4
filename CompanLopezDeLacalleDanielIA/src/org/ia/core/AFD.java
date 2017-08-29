package org.ia.core;

/**
 * Clase que implementa el Automata Finito Determinista (AFD) que comprueba si una línea del archivo de reglas esta bien escrita.
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public class AFD {
	
	/** Tipos de estados finales */
	public static final int RULE_ACCEPTED=17;
	public static final int COMMENT_ACCEPTED=21;
	public static final int BLANK_LINE_ACCEPTED=22;
	
	/**
	 * Devuelve el estado inicial.
	 * @return Numero del estado inicial.
	 */
	public static int initialState(){
		return 1;
	}
	
	/**
	 * Comprueba si un estado es el estado inicial.
	 * @param state Numero del estado a comprobar.
	 * @return <code>true</code> si es el estado inicial. 
	 */
	public static boolean isInitialState(int state){
		if(state==1) return true;
		return false;
	}
	
	/**
	 * Comprueba si un estado es un estado de transicion (estados validos exceptuando el inicial y el final).
	 * @param state Numero del estado a comprobar.
	 * @return <code>true</code> si es un estado de transcion. 
	 */
	public static boolean isTransitionState(int state){
		if(state>1 && state<21){
			if(state==17) return false;
			return true;
		}
		return false;
	}
	
	/**
	 * Comprueba si un estado es un estado final.
	 * @param state Numero del estado a comprobar.
	 * @return <code>true</code> si es un estado final. 
	 */
	public static boolean isFinalState(int state){
		if(state==17 || state==21 || state==22) return true;
		return false;
	}
	
	/**
	 * Comprueba si un estado es un estado de error.
	 * @param state Numero del estado a comprobar.
	 * @return <code>true</code> si es un estado de error. 
	 */
	public static boolean isErrorState(int state){
		if(state<=0 || state>22) return true;
		return false;
	}
	
	/**
	 * Usa una parte del AFD para comprobar si una cadena tiene unicamente un digito.
	 * @param str Cadena a comprobar.
	 * @return <code>true</code> si la cadena es MIN.
	 */
	public static boolean isDigit(String str){
		if(str==null || str.length()!=1) return false;
		if(next(6,str.charAt(0))==7) return true;
		return false;
	}
	
	/**
	 * Usa una parte del AFD para comprobar si una cadena es un numero natural (sin signo, solo digitos).
	 * @param str Cadena a comprobar.
	 * @return <code>true</code> si la cadena es un numero natural.
	 */
	public static boolean isNatural(String str){
		if(str==null || str.length()==0) return false;
		for(int i=0;i<str.length();i++){
			if(next(6,str.charAt(i))!=7) return false;
		}
		return true;
	}
	
	/**
	 * Usa una parte del AFD para comprobar si una cadena es un numero entero (con o sin signo).
	 * @param str Cadena a comprobar.
	 * @return <code>true</code> si la cadena es un numero natural.
	 */
	public static boolean isInteger(String str){
		if(str==null || str.length()==0) return false;
		if(next(5,str.charAt(0))!=6 && next(5,str.charAt(0))!=7) return false;
		for(int i=1;i<str.length();i++){
			if(next(6,str.charAt(i))!=7) return false;
		}
		return true;
	}
	
	/**
	 * Usa una parte del AFD para comprobar si una cadena es el limite MAX.
	 * @param str Cadena a comparar.
	 * @return <code>true</code> si la cadena es MAX.
	 */
	public static boolean isMax(String str){
		if(str==null || str.length()!=3) return false;
		if(next(5,str.charAt(0))==8 && next(8,str.charAt(1))==9 && next(9,str.charAt(2))==10) return true;
		return false;
	}
	
	/**
	 * Usa una parte del AFD para comprobar si una cadena es el limite MIN.
	 * @param str Cadena a comparar.
	 * @return <code>true</code> si la cadena es MIN.
	 */
	public static boolean isMin(String str){
		if(str==null || str.length()!=3) return false;
		if(next(5,str.charAt(0))==8 && next(8,str.charAt(1))==11 && next(11,str.charAt(2))==12) return true;
		return false;
	}
	
	/**
	 * Devuelve el siguiente estado.
	 * @param state Numero del estado actual.
	 * @param c Simbolo de entrada.
	 * @return Numero del estado siguiente. En caso de error es <0.
	 */
	public static int next(int state, char c){
		switch(state){
			case 1:
				if(c==' ') return 1;
				if(c=='\t') return 1;
				if(c=='O') return 2;
				if(c=='X') return 2;
				if(c=='_') return 2;
				if(c=='z') return 2;
				if(c=='O') return 2;
				if(c=='I') return 2;
				if(c=='E') return 2;
				if(c=='#') return 2;
				if(c=='=') return 2;
				if(c=='?') return 2;
				if(c=='/') return 18;
				if(c=='\n') return 22;
				return -1;
			case 2:
				if(c=='O') return 2;
				if(c=='X') return 2;
				if(c=='_') return 2;
				if(c=='z') return 2;
				if(c=='O') return 2;
				if(c=='I') return 2;
				if(c=='E') return 2;
				if(c=='#') return 2;
				if(c=='=') return 2;
				if(c=='?') return 2;
				if(c==' ') return 3;
				return -2;
			case 3:
				if(c==' ') return 3;
				if(c=='\t') return 3;
				if(c=='H') return 4;
				if(c=='V') return 4;
				if(c=='D') return 4;
				if(c=='+') return 6;
				if(c=='-') return 6;
				if(c=='0') return 7;
				if(c=='1') return 7;
				if(c=='2') return 7;
				if(c=='3') return 7;
				if(c=='4') return 7;
				if(c=='5') return 7;
				if(c=='6') return 7;
				if(c=='7') return 7;
				if(c=='8') return 7;
				if(c=='9') return 7;
				if(c=='M') return 8;
				if(c=='m') return 8;
				return -3;
			case 4:
				if(c==' ') return 5;
				if(c=='\t') return 5;
				if(c=='H') return 4;
				if(c=='V') return 4;
				if(c=='D') return 4;
				return -4;
			case 5:
				if(c==' ') return 5;
				if(c=='\t') return 5;
				if(c=='+') return 6;
				if(c=='-') return 6;
				if(c=='0') return 7;
				if(c=='1') return 7;
				if(c=='2') return 7;
				if(c=='3') return 7;
				if(c=='4') return 7;
				if(c=='5') return 7;
				if(c=='6') return 7;
				if(c=='7') return 7;
				if(c=='8') return 7;
				if(c=='9') return 7;
				if(c=='M') return 8;
				if(c=='m') return 8;
				return -5;
			case 6:
				if(c=='0') return 7;
				if(c=='1') return 7;
				if(c=='2') return 7;
				if(c=='3') return 7;
				if(c=='4') return 7;
				if(c=='5') return 7;
				if(c=='6') return 7;
				if(c=='7') return 7;
				if(c=='8') return 7;
				if(c=='9') return 7;
				return -6;
			case 7:
				if(c==' ') return 13;
				if(c=='\t') return 13;
				if(c=='0') return 7;
				if(c=='1') return 7;
				if(c=='2') return 7;
				if(c=='3') return 7;
				if(c=='4') return 7;
				if(c=='5') return 7;
				if(c=='6') return 7;
				if(c=='7') return 7;
				if(c=='8') return 7;
				if(c=='9') return 7;
				if(c=='\n') return 17;
				return -7;
			case 8:
				if(c=='A') return 9;
				if(c=='a') return 9;
				if(c=='I') return 11;
				if(c=='i') return 11;
				return -8;
			case 9:
				if(c=='X') return 10;
				if(c=='x') return 10;
				return -9;
			case 10:
				if(c==' ') return 13;
				if(c=='\t') return 13;
				if(c=='\n') return 17;
				return -10;
			case 11:
				if(c=='N') return 12;
				if(c=='n') return 12;
				return -11;
			case 12:
				if(c==' ') return 13;
				if(c=='\t') return 13;
				if(c=='\n') return 17;
				return -12;
			case 13:
				if(c==' ') return 13;
				if(c=='\t') return 13;
				if(c=='/') return 14;
				if(c=='\n') return 17;
				return -13;
			case 14:
				if(c=='/') return 15;
				return -14;
			case 15:
				if(c=='\n') return 17;
				else return 16;
			case 16:
				if(c=='\n') return 17;
				else return 16;
			case 17:
				return -17;
			case 18:
				if(c=='/') return 19;
				return -18;
			case 19:
				if(c=='\n') return 21;
				else return 20;
			case 20:
				if(c=='\n') return 21;
				else return 20;
			case 21:
				return -21;
			case 22:
				return -22;
			default:
				if(state<0) return state;
				return 0;
		}
	}
	
	/**
	 * Devuelve un mensaje de error para un estado de error.
	 * @param errorState Numero del estado de error.
	 * @return Cadena con el mensaje de error.
	 */
	public static String getErrorMessage(int errorState){
		switch(errorState){
			case 0:
				return "Estado desconocido.";
			case -1:
				return "Se esperaba un elemento de secuencia (O , X , _ , z , I , E , # , = , ?), espacio en blanco o comentario (//...).";
			case -2:
				return "Se esperaba un elemento de secuencia (O , X , _ , z , I , E , # , = , ?) o un espacio en blanco.";
			case -3:
				return "Se esperaba especificador de dirección (D , H , V), número entero o límite (max/min).";
			case -4:
				return "Se esperaba especificador de dirección (D , H , V), número entero o límite (max/min).";
			case -5:
				return "Se esperaba número entero o límite (max/min).";
			case -6:
				return "Se esperaba número entero.";
			case -7:
				return "Se esperaba un dígito, espacio en blanco o fin de línea.";
			case -8:
				return "Se esperaba un límite (max/min).";
			case -9:
				return "Se esperaba un límite (max/min).";
			case -10:
				return "Se esperaba espacio en blanco o fin de línea.";
			case -11:
				return "Se esperaba un límite (max/min).";
			case -12:
				return "Se esperaba espacio en blanco o fin de línea.";
			case -13:
				return "Se esperaba espacio en blanco, comentario (//...) o fin de línea.";
			case -14:
				return "Se esperaba un comentario (//...).";
			case -17:
				return "Caracteres extras después de que se haya aceptado la cadena (linea de regla).";
			case -18:
				return "Se esperaba un comentario (//...).";
			case -21:
				return "Caracteres extras después de que se haya aceptado la cadena (linea de comentario).";
			case -22:
				return "Caracteres extras después de que se haya aceptado la cadena (linea en blanco).";
			default:
				return "Error no registrado.";
		}
	}
	
	/**
	 * Devuelve una cadena con el comentario (con // y sin '\n').
	 * @param str Cadena de la que se quiere obtener el comentario.
	 * @return Cadena con el comentario. <code>null</code> si no se ha encontrado.
	 */
	public static String getComment(String str){
		if(str==null || str.length()==0) return null;
		int i=0;
		char c;
		int count=0;
		String comment="";
		while(i<str.length()){
			c=str.charAt(i++);
			if(c=='/'){
				count++;
				if(count==2) break;
			}
			else count=0;
		}
		if(count!=2) return null;
		i=i-2;
		while(i<str.length()){
			c=str.charAt(i);
			if(c=='\n') break;
			comment=comment+c;
			i++;
		}
		return comment;
	}
	
}
