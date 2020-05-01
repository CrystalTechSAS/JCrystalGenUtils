package jcrystal.utils.langAndPlats;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jcrystal.types.IJType;
import jcrystal.types.JVariable;
import jcrystal.utils.StringSeparator;
import jcrystal.utils.context.CodeGeneratorContext;
import jcrystal.utils.langAndPlats.AbsCodeBlock.IF;
import jcrystal.utils.langAndPlats.AbsCodeBlock.Lambda;
import jcrystal.utils.langAndPlats.AbsCodeBlock.PL;

public interface AbsICodeBlock {

	int $(String ins);

	IF $if(String cond, Runnable block);
	
	IF $if(boolean putif, String cond, Runnable block);

	void $if(String cond, String code);
	
	void $ifNull(String cond, Runnable code);
	
	void $ifNotNull(String cond, Runnable code);
	
	default void $ifNull(boolean putIf, String cond, Runnable code) {
		if(putIf || cond == null)
			code.run();
		else
			$ifNull(cond, code);
	}
	
	default void $ifNotNull(boolean putIf, String cond, Runnable code) {
		if(putIf || cond == null)
			code.run();
		else
			$ifNotNull(cond, code);
	}
	
	void $else_if(String cond, Runnable block);

	void $else(Runnable block);

	void $(String pre, Runnable r);
	
	default String $(IJType type) {
		CodeGeneratorContext cnt = CodeGeneratorContext.get();
		String ret = null;
		if(cnt.typeConverter != null)
			ret = cnt.typeConverter.$toString(type);
		if(ret != null)
			return ret;
		return $toString(type);
	}
	
	String $toString(IJType type);
	
	String $V(IJType type, String name);
	default String $V(JVariable p) {
		return $V($convert(p.type()), p.name());
	}
	
	default IJType $convert(IJType type) {
		CodeGeneratorContext cnt = CodeGeneratorContext.get(); 
		return cnt.typeConverter == null ? type : cnt.typeConverter.convert(type);
	}

	void $(String pre, Runnable r, String pos);

	void $catch(String ex, Runnable block);
	
	void $VoidCatch(String ex);

	void $SingleCatch(String ex, String p);

	ArrayList<String> getCode();
	default String getCodeString(){
		return getCode().stream().collect(Collectors.joining("\r\n"));
	};
	boolean isEmpty();
	int size();
	void $append(AbsICodeBlock internal);
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

	void $M(int modifiers, String retorno, String name, String params, String excepciones, Runnable block);

	default void $M(int modifiers, String retorno, String name, String params, Runnable block) {
		$M(modifiers, retorno, name, params, null, block);
	}

	default void $M(int modifiers, String retorno, String name, PL params, Runnable block) {
		$M(modifiers, retorno, name, params.list.stream().filter(p->p!=null).map(p->$V(p)).collect(Collectors.joining(", ")), block);
	}
	
	default void $M(int modifiers, String retorno, String name, StringSeparator params, Runnable block) {
		$M(modifiers, retorno, name, params.toString(), block);
	}
	default void $M(int modifiers, String retorno, String name, List<String> params, Runnable block) {
		$M(modifiers, retorno, name, params.stream().collect(Collectors.joining(", ")), block);
	}
	default void $M(int modifiers, String retorno, String name, Stream<JVariable> params, Runnable block) {
		$M(modifiers, retorno, name, params.map(p->$V(p)).collect(Collectors.joining(", ")), block);
	}

	default void $M(int modifiers, String retorno, String name, StringSeparator params, String excepciones, Runnable block) {
		$M(modifiers, retorno, name, params.toString(), excepciones, block);
	}
	
	default void $M(int modifiers, String retorno, String name, PL params, String excepciones, Runnable block) {
		$M(modifiers, retorno, name, params.list.stream().filter(p->p!=null).map(p->$V(p)).collect(Collectors.joining(", ")), excepciones, block);
	}

	void $L(String pre, Lambda block, String pos);

	String buildIf(String cond);

	public default JVariable P(IJType type, String name){
        return new JVariable(type, name);
    }
    public default PL $(JVariable...list){
        return new PL(list);
    }
    public default PL $(List<JVariable> list){
        return new PL(list);
    }
    public default PL $(List<JVariable> list, JVariable...list2){
        return new PL(list, list2);
    }
    public default PL $(PL list, JVariable...list2){
        return new PL(list.list, list2);
    }
    
    default void addStream(InputStream stream) {
    	try(BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
			for(String h; (h=br.readLine()) != null;)
				$(h);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}