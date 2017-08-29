package org.ia.core;

import java.util.Random;

/**
 * Clase que guarda un valor. Puede usarse con las reglas o con el min-max.
 * 
 * @author Daniel Compan Lopez de Lacalle
 */
public class Value {
	
	/** Valor maximo. */
	public static final long MAX=Long.MAX_VALUE;
	/** Valor minimo. */
	public static final long MIN=Long.MIN_VALUE;
	
	/** Valor entero. */
	private long val;
	/** Si es +infinito. En cuyo caso la variable de clase "val" no se tiene en consideracion. */
	private boolean isPositiveInfinity;
	/** Si es -infinito. En cuyo caso la variable de clase "val" no se tiene en consideracion. */
	private boolean isNegativeInfinity;
	/** Si no es un valor valido. */
	private boolean isNaN;
	
	/**
	 * Constructor de la clase.
	 * Crea un nuevo Valor no valido hasta que sea inicializado correctamente.
	 */
	Value(){
		this.val=0;
		this.isPositiveInfinity=false;
		this.isNegativeInfinity=false;
		this.isNaN=true;
	}
	
	/**
	 * Constructor de la clase.
	 * Crea un nuevo Valor y lo inicializa con el entero introducido como parametro.
	 * @param val Entero.
	 */
	Value(int val){
		this();
		set(val);
	}
	
	/**
	 * Constructor de la clase.
	 * Crea un nuevo Valor y lo inicializa con el entero largo introducido como parametro.
	 * @param val Entero largo.
	 */
	Value(long val){
		this();
		set(val);
	}
	

	/**
	 * Constructor de la clase.
	 * Crea un nuevo Valor y lo inicializa con el Valor introducido como parametro.
	 * @param v Valor del mismo tipo.
	 */
	Value(Value v){
		this();
		set(v);
	}
	
	/**
	 * Constructor de la clase.
	 * Crea un nuevo Valor y lo inicializa con la cadena introducida como parametro.
	 * @param val Cadena con un valor entero o un limite (max/min).
	 */
	Value(String val){
		this();
		set(val);
	}
	
	/**
	 * Devuelve un array de Valores con sus elementos no inicializados.
	 * @param size Tamaño del array.
	 * @return Array de Valores no inicializados de tamaño "size".
	 */
	public static Value[] getArrayValues(int size){
		if(size<1) return null;
		Value[] values=new Value[size];
		for(int i=0;i<size;i++){
			values[i]=new Value();
		}
		return values;
	}
	
	/**
	 * Devuelve un array de Valores con sus elementos inicializados al valor introducido por parametro.
	 * @param val Valor de inicializacion para todos los elementos del array.
	 * @param size Tamaño del array.
	 * @return Array de Valores inicializados a "val" de tamaño "size".
	 */
	public static Value[] getArrayValues(int val, int size){
		if(size<1) return null;
		Value[] values=new Value[size];
		for(int i=0;i<size;i++){
			values[i]=new Value(val);
		}
		return values;
	}
	
	/**
	 * Devuelve un array de Valores con sus elementos inicializados a los mismos valores que se han introducido en otro array. 
	 * @param vals Array de enteros con los valores de inicializacion.
	 * @return Array de igual longitud que el introducido por paramero e inicializado a los mismos valores.
	 */
	public static Value[] getArrayValues(int[] vals){
		if(vals==null) return null;
		Value[] values=new Value[vals.length];
		for(int i=0;i<vals.length;i++){
			values[i]=new Value(vals[i]);
		}
		return values;
	}
	
	/**
	 * Devuelve un array de Valores con sus elementos inicializados al valor introducido por parametro.
	 * @param val Valor de inicializacion para todos los elementos del array.
	 * @param size Tamaño del array.
	 * @return Array de Valores inicializados a "val" de tamaño "size".
	 */
	public static Value[] getArrayValues(long val, int size){
		if(size<1) return null;
		Value[] values=new Value[size];
		for(int i=0;i<size;i++){
			values[i]=new Value(val);
		}
		return values;
	}
	
	/**
	 * Devuelve un array de Valores con sus elementos inicializados a los mismos valores que se han introducido en otro array. 
	 * @param vals Array de enteros largos con los valores de inicializacion.
	 * @return Array de igual longitud que el introducido por paramero e inicializado a los mismos valores.
	 */
	public static Value[] getArrayValues(long[] vals){
		if(vals==null) return null;
		Value[] values=new Value[vals.length];
		for(int i=0;i<vals.length;i++){
			values[i]=new Value(vals[i]);
		}
		return values;
	}
	
