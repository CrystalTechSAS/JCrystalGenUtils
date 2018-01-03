package jcrystal.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by G on 11/28/2016.
 */
public class ListUtils {
    public static <T> List<T> join(Collection<T> l1, Collection<T> l2){
        List<T> ret = new ArrayList<>();
        ret.addAll(l1);
        ret.addAll(l2);
        return ret;
    }
}
