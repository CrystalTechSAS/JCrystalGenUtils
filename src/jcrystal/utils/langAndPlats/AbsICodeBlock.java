package jcrystal.utils.langAndPlats;

import java.util.List;

import jcrystal.preprocess.descriptions.IJType;
import jcrystal.preprocess.descriptions.JTypeSolver;
import jcrystal.utils.StringSeparator;
import jcrystal.utils.context.CodeGeneratorContext;
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
	
	String $(IJType type);
	default IJType $convert(IJType type) {
		CodeGeneratorContext cnt = CodeGeneratorContext.get(); 
		return cnt.typeConverter == null ? type : cnt.typeConverter.convert(type);
	}
	default IJType $convert(Class<?> type){
		return $convert(JTypeSolver.load(type, null));
	}

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
	default IF $if_let(IJType tipo, String name, String valor, String where, Runnable block) {
		if(tipo.nullable()) {
			return $if_let($(tipo), name, valor, where, block);			
		}else {
			$V($(tipo), name, valor);
			if(where != null) {
				return $if(where, block);
			}else {
				block.run();
				return null;
			}
		}
		
	}

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

	public default P P(String tipo, String nombre){
	        return new P(tipo, nombre);
	    }
	public default P P(IJType tipo, String nombre){
        return new P(tipo, nombre);
    }
    public default P P(Class<?> tipo, String nombre){
        return new P(JTypeSolver.load(tipo, null), nombre);
    }
    public default PL $(P...list){
        return new PL(list);
    }
    public default PL $(List<P> list){
        return new PL(list);
    }
    public default PL $(List<P> list, P...list2){
        return new PL(list, list2);
    }
    public default PL $(PL list, P...list2){
        return new PL(list.lista, list2);
    }

}