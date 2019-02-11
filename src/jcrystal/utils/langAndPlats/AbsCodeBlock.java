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

import jcrystal.preprocess.descriptions.IJType;
import jcrystal.preprocess.descriptions.WrapStringJType;
import jcrystal.utils.StringSeparator;

public abstract class AbsCodeBlock extends ArrayList<String> implements AbsICodeBlock {
	private static final long serialVersionUID = -994460027594152244L;
	protected String prefijo = "";
    public AbsCodeBlock(){}
    public AbsCodeBlock(int level){
        for(int e = 0; e < level; e++)prefijo+="\t";
    }
    @Override
	public final int $(String ins){
        this.add(ins);
        return size()-1;
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

    @Override public boolean add(String s) {
    		return super.add(prefijo + s);
    }

    public void saveFile(File out){
        try(PrintWriter pw = new PrintWriter(out)){
            for(String h : this)
                pw.println(h);
        } catch (FileNotFoundException e) {
			NullPointerException ex = new NullPointerException();
			ex.initCause(e);
			throw ex;
		}
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
	public abstract void $V(String tipo, String name, String valor);
    @Override
	public abstract IF $if_let(String tipo, String name, String valor, String where, Runnable block);

    @Override
	public abstract void $FE(String tipo, String name, String valor, Runnable block);
    @Override
	public abstract void $M(int modifiers, String retorno, String name, PL params, Runnable block);
    @Override
	public abstract void $M(int modifiers, String retorno, String name, StringSeparator params, Runnable block);
    @Override
	public abstract void $M(int modifiers, String retorno, String name, StringSeparator params, String excepciones, Runnable block);
    @Override
	public abstract void $M(int modifiers, String tipoRetorno, String name, PL params, final Runnable block, final String retorno);
    @Override
	public abstract void $L(String pre, Lambda block, String pos);
    @Override
	public abstract String buildIf(String cond);
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
        protected P(String tipo, String nombre){
              this.tipo = new WrapStringJType(tipo);
              this.nombre = nombre;
          }
        protected P(IJType tipo, String nombre){
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
        protected PL(java.util.List<P> list){
            this.lista = new ArrayList<>(list);
        }
        protected PL(P...list){
            this.lista = new ArrayList<>(Arrays.asList(list));
        }
        protected PL(java.util.List<P> list, P...list2){
              this.lista = new ArrayList<>(list);
              this.lista.addAll(Arrays.asList(list2));
          }
        public final void add(P p){
        	lista.add(p);
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
			@Override public void $M(int modifiers, String retorno, String name, PL params, Runnable block) {
				AbsCodeBlock.this.$M(modifiers, retorno, name, params, block);
			}
			@Override public void $M(int modifiers, String retorno, String name, PL params, String excepciones, Runnable block) {
				AbsCodeBlock.this.$M(modifiers, retorno, name, params, excepciones, block);
			}
			@Override public void $M(int modifiers, String retorno, String name, StringSeparator params, Runnable block) {
				AbsCodeBlock.this.$M(modifiers, retorno, name, params, block);
			}
			@Override public void $M(int modifiers, String retorno, String name, StringSeparator params, String excepciones, Runnable block) {
				AbsCodeBlock.this.$M(modifiers, retorno, name, params,  excepciones, block);
			}
			@Override public void $M(int modifiers, String tipoRetorno, String name, PL params, Runnable block, String retorno) {
				AbsCodeBlock.this.$M(modifiers, tipoRetorno, name, params, block, retorno);
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
    }
    public static void addResource(InputStream resource, Map<String, Object> config, File out) throws Exception{
	        out.getParentFile().mkdirs();
	        
	        try(BufferedReader br = new BufferedReader(new InputStreamReader(resource)){
	        	public String readLine() throws IOException {
	        		String line = super.readLine();
	        		if(line != null)
	        			for(Entry<String, Object> val : config.entrySet())
	                		line = line.replace("#"+val.getKey(), ""+val.getValue());
	                return line;
	        	};
	        }; PrintWriter pw = new PrintWriter(out)){
	        	for(String line; (line = br.readLine())!=null; ){
	        		if(line.startsWith("#IF")){
	            		final Boolean val = (Boolean)config.get(line.substring(3).trim());
	            		ArrayList<String> IF = new ArrayList<>();
	            		ArrayList<String> ELSE = new ArrayList<>();
	            		for(;!(line=br.readLine()).startsWith("#");IF.add(line));
	            		if(line.startsWith("#ELSE"))
	            			for(;!(line=br.readLine()).startsWith("#");ELSE.add(line));
	            		if(!line.startsWith("#ENDIF"))
	            			throw new NullPointerException();
	            		(val?IF:ELSE).stream().forEach(pw::println);
	            	}else 
	            		pw.println(line);
	            }
	        }
	    }
}
