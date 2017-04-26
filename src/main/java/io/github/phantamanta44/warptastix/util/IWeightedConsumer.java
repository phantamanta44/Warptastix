package io.github.phantamanta44.warptastix.util;

import java.util.function.Consumer;

public interface IWeightedConsumer<T> extends Consumer<T>, Comparable<IWeightedConsumer<T>> {

    int weight();

    @Override
    default IWeightedConsumer<T> andThen(Consumer<? super T> after) {
        return weight(weight(), after);
    }

    default int compareTo(IWeightedConsumer<T> o) {
        return o.weight() - weight();
    }

    static <T> IWeightedConsumer<T> weight(int weight, Consumer<? super T> consumer) {
        return new IWeightedConsumer<T>() {
            @Override
            public int weight() {
                return weight;
            }

            @Override
            public void accept(T t) {
                consumer.accept(t);
            }
        };
    }

}
