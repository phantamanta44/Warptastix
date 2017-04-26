package io.github.phantamanta44.warptastix.util;

/**
 * Represents an immutable 2-tuple (i.e. pair of objects).
 * @param <A> The type of object A.
 * @param <B> The type of object B.
 * @author Evan Geng
 */
public class Pair<A, B> {

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }

    private final A a;
    private final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object o) {
        return o instanceof Pair && ((Pair)o).a.equals(a) && ((Pair)o).b.equals(b);
    }

    @Override
    public String toString() {
        return "(" + a.toString() + ", " + b.toString() + ")";
    }

}
