package jcrystal.utils;

import java.util.List;

public class StreamUtils {
	public interface ForEachWithIndex<E>{
		public void consume(int index, E element);
	}
	public static <E> void forEachWithIndex(List<E> list, ForEachWithIndex<E> consumer){
		int i = 0;
		for(E e : list)
			consumer.consume(i++, e);
	}
}
