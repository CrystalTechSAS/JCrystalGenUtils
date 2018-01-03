package jcrystal.utils.langAndPlats;

import java.util.Comparator;

/**
 * Created by AndreaC on 29/12/2016.
 */
public class ClassUtils {
	public final static Comparator<Class<?>> CLASS_NAME_COMPARATOR = (Class<?> o1, Class<?> o2)-> o1.getName().compareTo(o2.getName()) ;
    public static boolean isPrimitiveObjectType(Class<?> tipo){
        return tipo == Integer.class || tipo == Long.class || tipo == Double.class || tipo == Float.class || tipo == Boolean.class || tipo == Character.class || tipo == Byte.class || tipo == Short.class;
    }
    public static Class<?> getObjectType(Class<?> tipo){
        switch (tipo.getSimpleName()){
            case "int": return Integer.class;
            case "long": return Long.class;
            case "double": return Double.class;
            case "float": return Float.class;
            case "boolean": return Boolean.class;
            case "char": return Character.class;
            case "byte": return Byte.class;
            case "short": return Short.class;
        }
        return tipo;
    }
}
