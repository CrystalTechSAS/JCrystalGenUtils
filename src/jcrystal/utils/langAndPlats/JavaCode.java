package jcrystal.utils.langAndPlats;

import java.lang.reflect.Modifier;
import java.util.stream.Collectors;

import jcrystal.types.IJType;
import jcrystal.types.WrapStringJType;
import jcrystal.utils.StringSeparator;

public class JavaCode extends AbsCodeBlock{
	public JavaCode(){}
    public JavaCode(int level){
        super(level);
    }

    /**
     * Variable declaration
     * @param tipo
     * @param name
     * @param valor
     */
    public final void $V(String tipo, String name, String valor){
        this.$(tipo + " " + name + " = " + valor + ";");
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
    public String buildIf(String cond) {
        return "if("+cond+")";
    }

    @Override
    public void $L(String pre, Lambda block, String pos) {
        add(pre+"{(" + block.params+") in ");
        String lastPre = prefijo;
        prefijo += "\t";
        block.run();
        prefijo = lastPre;
        add("}" + pos);
    }    

    @Override
    public void $M(int modifiers, String retorno, String name, String params, String excepciones, Runnable block) {
	    StringSeparator mods = new StringSeparator(" ");
	    if(Modifier.isPublic(modifiers))mods.add("public");
	    else if(Modifier.isProtected(modifiers))mods.add("protected");
	    else if(Modifier.isPrivate(modifiers))mods.add("private");
	    if(Modifier.isStatic(modifiers))mods.add("static");
	    if(retorno == null)
		    retorno = "void";
	    if(excepciones == null)
	    	this.$(mods + (!retorno.isEmpty()?" " + retorno:"") + " " + name + "(" + params + ")", block);
	    else
	    	this.$(mods + (!retorno.isEmpty()?" " + retorno:"") + " " + name + "(" + params + ")" + excepciones, block);
    }
	public final void $import(String...packages){
		if(packages != null)
			for(String h : packages)
				$("import " + h + ";");
	}
	
	@Override
	public String $toString(IJType type) {
		if(type instanceof WrapStringJType)
			return type.name();
		else if(type.getInnerTypes().isEmpty()) {
			if(type.name().startsWith("java.lang"))
				return type.getSimpleName();
			else
				return type.name().replace("$", ".");
		}else if(type.isArray())
			return $(type.getInnerTypes().get(0))+"[]";
		else {
			return type.name()+"<" + type.getInnerTypes().stream().map(f->$(f)).collect(Collectors.joining(", ")) + ">";
		}
	}
	
	@Override
	public String $V(IJType type, String name) {
		return $(type)+" " + name;
	}
	
	@Override
	public void $ifNull(String param, Runnable code) {
		$if(param + " == null", code);
	}

	@Override
	public void $ifNotNull(String param, Runnable code) {
		$if(param + " != null", code);
	}

}
