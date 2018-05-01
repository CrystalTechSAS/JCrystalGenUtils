package jcrystal.utils.langAndPlats;

import java.util.List;

import jcrystal.utils.StringSeparator;
import jcrystal.utils.langAndPlats.AbsCodeBlock.IF;
import jcrystal.utils.langAndPlats.AbsCodeBlock.Lambda;
import jcrystal.utils.langAndPlats.AbsCodeBlock.P;
import jcrystal.utils.langAndPlats.AbsCodeBlock.PL;

public interface AbsICodeBlock {

	int $(String ins);

	IF $if(String cond, Runnable block);
	
	IF $if(boolean putif, String cond, Runnable block);

	void $if(String cond, String code);

	void $else_if(String cond, Runnable block);

	void $else(Runnable block);

	void $(String pre, Runnable r);

	void $(String pre, Runnable r, String pos);

	void $VoidCatch(String ex);

	void $SingleCatch(String ex, String p);

	//
	/**
	 * Variable declaration
	 * @param tipo
	 * @param name
	 * @param valor
	 */
	void $V(String tipo, String name, String valor);

	IF $if_let(String tipo, String name, String valor, String where, Runnable block);

	/**
	 * For each
	 * @param tipo
	 * @param name
	 * @param valor
	 */
	void $FE(String tipo, String name, String valor, Runnable block);

	void $M(int modifiers, String retorno, String name, PL params, Runnable block);

	void $M(int modifiers, String retorno, String name, StringSeparator params, Runnable block);

	void $M(int modifiers, String retorno, String name, StringSeparator params, String excepciones, Runnable block);
	
	void $M(int modifiers, String retorno, String name, PL params, String excepciones, Runnable block);

	void $M(int modifiers, String tipoRetorno, String name, PL params, Runnable block, String retorno);

	void $L(String pre, Lambda block, String pos);

	String buildIf(String cond);

	// --- Context methods
	boolean is_iOS();

	boolean is_Android();

	boolean is_Server();

	boolean is_Web();
	
	public default P P(String tipo, String nombre){
        return new P(tipo, nombre);
    }
    public default P P(Class<?> tipo, String nombre){
        return new P(tipo.getSimpleName(), nombre);
    }
    public default P PLIST(String tipo, String nombre){
        return new P("java.util.list<" + tipo + ">", nombre);
    }
    public default PL $(P...list){
        return new PL(list);
    }
    public default PL $(List<P> list){
        return new PL(list);
    }

}