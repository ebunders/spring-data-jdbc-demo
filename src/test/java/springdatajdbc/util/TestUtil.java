package springdatajdbc.util;

import java.util.Optional;

public class TestUtil {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> T optionalValue(Optional<T> o) {
        return o.orElseThrow(() -> new RuntimeException("value not found"));
    }
}
