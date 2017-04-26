package io.github.phantamanta44.warptastix.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TreeBranch<T> {

    private final T value;
    private final Set<TreeBranch<T>> children;

    public TreeBranch(T value) {
        this.value = value;
        this.children = new HashSet<>();
    }

    public TreeBranch() {
        this(null);
    }

    public T get() {
        return value;
    }

    public boolean hasValue() {
        return value != null;
    }

    public Stream<TreeBranch<T>> children() {
        return children.stream();
    }

    public Stream<TreeBranch<T>> children(Predicate<T> filter) {
        return children().filter(TreeBranch::hasValue).filter(b -> filter.test(b.get()));
    }

    public TreeBranch<T> child(T value) {
        TreeBranch<T> branch = new TreeBranch<>(value);
        children.add(branch);
        return branch;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

}