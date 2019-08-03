package jcrystal.utils;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by G on 11/20/2016.
 */
public class StringSeparator {
    String act = "";
    String separator;
    public StringSeparator(char separator){
        this.separator = Character.toString(separator);
    }
    public StringSeparator(String separator){
        this.separator = separator;
    }
    public StringSeparator add(StringSeparator nuevo){
        if(act.isEmpty())
            act = nuevo.toString();
        else if(!nuevo.isEmpty())
            act += separator + nuevo;
        return this;
    }
    public StringSeparator add(Stream<String> nuevos){
	    nuevos.forEach(this::add);
	    return this;
    }
    public StringSeparator add(List<String> nuevos){
	    nuevos.forEach(this::add);
	    return this;
    }
    public StringSeparator add(String nuevo){
        if(act.isEmpty())
            act = nuevo;
        else act += separator + nuevo;
        return this;
    }
    public StringSeparator addFirst(String nuevo){
        if(act.isEmpty())
            act = nuevo;
        else act = nuevo + separator + act;
        return this;
    }
    public final boolean isEmpty(){
        return act.isEmpty();
    }
    public void clear() {
	    act = "";
    }
    @Override
    public String toString() {
        return act;
    }
}
