package jcrystal.utils.langAndPlats;

import jcrystal.types.IJType;
import jcrystal.types.WrapStringJType;
import jcrystal.utils.StringSeparator;

import java.io.*;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.stream.Collectors;

public class AbsJavascriptCode extends AbsCodeBlock{
    private static final long serialVersionUID = 8286135788802310977L;

	public AbsJavascriptCode(){}
    public AbsJavascriptCode(int level){
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

	public final void $import(String...packages){
		for(String h : packages)
			$("import " + h + ";");
	}

	public void append(String val) {
		if(isEmpty())
			$(val);
		else
			set(size()-1, get(size()-1)+val);
	}
	@Override
	public String $(IJType type) {
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
}
