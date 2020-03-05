package jcrystal.utils.langAndPlats;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class HTMLCode extends ArrayList<String>{
	protected String prefijo = "";
	public HTMLCode(){}
	public HTMLCode(int level){
		for(int e = 0; e < level; e++)prefijo+="\t";
	}
	public int $(String ins){
		this.add(ins);
		return size()-1;
	}
	
	public int $(String tag, Runnable block){
		this.add("<"+tag+">");
		String lastPre = prefijo;
		prefijo+=" ";
		block.run();
		prefijo = lastPre;
		this.add("</"+tag+">");
		return size()-1;
	}
	public int $(JavascriptCode code){
		this.add("<script type=\"text/javascript\">");
		this.addAll(code.getCode());
		this.add("</script>");
		return size()-1;
	}
	public int $(String tag, String props, Runnable block){
		this.add("<"+tag+" " + props + ">");
		String lastPre = prefijo;
		prefijo+=" ";
		block.run();
		prefijo = lastPre;
		this.add("</"+tag+">");
		return size()-1;
	}
	public int DIV(String props, Runnable block){
		this.add("<div " + props + ">");
		String lastPre = prefijo;
		prefijo+=" ";
		block.run();
		prefijo = lastPre;
		this.add("</div>");
		return size()-1;
	}
	@Override public boolean add(String s) {
		return super.add(prefijo + s);
	}
	public final void save(File file) {
		try {
			Files.write(file.toPath(), this, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
