package jcrystal.utils;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class StreamUtils {
	public static <E> void forEachWithIndex(List<E> list, BiConsumer<Integer, E> consumer){
		int i = 0;
		for(E e : list)
			consumer.accept(i++, e);
	}
	public static <E,X> Stream<X> mapWithIndex(Stream<E> list, BiFunction<Integer, E,X> consumer){
		int[] i = {0};
		return list.map(f->consumer.apply(i[0]++, f));
	}
	public static <E,X> Stream<X> mapWithIndex(List<E> list, BiFunction<Integer, E,X> consumer){
		int[] i = {0};
		return list.stream().map(f->consumer.apply(i[0]++, f));
	}
}
