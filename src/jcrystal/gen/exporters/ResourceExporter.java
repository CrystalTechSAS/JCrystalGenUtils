package jcrystal.gen.exporters;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

public class ResourceExporter {


	public static void addResource(InputStream resource, Map<String, Object> config, File path) throws IOException{
		try(BufferedReader br = new BufferedReader(new InputStreamReader(resource)){
	        	public String readLine() throws IOException {
	        		String line = super.readLine();
	        		if(line != null)
	        			for(Entry<String, Object> val : config.entrySet())
	                		line = line.replace("#"+val.getKey(), ""+val.getValue());
	                return line;
	        	};
	        }; PrintWriter pw = new PrintWriter(path)){
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
