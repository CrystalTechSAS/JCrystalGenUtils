package jcrystal.utils;

import jcrystal.utils.langAndPlats.AbsCodeBlock;
import jcrystal.utils.langAndPlats.AbsJavaCode;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;


public class JavaCodeSource {
    public ArrayList<String> _source = new ArrayList<>();
    public TreeMap<String, Section> sections = new TreeMap<>();
    private int ultimoCierre;
    public JavaCodeSource(File file)throws IOException{
        ArrayList<String> code = new ArrayList<>(Files.readAllLines(file.toPath()));
        for(int e = 0; e < code.size(); e++){
            if(isStartTag(code.get(e))){
                String tag = code.get(e).trim();
                final String tagName = tag.substring(2, tag.length() - 2).trim();
                ArrayList<String> sec = new ArrayList<>();
                int i = e + 1;
                for(;i < code.size() && !isEndTag(code.get(i)); i++) {
                    if(isStartTag(code.get(i)))
                        throw new NullPointerException("Invalid code tag ");
                    sec.add(code.get(i));
                }
                if(i == code.size() || sections.containsKey(tagName))
                    throw new NullPointerException("Invalid code tag " + tagName);
                sections.put(tagName, new Section(_source.size(), tagName, sec));
                e = i;
                _source.add("");
            }else {
                if (code.get(e).trim().equals("}"))
                    ultimoCierre = _source.size();
                _source.add(code.get(e));
            }
        }
    }
    public void addSecction(String name, List<String> code){
        if(sections.containsKey(name)){
            sections.get(name).code = code;
        }else{
            sections.put(name, new Section(-1, name, code));
        }
    }
    public AbsJavaCode addCodeBlock(String name, int level){
    		AbsJavaCode codigo = new AbsJavaCode(level);
        if(sections.containsKey(name)){
            sections.get(name).code = codigo;
        }else{
            sections.put(name, new Section(-1, name, codigo));
        }
        return codigo;
    }
    public<T extends AbsCodeBlock> T addCodeBlock(String name, T block){
    		if(sections.containsKey(name)){
    			sections.get(name).code = block;
    		}else{
    			sections.put(name, new Section(-1, name, block));
    		}
    		return block;
    }
    private boolean isStartTag(String line){
        line = line.trim();
        return line.startsWith("/* ") && line.endsWith(" */");
    }
    private boolean isEndTag(String line){
        line = line.trim();
        return line.equals("/* END */");
    }
    public void save(PrintWriter pw)throws IOException{
        List<Section> secciones = new ArrayList<>(sections.values());
        Collections.sort(secciones);
        int cSec = 0;
        for(int e = 0; e < ultimoCierre; e++){
            while(cSec < secciones.size() && secciones.get(cSec).pos < e)
                cSec++;
            if(cSec < secciones.size() && secciones.get(cSec).pos == e){
                secciones.get(cSec).printSection(pw);
                cSec++;
            }else
                pw.println(_source.get(e));
        }

        for(;cSec < secciones.size(); cSec++)
            secciones.get(cSec).printSection(pw);

        for(Section sec : secciones)
            if(sec.pos == -1)
                sec.printSection(pw);

        for(int e = ultimoCierre; e < _source.size(); e++)
            pw.println(_source.get(e));
    }
    private static class Section implements Comparable<Section>{
        private int pos;
        private String name;
        private List<String> code;

        public Section(int pos, String name, List<String> code) {
            this.pos = pos;
            this.name = name;
            this.code = code;
        }

        @Override
        public int compareTo(Section o) {
            return Integer.compare(pos, o.pos);
        }
        private void printSection(PrintWriter pw){
            pw.println("/* " + name + " */");
            for(String line : code)
                pw.println(line);
            pw.println("/* END */");
        }
    }
}
