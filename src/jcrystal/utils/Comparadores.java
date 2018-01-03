package jcrystal.utils;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Created by G on 11/26/2016.
 */
public class Comparadores {
    public static final Comparator<Field> comparatorFields = new Comparator<Field>() {
        @Override
        public int compare(Field o1, Field o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
}
