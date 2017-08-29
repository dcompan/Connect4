package org.ia.core;

/**
 * Clase que manja el Log del programa.
 *
 * @author Daniel Compan Lopez de Lacalle
 */
public class Log {
	
	/* Constantes para definir los tipos de cadenas que se le pasan al log. */
	public static final int EXCEPTION=-2;
	public static final int ERROR=-1;
	public static final int WARNING=0;
	public static final int INFO=1;
	public static final int CONSOLE=2;
	
	/**
	 * Envia un mensaje a la salida de log.
	 * @param str Cadena con el mensaje a enviar.
	 * @param type Tipo de mensaje.
	 */
	public static void msg(String str, int type){
		System.out.println(str);
	}
	
}
