package jcrystal.utils.langAndPlats;

import java.util.ArrayList;
import java.util.Collection;

public class HTMLCode{
	protected ArrayList<String> code = new ArrayList<>();
	protected String prefijo = "";
	public HTMLCode(){}
	public HTMLCode(int level){
		for(int e = 0; e < level; e++)prefijo+="\t";
	}
	public int $(String ins){
		this.add(ins);
		return code.size()-1;
	}
	public void addAll(Collection<String> data) {
		for(String h : data)
			$(h);
	}
	public int $(String tag, Runnable block){
		this.add("<"+tag+">");
		String lastPre = prefijo;
		prefijo+=" ";
		block.run();
		prefijo = lastPre;
		this.add("</"+tag+">");
		return code.size()-1;
	}
	public int $(JavascriptCode code){
		this.add("<script type=\"text/javascript\">");
		this.addAll(code.getCode());
		this.add("</script>");
		return code.size()-1;
	}
	public int $(String tag, String props, Runnable block){
		this.add("<"+tag+" " + props + ">");
		String lastPre = prefijo;
		prefijo+=" ";
		block.run();
		prefijo = lastPre;
		this.add("</"+tag+">");
		return code.size()-1;
	}
	public int DIV(String props, Runnable block){
		this.add("<div " + props + ">");
		String lastPre = prefijo;
		prefijo+=" ";
		block.run();
		prefijo = lastPre;
		this.add("</div>");
		return code.size()-1;
	}
	public boolean add(String s) {
		return code.add(prefijo + s);
	}
	public ArrayList<String> getCode() {
		return code;
	}
	public class B extends HTMLCode{
		public final int $(String ins){
			return HTMLCode.this.$(ins);
		}
		public final int $(String tag, Runnable block){
			return HTMLCode.this.$(tag, block);
		}
		public final int $(JavascriptCode code){
			return HTMLCode.this.$(code);
		}
		public final int $(String tag, String props, Runnable block){
			return HTMLCode.this.$(tag, props, block);
		}
		public final int DIV(String props, Runnable block){
			return HTMLCode.this.DIV(props, block);
		}
		@Override public boolean add(String s) {
			return HTMLCode.this.add(s);
		}
	}
}
