package jcrystal.utils.langAndPlats;

import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import jcrystal.types.IJType;
import jcrystal.types.JVariable;
import jcrystal.types.WrapStringJType;
import jcrystal.types.utils.GlobalTypes;
import jcrystal.utils.StringSeparator;

/**
* Created by G on 1/14/2017.
*/
public class SwiftCode extends AbsCodeBlock{
	
	/**
	* Variable declaration
	* @param tipo
	* @param name
	* @param valor
	*/
	public final void $V(String tipo, String name, String valor){
		this.$("var " + name + " : " + tipo + " = " + valor);
	}
	@Override
	public String $V(JVariable variable){
    	return variable.name() + " : " + this.$(variable.type());
    }
	@Override
	public IF $if_let(String tipo, String name, String valor, String where, Runnable block) {
		if(where != null && !where.trim().isEmpty()) {
			return $if("let " + name + " = " + valor + ", " + where, block);
		}else {
			return $if("let " + name + " = " + valor, block);
		}
	}
	
	/**
	* For each
	* @param tipo
	* @param name
	* @param valor
	*/
	public final void $FE(String tipo, String name, String valor, Runnable block){
		this.$("for " + name + " in " + valor,  block);
	}
	
	@Override
	public void $M(int modifiers, String retorno, String name, String params, String exceptions, Runnable block) {
		if(exceptions!=null)
			throw new NullPointerException();

		StringSeparator mods = new StringSeparator(" ");
		if(Modifier.isPublic(modifiers))mods.add("public");
		else if(Modifier.isProtected(modifiers))mods.add("protected");
		else if(Modifier.isPrivate(modifiers))mods.add("private");
		if(Modifier.isStatic(modifiers))mods.add("static");
		if(retorno == null || retorno.equalsIgnoreCase("void"))
			this.$(mods + " func " + name +"(" + params + ")", block);
		else
			this.$(mods + " func " + name +"(" + params + ") -> " + retorno, block);
	}

	@Override
	public void $L(String pre, Lambda block, String pos) {
		if(block.ret!=null)
			add(pre + "{(" + block.params + ")->(" + block.ret + ") in ");
		else
			add(pre + "{(" + block.params + ") in ");
		String lastPre = prefijo;
		prefijo += "\t";
		block.run();
		prefijo = lastPre;
		add("}" + pos);
	}
	@Override
	public String buildIf(String cond) {
		return "if "+cond+" ";
	}
	
	@Override
	public String $(IJType type) {
		if(type instanceof WrapStringJType)
			return type.name();
		return $(type, type.nullable());
	}
	public String $(IJType type, boolean nullable) {
		if(type instanceof WrapStringJType)
			return type.name() + (nullable?"?":"");
		else if(type.isArray() && type.firstInnerType().is(byte.class, Byte.class))
			return "NSData" + (nullable?"?":"");
		else if(type.is("jcrystal.server.FileUploadDescriptor"))
			return "JCFile";
		else if(type.is(int.class))
			return "Int" + (nullable?"?":"");
		else if(type.is(double.class))
			return "Double";
		else if(type.is(Double.class))
			return "Double" + (nullable?"?":"");
		else if(type.is(Long.class))
			return "Int64" + (nullable?"?":"");
		else if(type.is(long.class))
			return "Int64";
		else if(type.is(Integer.class))
			return "Int" + (nullable?"?":"");
		else if(type.is(Boolean.class))
			return "Bool" + (nullable?"?":"");
		else if(type.is(boolean.class))
			return "Bool";
		else if(type.is(Date.class))
			return "Date" + (nullable?"?":"");
		else if(type.is(String.class))
			return "String" + (nullable?"?":"");
		else if(type.is(JSONObject.class))
			return "[String: AnyObject]";
		else if(type.is(JSONArray.class))
			return "[[String: AnyObject]]";
		else if(type.name().equals("java.io.PrintWriter"))
			return "OutputStream";
		else if(type.isArray())
			return "["+$(type.getInnerTypes().get(0))+"]" + (nullable?"?":"");
		else if(type == GlobalTypes.jCrystal.VoidSuccessListener)
			return "@escaping ()->()";
		else if(type == GlobalTypes.jCrystal.ErrorListener)
			return "@escaping ((RequestError)->())";
		else if(GlobalTypes.jCrystal.isSuccessListener(type)) {
			return "@escaping (" + type.getInnerTypes().stream().map(f->$(f, false)).collect(Collectors.joining(", ")) + ") -> ()" + (nullable?"?":"");
		}
		else if(type.isIterable())
			return "[" + type.getInnerTypes().stream().map(f->$(f, false)).collect(Collectors.joining(", ")) + "]" + (nullable?"?":"");
		else
			return type.getSimpleName() + (nullable?"?":"");
	}

	@Override
	public String $V(IJType type, String name) {
		return name+ " : " + $(type);
	}

	@Override
	public void $ifNull(String param, Runnable code) {
		$if(param + " == nil", code);
	}

	@Override
	public void $ifNotNull(String param, Runnable code) {
		$if(param + " != nil", code);
	}
}
