package jcrystal.utils.langAndPlats;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.stream.Collectors;

import jcrystal.types.IJType;
import jcrystal.types.WrapStringJType;
import jcrystal.utils.StringSeparator;

/**
* Created by G on 1/14/2017.
*/
public class TypescriptCode extends AbsImportsCodeBlock{

	/**
	* Variable declaration
	* @param tipo
	* @param name
	* @param valor
	*/
	public final void $V(String tipo, String name, String valor){
		this.$("let " + name + " : " + tipo + " = " + valor+";");
	}
	
	@Override
	public IF $if_let(String tipo, String name, String valor, String where, Runnable block) {
		$("let " + name + (tipo!=null?":"+tipo:"")+" = this." + valor+";");
		if(where != null && !where.trim().isEmpty())
			return $if("typeof("+ name+") !== 'undefined' && "+name + " !== null && (" + where + ")", block);
		else
			return $if("typeof("+ name+") !== 'undefined' && "+name + " !== null", block);
	}
	
	/**
	* For each
	* @param tipo
	* @param name
	* @param valor
	*/
	public final void $FE(String tipo, String name, String valor, Runnable block){
		this.$("for ( let " + name + " of " + valor+")",  block);
	}
	
	@Override
	public void $M(int modifiers, String retorno, String name, String params, String exception, Runnable block) {
		if(exception != null)
			throw new NullPointerException();
		StringSeparator mods = new StringSeparator(" ");
		if(Modifier.isPublic(modifiers))mods.add("public");
		else if(Modifier.isProtected(modifiers))mods.add("protected");
		else if(Modifier.isPrivate(modifiers))mods.add("private");
		if(Modifier.isStatic(modifiers))
			mods.add("static");
		this.$(mods + " " + name +"(" + params + "):" + retorno, block);
	}
	
	@Override
	public void $L(String pre, Lambda block, String pos) {
		if(block.ret!=null)
			add(pre + "{(" + block.params + ") =>" + block.ret + " ");
		else
			add(pre + "{(" + block.params + ") =>  ");
		String lastPre = prefijo;
		prefijo += "\t";
		block.run();
		prefijo = lastPre;
		add("}" + pos);
	}
	@Override
	public String buildIf(String cond) {
		return "if ("+cond+") ";
	}
	
	@Override
	public String $(IJType type) {
		if(type instanceof WrapStringJType)
			return type.getName();
		else if(type.is(int.class, long.class, Long.class, Integer.class, double.class, Double.class, float.class))
			return "number";
		else if(type.is(boolean.class, Boolean.class))
			return "boolean";
		else if(type.is(String.class))
			return "string";
		else if(type.is(Date.class))
			return "any";
		else if(type.getName().equals("com.google.appengine.api.datastore.GeoPt"))
			return "number[]";
		else if(type.isEnum())
			return type.getSimpleName();
		else if(type.getInnerTypes().isEmpty())
			return type.getSimpleName();
		else if(type.isArray() || type.isIterable())
			return $(type.getInnerTypes().get(0))+"[]";
		else
			return type.getName()+"<" + type.getInnerTypes().stream().map(f->$(f)).collect(Collectors.joining(", ")) + ">";
	}

	
	@Override
	public String $V(IJType type, String name) {
		return name +" : "+$(type);
	}

	@Override
	public void $ifNull(String param, Runnable code) {
		$if(param + " === null || typeof(" + param + ") === 'undefined'", code);
	}

	@Override
	public void $ifNotNull(String param, Runnable code) {
		$if(param + " !== null && typeof(" + param + ") !== 'undefined'", code);
	}
}