	/**
	 * Devuelve un array de Valores con sus elementos inicializados al valor introducido por parametro.
	 * @param val Valor de inicializacion para todos los elementos del array.
	 * @param size Tamaño del array.
	 * @return Array de Valores inicializados a "val" de tamaño "size".
	 */
	public static Value[] getArrayValues(Value val, int size){
		if(size<1) return null;
		Value[] values=new Value[size];
		for(int i=0;i<size;i++){
			values[i]=new Value(val);
		}
		return values;
	}
	
	/**
	 * Devuelve un array de Valores con sus elementos inicializados a los mismos Valores que se han introducido en otro array. 
	 * @param vals Array de Valores con los valores de inicializacion.
	 * @return Array de igual longitud que el introducido por paramero e inicializado a los mismos valores.
	 */
	public static Value[] getArrayValues(Value[] vals){
		if(vals==null) return null;
		Value[] values=new Value[vals.length];
		for(int i=0;i<vals.length;i++){
			values[i]=new Value(vals[i]);
		}
		return values;
	}
	
	/**
	 * Devuelve un array de Valores con sus elementos inicializados al valor introducido por parametro.
	 * @param val Valor de inicializacion para todos los elementos del array.
	 * @param size Tamaño del array.
	 * @return Array de Valores inicializados a "val" de tamaño "size".
	 */
	public static Value[] getArrayValues(String val, int size){
		if(size<1) return null;
		Value[] values=new Value[size];
		for(int i=0;i<size;i++){
			values[i]=new Value(val);
		}
		return values;
	}
	
	/**
	 * Devuelve un array de Valores con sus elementos inicializados a los mismos valores que se han introducido en otro array. 
	 * @param vals Array de cadenas con los valores de inicializacion.
	 * @return Array de igual longitud que el introducido por paramero e inicializado a los mismos valores.
	 */
	public static Value[] getArrayValues(String[] vals){
		if(vals==null) return null;
		Value[] values=new Value[vals.length];
		for(int i=0;i<vals.length;i++){
			values[i]=new Value(vals[i]);
		}
		return values;
	}
	
	/**
	 * Establece el Valor a partir de un entero.
	 * @param val Entero.
	 */
	public void set(int val){
		this.val=val;
		this.isPositiveInfinity=false;
		this.isNegativeInfinity=false;
		this.isNaN=false;
	}
	
	/**
	 * Establece el Valor a partir de un entero largo.
	 * @param val Entero largo.
	 */
	public void set(Long val){
		this.val=val;
		this.isPositiveInfinity=false;
		this.isNegativeInfinity=false;
		this.isNaN=false;
	}
	
	/**
	 * Establece el Valor a partir de otro Valor.
	 * Hace una copia de todas las variables de clase, incluso si es NaN.
	 * @param v Valor del mismo tipo.
	 * @return <code>true</code> si se ha podido establecer correctamente.
	 */
	public boolean set(Value v){
		if(v==null) return false;
		this.val=v.val;
		this.isPositiveInfinity=v.isPositiveInfinity;
		this.isNegativeInfinity=v.isNegativeInfinity;
		this.isNaN=v.isNaN;
		return true;
	}
	
	/**
	 * Establece el Valor a partir de una cadena. Si es max/min se establece a +Infinito/-Infinito;
	 * @param val Cadena con un valor entero o un limite (max/min).
	 * @return <code>true</code> si se ha establecido correctamente.
	 */
	public boolean set(String val){
		if(val==null || val.length()==0){
			this.val=0;
			this.isPositiveInfinity=false;
			this.isNegativeInfinity=false;
			this.isNaN=true;
			return false;
		}
		if(AFD.isMax(val)){
			this.val=0;
			setPositiveInfinity();
			return true;
		}
		if(AFD.isMin(val)){
			this.val=0;
			setNegativeInfinity();
			return true;
		}
		if(AFD.isInteger(val)){
			set(Long.parseLong(val));
			return true;
		}
		return false;
	}
	
	/**
	 * Establece el Valor a +Infinito.
	 */
	public void setPositiveInfinity(){
		this.isPositiveInfinity=true;
		this.isNegativeInfinity=false;
		this.isNaN=false;
	}
	
	/**
	 * Establece el Valor a -Infinito.
	 */
	public void setNegativeInfinity(){
		this.isPositiveInfinity=false;
		this.isNegativeInfinity=true;
		this.isNaN=false;
	}
	
	/**
	 * Comprueba si el Valor es +Infinito.
	 * @return <code>true</code> si el Valor es +Infinito. <code>false</code> si no.
	 */
	public boolean isPositiveInfinity(){
		return this.isPositiveInfinity;
	}
	
	/**
	 * Comprueba si el Valor es -Infinito.
	 * @return <code>true</code> si el Valor es -Infinito. <code>false</code> si no.
	 */
	public boolean isNegativeInfinity(){
		return this.isNegativeInfinity;
	}
	
