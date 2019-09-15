package springdatajdbc.util;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@SuppressWarnings("ALL")
public class CollectionUtil {
    private CollectionUtil() {
    }

    public static <T> Set<T> asSet(T... values) {
        return Stream.<T>of(values).collect(toSet());
    }

    public static <T> Set<T> asSet(Set<T> base, T... values) {
        return Stream.<T>concat(base.stream(), Stream.of(values)).collect(toSet());
    }

    public static <T> List<T> extendList(List<T> l, T... ts) {
        return Stream.<T>concat(l.stream(), Stream.of(ts)).collect(toList());
    }

    public static <T> T first(List<T> tl) {
        if (tl.isEmpty()) throw new RuntimeException("Can not take 1st from empty list");
        return tl.get(0);
    }

    public static <T, X> List<X> map(List<T> l, Function<T, X> f) {
        return l.stream().map(f).collect(toList());
    }
}
