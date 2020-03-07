package jcrystal.utils.langAndPlats;

import jcrystal.types.IJType;
import jcrystal.types.WrapStringJType;
import jcrystal.utils.StringSeparator;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.stream.Collectors;

public class JavascriptCode extends AbsCodeBlock{
	public JavascriptCode(){}
    public JavascriptCode(int level){
        super(level);
    }

    /**
     * Variable declaration
     * @param tipo
     * @param name
     * @param valor
     */
    public final void $V(String tipo, String name, String valor){
        this.$("var " + name + " = " + valor + ";");
    }

    @Override
    public IF $if_let(String tipo, String name, String valor, String where, Runnable block) {
        $(tipo + " " + name + " = " + valor+";");
        if(where != null && !where.trim().isEmpty())
            return $if(name + " != null && (" + where + ")", block);
        else
            return $if(name + " != null", block);
    }
    /**
     * For each
     * @param tipo
     * @param name
     * @param valor
     */
    public final void $FE(String tipo, String name, String valor, Runnable block){
        this.$("for("+tipo+" " + name + " : " + valor + ")",  block);
    }
    @Override
    public void $M(int modifiers, String retorno, String name, String params, String exceptions, Runnable block) {
        StringSeparator mods = new StringSeparator(" ");
        if(Modifier.isPublic(modifiers))mods.add("public");
        else if(Modifier.isProtected(modifiers))mods.add("protected");
        else if(Modifier.isPrivate(modifiers))mods.add("private");
        if(Modifier.isStatic(modifiers))mods.add("static");
        if(retorno == null)
        	retorno = "void";
        this.$("function "+name+"(" + params + ")", block);
    }

    @Override
    public String buildIf(String cond) {
        return "if("+cond+")";
    }

    @Override
    public void $L(String pre, Lambda block, String pos) {
        add(pre+"function(" + block.params+"){");
        String lastPre = prefijo;
        prefijo += "\t";
        block.run();
        prefijo = lastPre;
        add("}" + pos);
    }
    
    public final void $import(String...packages){
		for(String h : packages)
			$("import " + h + ";");
	}

	@Override
	public String $(IJType type) {
		String superRet = super.$(type);
		if(superRet != null)
			return superRet;
		if(type instanceof WrapStringJType)
			return type.getName();
		else if(type.getInnerTypes().isEmpty())
			return type.getName();
		else if(type.isArray())
			return $(type.getInnerTypes().get(0))+"[]";
		else
			return type.getName()+"<" + type.getInnerTypes().stream().map(f->$(f)).collect(Collectors.joining(", ")) + ">";
	}
	@Override
	public String $V(IJType type, String name) {
		return name;
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
