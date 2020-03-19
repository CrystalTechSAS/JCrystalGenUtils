package jcrystal.utils.langAndPlats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import jcrystal.types.IJType;
import jcrystal.types.WrapStringJType;

public abstract class AbsCodeBlock implements AbsICodeBlock {
	protected String prefijo = "";
	private final ArrayList<String> code = new ArrayList<>();
    public AbsCodeBlock(){}
    public AbsCodeBlock(int level){
        for(int e = 0; e < level; e++)prefijo+="\t";
    }
    @Override
	public final int $(String ins){
        this.add(ins);
        return code.size()-1;
    }
    @Override
	public final IF $if(String cond, Runnable block){
        if(cond == null || cond.trim().isEmpty())
            block.run();
        else
            this.$(buildIf(cond), block);
        return new IF();
    }
    @Override
	public final IF $if(boolean putif, String cond, Runnable block){
        if(cond == null || cond.trim().isEmpty() || !putif)
            block.run();
        else
            this.$(buildIf(cond), block);
        return new IF();
    }
    @Override
	public final void $if(String cond, String code){
	    if(cond==null || cond.trim().isEmpty())
		    this.$(code);
	    else
		    this.$(buildIf(cond)+"{"+code+"}");
    }
    @Override
	public final void $else_if(String cond, Runnable block){
        this.$("else " + buildIf(cond), block);
    }
    @Override
	public final void $else(Runnable block){
        this.$("else", block);
    }
    public final void incLevel(){
        prefijo += "\t";
    }
    public final void decLevel(){
        prefijo = prefijo.substring(1);
    }
    @Override
	public final void $(String pre, Runnable r){
        $(pre+"{");
        String lastPre = prefijo;
        prefijo += "\t";
        r.run();
        prefijo = lastPre;
        $("}");
    }
    /* (non-Javadoc)
	 * @see jcrystal.utils.langAndPlats.ICodeBlock#$(java.lang.String, java.lang.Runnable, java.lang.String)
	 */
    @Override
	public final void $(String pre, Runnable r, String pos){
        $(pre+"{");
        String lastPre = prefijo;
        prefijo += "\t";
        r.run();
        prefijo = lastPre;
        $("}"+pos);
    }
    
    public final void $(String pre, Runnable r, String pos, Runnable r2, String pos2){
        $(pre+"{");
        String lastPre = prefijo;
        prefijo += "\t";
        r.run();
        prefijo = lastPre;
        $("}"+pos+"{");
        prefijo += "\t";
        r2.run();
        prefijo = lastPre;
        $("}"+pos2);
        
    }

    public boolean add(String s) {
		return code.add(prefijo + s);
    }

    @Override
	public final void $catch(String ex, Runnable block){
        $("catch("+ex+"){");
        String lastPre = prefijo;
        prefijo += "\t";
        block.run();
        prefijo = lastPre;
        $("}");
    }
    @Override
	public final void $VoidCatch(String ex){
        $("catch("+ex+"){}");
    }
    @Override
	public final void $SingleCatch(String ex,String p){
        $("catch("+ex+"){"+p+"}");
    }
    @Override
	public ArrayList<String> getCode() {
		return code;
	}
    @Override
	public boolean isEmpty() {
		return code.isEmpty();
	}
    @Override
    public int size() {
    	return code.size();
    }
    @Override
	public void $append(AbsICodeBlock internal) {
		for(String line : internal.getCode())
			$(line);
	}
    @Override
	public abstract void $V(String tipo, String name, String valor);
    @Override
	public abstract IF $if_let(String tipo, String name, String valor, String where, Runnable block);

    @Override
	public abstract void $FE(String tipo, String name, String valor, Runnable block);
    @Override
	public abstract void $M(int modifiers, String retorno, String name, String params, String excepciones, Runnable block);
    @Override
	public abstract void $L(String pre, Lambda block, String pos);
    @Override
	public abstract String buildIf(String cond);
    