	/**
	 * Establece el Valor a NaN (numeo no valido).
	 */
	public void setNaN(){
		this.isNaN=true;
	}
	
	/**
	 * Comprueba si el Valor es un numeo no valido.
	 * @return <code>true</code> si es un Valor no valido. <code>false</code> si no.
	 */
	public boolean isNaN(){
		return this.isNaN;
	}
	
	/**
	 * Suma dos Valores y devuelve el resultado en otro.
	 * El valor +Infinito prevalece sobre -Infinito.
	 * @param a Valor 1.
	 * @param b Valor 2.
	 * @return Resultado de sumar a+b.
	 */
	public static Value sum(Value a, Value b){
		if(a==null || b==null) return null;
		Value result=new Value();
		result.val=a.val+b.val;
		if(a.isPositiveInfinity || b.isPositiveInfinity) result.isPositiveInfinity=true;
		else{
			if(a.isNegativeInfinity || b.isNegativeInfinity){ result.isNegativeInfinity=true; }
		}
		if(!a.isNaN && !b.isNaN) result.isNaN=false;
		return result;
	}
	
	/**
	 * Suma dos Valores y guarda el resultado en este.
	 * @param v Valor a sumar.
	 * @return <code>true</code> si la operacion se ha realizado correctamente.
	 */
	public boolean sum(Value v){
		if(v==null) return false;
		Value result=new Value();
		result=sum(this,v);
		this.set(result);
		return !result.isNaN;
	}
	
	/**
	 * Comprueva si un Valor es estrictamente mayor que otro.
	 * @param a Valor 1.
	 * @param b Valor 2.
	 * @return <code>true</code> si a>b. <code>false</code> si no.
	 */
	public static boolean greaterThan(Value a, Value b){
		if(a==null) return false;
		if(b==null) return true;
		if(a.isNaN) return false;
		if(b.isNaN) return true;
		if(a.isPositiveInfinity){
			if(b.isPositiveInfinity) return false;
			return true;
		}
		else if(a.isNegativeInfinity){ return false; }
		else{
			if(b.isPositiveInfinity){ return false; }
			else if(b.isNegativeInfinity){ return true; }
			else{ return a.val>b.val; }
		}
	}
	
	/**
	 * Comprueva si este Valor es estrictamente mayor que otro.
	 * @param v Valor a comparar.
	 * @return <code>true</code> si este Valor es mayor (>) que el introducido. <code>false</code> si no.
	 */
	public boolean greaterThan(Value v){
		return greaterThan(this,v);
	}
	
	/**
	 * Comprueva si un Valor es mayor o igual que otro.
	 * @param a Valor 1.
	 * @param b Valor 2.
	 * @return <code>true</code> si a>=b. <code>false</code> si no.
	 */
	public static boolean greaterEqual(Value a, Value b){
		if(a==null && b==null) return true;
		if(a==null) return false;
		if(b==null) return true;
		if(a.isNaN && b.isNaN) return true;
		if(a.isNaN) return false;
		if(b.isNaN) return true;
		if(a.isPositiveInfinity){ return true; }
		else if(a.isNegativeInfinity){
			if(b.isNegativeInfinity) return true;
			return false;
		}
		else{
			if(b.isPositiveInfinity){ return false; }
			else if(b.isNegativeInfinity){ return true; }
			else{ return a.val>=b.val; }
		}
	}
	
	/**
	 * Comprueva si este Valor es mayor o igual que otro.
	 * @param v Valor a comparar.
	 * @return <code>true</code> si este Valor es mayor o igual (>=) que el introducido. <code>false</code> si no.
	 */
	public boolean greaterEqual(Value v){
		return greaterEqual(this,v);
	}
	
	/**
	 * Comprueva si un Valor es menor que otro.
	 * @param a Valor 1.
	 * @param b Valor 2.
	 * @return <code>true</code> si a<b. <code>false</code> si no.
	 */
	public static boolean lessThan(Value a, Value b){
		return greaterThan(b,a);
	}
	
	/**
	 * Comprueva si este Valor es estrictamente menor que otro.
	 * @param v Valor a comparar.
	 * @return <code>true</code> si este Valor es menor (<) que el introducido. <code>false</code> si no.
	 */
	public boolean lessThan(Value v){
		return lessThan(this,v);
	}
	
	/**
	 * Comprueva si un Valor es menor o igual que otro.
	 * @param a Valor 1.
	 * @param b Valor 2.
	 * @return <code>true</code> si a<=b. <code>false</code> si no.
	 */
	public static boolean lessEqual(Value a, Value b){
		return greaterEqual(b,a);
	}
	
	/**
	 * Comprueva si este Valor es meor o igual que otro.
	 * @param v Valor a comparar.
	 * @return <code>true</code> si este Valor es menor o igual (<=) que el introducido. <code>false</code> si no.
	 */
	public boolean lessEqual(Value v){
		return lessEqual(this,v);
	}
	
