package com.ecom.sale.util;

import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Component
public class UpdateUtils {

    public <T> boolean updateIfChanged(Supplier<T> getter,
                                       Consumer<T> setter,
                                       T newValue) {
        T currentVal = getter.get();
        if (currentVal != null && !Objects.equals(currentVal, newValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }
}