    @Override
    public abstract void $ifNull(String cond, Runnable code);
	@Override
    public abstract void $ifNotNull(String cond, Runnable code);

	
    public static abstract class Lambda implements Runnable{
        final String params;
        final String ret;
        public Lambda(String params){
            this.params = params;this.ret = null;
        }
        public Lambda(String params, String ret){
            this.params = params;
            this.ret = ret;
        }
    }
    public class IF{
        public final IF $else(String cond, Runnable run){
            $("else " + buildIf(cond), run);
            return this;
        }
        public final void $else(Runnable block){
            $("else", block);
        }
        public final void $else(final String block){
        	$("else", new Runnable() {
                @Override
                public void run() {
                    $(block);
                }
            });
        }
    }
    public static class P{
        public final IJType tipo;
        public final String nombre;
        public P(String tipo, String nombre){
        	this.tipo = new WrapStringJType(tipo);
          	this.nombre = nombre;
        }
        public P(IJType tipo, String nombre){
            this.tipo = tipo;
            this.nombre = nombre;
        }
        public final PL $(PL params) {
        	params.lista.add(0, this);
        	return params;
        }
    }
    public static class PL{
        public final ArrayList<P> lista;
        public static final PL EMPTY = new PL();
        public PL(){
            this.lista = new ArrayList<>();
        }
        public PL(java.util.List<P> list){
            this.lista = new ArrayList<>(list);
        }
        public PL(P...list){
            this.lista = new ArrayList<>(Arrays.asList(list));
        }
        protected PL(java.util.List<P> list, P...list2){
              this.lista = new ArrayList<>(list);
              this.lista.addAll(Arrays.asList(list2));
          }
        public final void add(P p){
        	lista.add(p);
        }
        public String collect(Function<P, String> mapper) {
        	return lista.stream().map(mapper).collect(Collectors.joining(", "));
        }
        public String collect(String prefix, Function<P, String> mapper) {
        	String ret = lista.stream().map(mapper).collect(Collectors.joining(", "));
        	if(ret.isEmpty())
        		return prefix;
        	return prefix+", "+ret;
        }
        public String collect(Function<P, String> mapper, String suffix) {
        	String ret = lista.stream().map(mapper).collect(Collectors.joining(", "));
        	if(ret.isEmpty())
        		return suffix;
        	return ret + ", "+suffix;
        }
        public void adding(P p,Runnable r) {
        	lista.add(p);
        	r.run();
        	lista.remove(lista.size()-1);
        }
        public void pop() {
        	lista.remove(lista.size()-1);
        }
    }
    
    public class B implements AbsICodeBlock{
    			public final AbsCodeBlock P = AbsCodeBlock.this; 
    			@Override public int $(String ins) {
				return AbsCodeBlock.this.$(ins);
			}
			@Override public IF $if(String cond, Runnable block) {
				return AbsCodeBlock.this.$if(cond, block);
			}
			@Override public IF $if(boolean putIf, String cond, Runnable block) {
				return AbsCodeBlock.this.$if(putIf, cond, block);
			}
			@Override public void $if(String cond, String code) {
				AbsCodeBlock.this.$if(cond, code);
			}
			@Override public void $else_if(String cond, Runnable block) {
				AbsCodeBlock.this.$else_if(cond, block);
			}
			@Override public void $else(Runnable block) {
				AbsCodeBlock.this.$else(block);
			}
			@Override public void $(String pre, Runnable r) {
				AbsCodeBlock.this.$(pre, r);
			}
			@Override public void $(String pre, Runnable r, String pos) {
				AbsCodeBlock.this.$(pre, r, pos);
			}
			@Override public void $catch(String ex, Runnable block) {
				AbsCodeBlock.this.$catch(ex, block);
			}
			@Override public void $VoidCatch(String ex) {
				AbsCodeBlock.this.$VoidCatch(ex);
			}
			@Override public void $SingleCatch(String ex, String p) {
				AbsCodeBlock.this.$SingleCatch(ex, p);
			}
			@Override public void $V(String tipo, String name, String valor) {
				AbsCodeBlock.this.$V(tipo, name, valor);
			}
			@Override public IF $if_let(String tipo, String name, String valor, String where, Runnable block) {
				return AbsCodeBlock.this.$if_let(tipo, name, valor, where, block);
			}
			@Override public void $FE(String tipo, String name, String valor, Runnable block) {
				AbsCodeBlock.this.$FE(tipo, name, valor, block);
			}
			@Override public void $M(int modifiers, String retorno, String name, String params, String excepciones, Runnable block) {
				AbsCodeBlock.this.$M(modifiers, retorno, name, params,  excepciones, block);
			}
			@Override public void $L(String pre, Lambda block, String pos) {
				AbsCodeBlock.this.$L(pre, block, pos);
			}
			@Override public String buildIf(String cond) {
				return AbsCodeBlock.this.buildIf(cond);
			}
			@Override
			public String $(IJType type) {
				return AbsCodeBlock.this.$(type);
			}
			@Override
			public String $V(IJType type, String name) {
				return AbsCodeBlock.this.$V(type, name);
			}
			@Override
			public ArrayList<String> getCode() {
				return AbsCodeBlock.this.getCode();
			}
			@Override
			public boolean isEmpty() {
				return AbsCodeBlock.this.isEmpty();
			}
			@Override
			public void $append(AbsICodeBlock internal) {
				AbsCodeBlock.this.$append(internal);
			}
			@Override
			public int size() {
				return AbsCodeBlock.this.size();
			}
			@Override
			public void $ifNull(String cond, Runnable code) {
				AbsCodeBlock.this.$ifNull(cond, code);
			}
			@Override
			public void $ifNotNull(String cond, Runnable code) {
				AbsCodeBlock.this.$ifNotNull(cond, code);
			}
    }
}
