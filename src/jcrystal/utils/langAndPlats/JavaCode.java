package jcrystal.utils.langAndPlats;

import jcrystal.preprocess.descriptions.IJType;
import jcrystal.preprocess.descriptions.WrapStringJType;
import jcrystal.utils.StringSeparator;
import jcrystal.utils.context.CodeGeneratorContext;

import java.io.*;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class JavaCode extends AbsCodeBlock{
    private static final long serialVersionUID = 8286135788802310977L;

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
    public final void $M(int modifiers, String retorno, String name, PL params, Runnable block){
        StringSeparator mods = new StringSeparator(" ");
        if(Modifier.isPublic(modifiers))mods.add("public");
        else if(Modifier.isProtected(modifiers))mods.add("protected");
        else if(Modifier.isPrivate(modifiers))mods.add("private");
        if(Modifier.isStatic(modifiers))mods.add("static");
        StringSeparator pars = new StringSeparator(", ");
        for(P p : params.lista)if(p!=null)
            pars.add($(p.tipo) + " " + p.nombre);
        if(retorno == null)
        	retorno = "void";
        this.$(mods + " " + retorno + " " + name + "(" + pars + ")", block);
    }

    @Override
    public void $M(int modifiers, String retorno, String name, StringSeparator params, Runnable block) {
        StringSeparator mods = new StringSeparator(" ");
        if(Modifier.isPublic(modifiers))mods.add("public");
        else if(Modifier.isProtected(modifiers))mods.add("protected");
        else if(Modifier.isPrivate(modifiers))mods.add("private");
        if(Modifier.isStatic(modifiers))mods.add("static");
        if(retorno == null)
        	retorno = "void";
        this.$(mods + " " + retorno + " " + name + "(" + params + ")", block);
    }

    public final void $M(int modifiers, String tipoRetorno, String name, PL params, final Runnable block, final String retorno){
        $M(modifiers, tipoRetorno, name, params, ()->{
            block.run();
            $(retorno);
        });
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
    public static void addResource(InputStream resource, String paquete, File out) throws Exception{
        safeMkdirs(out.getParentFile());
        try(BufferedReader br = new BufferedReader(new InputStreamReader(resource)); PrintWriter pw = new PrintWriter(out)){
            pw.println("package "+paquete+";");
            for(String line; (line = br.readLine())!=null; )
                pw.println(line);
        }
    }
    
    public static void safeMkdirs(File f){
        f.mkdirs();
    }

    @Override
    public void $M(int modifiers, String retorno, String name, StringSeparator params, String excepciones, Runnable block) {
	    StringSeparator mods = new StringSeparator(" ");
	    if(Modifier.isPublic(modifiers))mods.add("public");
	    else if(Modifier.isProtected(modifiers))mods.add("protected");
	    else if(Modifier.isPrivate(modifiers))mods.add("private");
	    if(Modifier.isStatic(modifiers))mods.add("static");
	    if(retorno == null)
		    retorno = "void";
	    this.$(mods + " " + retorno + " " + name + "(" + params + ")" + excepciones, block);
    }
	public final void $import(String...packages){
		for(String h : packages)
			$("import " + h + ";");
	}
	@Override
	public void $M(int modifiers, String retorno, String name, PL params, String excepciones, Runnable block) {
		StringSeparator mods = new StringSeparator(" ");
	        if(Modifier.isPublic(modifiers))mods.add("public");
	        else if(Modifier.isProtected(modifiers))mods.add("protected");
	        else if(Modifier.isPrivate(modifiers))mods.add("private");
	        if(Modifier.isStatic(modifiers))mods.add("static");
	        StringSeparator pars = new StringSeparator(", ");
	        for(P p : params.lista)if(p!=null)
	            pars.add($(p.tipo) + " " + p.nombre);
	        if(retorno == null)
	        	retorno = "void";
	        this.$(mods + " " + retorno + " " + name + "(" + pars + ")" + excepciones, block);
	}
	
	@Override
	public String $(IJType type) {
		if(type instanceof WrapStringJType)
			return type.getName();
		else if(type.getInnerTypes().isEmpty()) {
			if(type.getName().startsWith("java.lang"))
				return type.getSimpleName();
			else
				return type.getName();
		}else if(type.isArray())
			return $(type.getInnerTypes().get(0))+"[]";
		else {
			if((type.getName()+"<" + type.getInnerTypes().stream().map(f->$(f)).collect(Collectors.joining(", ")) + ">").contains("<["))
				throw new NullPointerException();
			return type.getName()+"<" + type.getInnerTypes().stream().map(f->$(f)).collect(Collectors.joining(", ")) + ">";
		}
	}
	

}
