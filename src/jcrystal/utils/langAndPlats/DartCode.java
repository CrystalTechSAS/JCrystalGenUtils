package jcrystal.utils.langAndPlats;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.stream.Collectors;

import jcrystal.types.IJType;
import jcrystal.types.WrapStringJType;
import jcrystal.utils.StringSeparator;

public class DartCode extends AbsImportsCodeBlock{
    private static final long serialVersionUID = 8286135788802310977L;

	public DartCode(){}
    public DartCode(int level){
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
		for(String h : packages)
			$("import " + h + ";");
	}
	
	@Override
	public String $(IJType type) {
		if(type instanceof WrapStringJType)
			return type.getName();
		if(type.is(boolean.class))
			return "bool";
		if(type.is(long.class))
			return "int";
		if(type.is(Long.class))
			return "int";
		else if(type.getInnerTypes().isEmpty()) {
			if(type.getName().startsWith("java.lang"))
				return type.getSimpleName();
			else if(type.is("org.json.JSONObject"))
				return "Map<String, dynamic>";
			else if(type.is("org.json.JSONArray"))
				return "List<dynamic>";
			else
				return type.getName();
		}else if(type.isArray())
			return $(type.getInnerTypes().get(0))+"[]";
		else if(type.isIterable())
			return "List<"+$(type.getInnerTypes().get(0))+">";
		else {
			if((type.getName()+"<" + type.getInnerTypes().stream().map(f->$(f)).collect(Collectors.joining(", ")) + ">").contains("<["))
				throw new NullPointerException();
			return type.getName()+"<" + type.getInnerTypes().stream().map(f->$(f)).collect(Collectors.joining(", ")) + ">";
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