	/**
	 * Comprueba si dos Valores son iguales.
	 * @param a Valor 1.
	 * @param b Valor 2.
	 * @return <code>true</code> si a=b. <code>false</code> si no.
	 */
	public static boolean equals(Value a, Value b){
		if(a==null && b==null) return true;
		if(a==null || b==null) return false;
		if(a.isNaN && b.isNaN) return true;
		if(a.isNaN || b.isNaN) return false;
		if(a.isPositiveInfinity && b.isPositiveInfinity) return true;
		if(a.isPositiveInfinity || b.isPositiveInfinity) return false;
		if(a.isNegativeInfinity && b.isNegativeInfinity) return true;
		if(a.isNegativeInfinity || b.isNegativeInfinity) return false;
		if(a.val==b.val) return true;
		return false;
	}
	
	/**
	 * Comprueba si un Valor es igual a este.
	 * @param v Valor a comparar.
	 * @return <code>true</code> si este Valor es igual (=) que el introducido. <code>false</code> si no.
	 */
	public boolean equals(Value v){
		return equals(this,v);
	}
	
	/**
	 * Comprueba si un array de Valores esta ordenado de forma ascendente.
	 * Esto es vals[0]<=vals[1]<=...<=vals[n-2]<=vals[n-1] siendo n el tamaño del array.
	 * @param vals Array de Valores a comprobar.
	 * @return <code>true</code> si esta ordenado de forma ascendente. <code>false</code> si no.
	 */
	public static boolean isSorted(Value[] vals){
		if(vals==null || vals.length==0) return false;
		for(int i=1;i<vals.length;i++){
			if(greaterThan(vals[i-1],vals[i])) return false;
		}
		return true;
	}
	
	/**
	 * Devuelve el Valor mas alto de un array de Valores.
	 * @param vals Array de Valores del cual se quiere obtener el mayor.
	 * @return Valor mas alto (por referencia).
	 */
	public static Value getMaxValue(Value[] vals){
		if(vals==null || vals.length==0) return null;
		Value maxValue=vals[0];
		for(int i=1;i<vals.length;i++){
			if(Value.greaterThan(vals[i],maxValue)) maxValue=vals[i];
		}
		return maxValue;
	}
	
	/**
	 * Devuelve el Valor mas bajo de un array de Valores.
	 * @param vals Array de Valores del cual se quiere obtener el menor.
	 * @return Valor mas bajo (por referencia). NaN si todos lo son.
	 */
	public static Value getMinValue(Value[] vals){
		if(vals==null || vals.length==0) return null;
		Value minValue=vals[0];
		for(int i=1;i<vals.length;i++){
			if(Value.lessThan(vals[i],minValue)) minValue=vals[i];
		}
		return minValue;
	}
	
	/**
	 * Devuelve el indice del elemento que tenga el valor introducido como parametro. Si hay varios elige uno al azar.
	 * @param vals Array de Valores (elementos).
	 * @param v Valor a buscar. 
	 * @return Indice de uno de los elementos con dicho valor. -1 si no se ha encontrado ninguno.
	 */
	public static int getIndexOf(Value[] vals, Value v){
		if(vals==null || vals.length==0) return -1;
		// Contar el numero de elementos con el valor indicado
		int count=0;
		for(int i=0;i<vals.length;i++){
			if(Value.equals(vals[i],v)) count=count+1;
		}
		if(count==0) return-1;
		// Obtener indices de elementos con ese valor
		int[] proposed=new int[count];
		int j=0;
		for(int i=0;i<vals.length;i++){
			if(vals[i].equals(v)) proposed[j++]=i;
		}
		// Elegir uno al azar (si hay mas de 1)
		int selected=proposed[0];
		if(count>1){
			Random random=new Random();
			selected=proposed[random.nextInt(count)];
		}
		return selected;
	}
	
	/**
	 * Devuelve el Valor como una cadena.
	 */
	@Override
	public String toString(){
		String str;
		if(isNaN){ str="NaN"; }
		else if(isPositiveInfinity){ str="+INF"; }
		else if(this.isNegativeInfinity){ str="-INF"; }
		else{ str=""+val; }
		return str;
	}
	
	/**
	 * Devuelve el Valor como una cadena, usando MAX/min en vez de +INF/-INF cuando corresponda.
	 * @return Cadena con el valor.
	 */
	public String toStringForRules(){
		String str;
		if(isNaN){ str="NaN"; }
		else if(isPositiveInfinity){ str="MAX"; }
		else if(this.isNegativeInfinity){ str="min"; }
		else{ str=""+val; }
		return str;
	}
	
}
