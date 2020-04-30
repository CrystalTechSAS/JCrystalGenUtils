package jcrystal.utils.langAndPlats.delegates;

import jcrystal.types.JVariable;
import jcrystal.utils.langAndPlats.AbsCodeBlock.PL;

public interface AbsCodeBlockDelegator extends jcrystal.utils.langAndPlats.AbsICodeBlock{
	jcrystal.utils.langAndPlats.AbsCodeBlock getDelegator();
	default boolean add(String s){
		return getDelegator().add(s);
	}
	default boolean isEmpty(){
		return getDelegator().isEmpty();
	}
	default int size(){
		return getDelegator().size();
	}
	default void $V(String tipo, String name, String valor){
		getDelegator().$V(tipo, name, valor);
	}
	default jcrystal.utils.langAndPlats.AbsCodeBlock.IF $if_let(String tipo, String name, String valor, String where, Runnable block){
		return getDelegator().$if_let(tipo, name, valor, where, block);
	}
	default jcrystal.utils.langAndPlats.AbsCodeBlock.IF $if(String cond, Runnable block){
		return getDelegator().$if(cond, block);
	}
	default void $if(String cond, String code){
		getDelegator().$if(cond, code);
	}
	default jcrystal.utils.langAndPlats.AbsCodeBlock.IF $if(boolean putif, String cond, Runnable block){
		return getDelegator().$if(putif, cond, block);
	}
	default java.util.ArrayList<String> getCode(){
		return getDelegator().getCode();
	}
	default int $(String ins){
		return getDelegator().$(ins);
	}
	default void $(String pre, Runnable r, String pos, Runnable r2, String pos2){
		getDelegator().$(pre, r, pos, r2, pos2);
	}
	default void $(String pre, Runnable r, String pos){
		getDelegator().$(pre, r, pos);
	}
	default void $(String pre, Runnable r){
		getDelegator().$(pre, r);
	}
	default void $M(int modifiers, String retorno, String name, String params, String excepciones, Runnable block){
		getDelegator().$M(modifiers, retorno, name, params, excepciones, block);
	}
	default void $catch(String ex, Runnable block){
		getDelegator().$catch(ex, block);
	}
	default void $VoidCatch(String ex){
		getDelegator().$VoidCatch(ex);
	}
	default void $SingleCatch(String ex, String p){
		getDelegator().$SingleCatch(ex, p);
	}
	default void $ifNull(String cond, Runnable code){
		getDelegator().$ifNull(cond, code);
	}
	default void $append(jcrystal.utils.langAndPlats.AbsICodeBlock internal){
		getDelegator().$append(internal);
	}
	default String buildIf(String cond){
		return getDelegator().buildIf(cond);
	}
	default void $ifNotNull(String cond, Runnable code){
		getDelegator().$ifNotNull(cond, code);
	}
	default void $L(String pre, jcrystal.utils.langAndPlats.AbsCodeBlock.Lambda block, String pos){
		getDelegator().$L(pre, block, pos);
	}
	default void $else_if(String cond, Runnable block){
		getDelegator().$else_if(cond, block);
	}
	default void $else(Runnable block){
		getDelegator().$else(block);
	}
	default void $FE(String tipo, String name, String valor, Runnable block){
		getDelegator().$FE(tipo, name, valor, block);
	}
	default void incLevel(){
		getDelegator().incLevel();
	}
	default void decLevel(){
		getDelegator().decLevel();
	}
	default String $V(JVariable p){
		return getDelegator().$V(p);
	}
	default String $V(jcrystal.types.IJType type, String name){
		return getDelegator().$V(type, name);
	}
	default jcrystal.utils.langAndPlats.AbsCodeBlock.IF $if_let(jcrystal.types.IJType tipo, String name, String valor, String where, Runnable block){
		return getDelegator().$if_let(tipo, name, valor, where, block);
	}
	default PL $(java.util.List<JVariable> list){
		return getDelegator().$(list);
	}
	default PL $(JVariable[] list){
		return getDelegator().$(list);
	}
	default PL $(java.util.List<JVariable> list, JVariable[] list2){
		return getDelegator().$(list, list2);
	}
	default PL $(PL list, JVariable[] list2){
		return getDelegator().$(list, list2);
	}
	default String $(jcrystal.types.IJType type){
		return getDelegator().$(type);
	}
	default void $M(int modifiers, String retorno, String name, jcrystal.utils.StringSeparator params, Runnable block){
		getDelegator().$M(modifiers, retorno, name, params, block);
	}
	default void $M(int modifiers, String retorno, String name, PL params, Runnable block){
		getDelegator().$M(modifiers, retorno, name, params, block);
	}
	default void $M(int modifiers, String retorno, String name, String params, Runnable block){
		getDelegator().$M(modifiers, retorno, name, params, block);
	}
	default void $M(int modifiers, String retorno, String name, jcrystal.utils.StringSeparator params, String excepciones, Runnable block){
		getDelegator().$M(modifiers, retorno, name, params, excepciones, block);
	}
	default void $M(int modifiers, String retorno, String name, PL params, String excepciones, Runnable block){
		getDelegator().$M(modifiers, retorno, name, params, excepciones, block);
	}
	default void $M(int modifiers, String retorno, String name, java.util.List<String> params, Runnable block){
		getDelegator().$M(modifiers, retorno, name, params, block);
	}
	default void $M(int modifiers, String retorno, String name, java.util.stream.Stream<JVariable> params, Runnable block){
		getDelegator().$M(modifiers, retorno, name, params, block);
	}
	default void $ifNull(boolean putIf, String cond, Runnable code){
		getDelegator().$ifNull(putIf, cond, code);
	}
	default JVariable P(jcrystal.types.IJType tipo, String nombre){
		return getDelegator().P(tipo, nombre);
	}
	default void $ifNotNull(boolean putIf, String cond, Runnable code){
		getDelegator().$ifNotNull(putIf, cond, code);
	}
	default void addStream(java.io.InputStream stream){
		getDelegator().addStream(stream);
	}
	default jcrystal.types.IJType $convert(jcrystal.types.IJType type){
		return getDelegator().$convert(type);
	}
	default String getCodeString(){
		return getDelegator().getCodeString();
	}
}
