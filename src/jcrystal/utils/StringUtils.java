package jcrystal.utils;

/**
 * Created by G on 11/10/2016.
 */
public class StringUtils {
    public static String capitalize(String h){
        h = h.trim();
        if(h.isEmpty())
            return h;
        return h.substring(0,1).toUpperCase() + h.substring(1);
    }
    public static String lowercalize(String h){
        return h.substring(0,1).toLowerCase() + h.substring(1);
    }
    public static String camelizar(String v){
        if(v.contains("_")){
            String[] vals = v.split("_");
            String ret = "";
            for(int e = 0; e < vals.length; e++)
                ret += camelizar(vals[e]);
            return ret;
        }else
            return v.substring(0, 1).toUpperCase() + v.substring(1).toLowerCase();
    }
}
