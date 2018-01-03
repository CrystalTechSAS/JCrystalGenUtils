package jcrystal.utils;

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
    @Override
    public String toString() {
        return act;
    }
}
