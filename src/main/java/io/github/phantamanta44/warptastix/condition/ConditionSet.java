package io.github.phantamanta44.warptastix.condition;

import io.github.phantamanta44.warptastix.util.IWeightedConsumer;
import io.github.phantamanta44.warptastix.util.TreeBranch;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.function.Consumer;

public class ConditionSet {

    private final TreeBranch<ICondition> cTree;

    public ConditionSet() {
        this.cTree = new TreeBranch<>(new NoOpCondition());
    }

    public Consumer<Player> meets(Player pl) {
        return iterateBranches(pl, cTree);
    }

    private IWeightedConsumer<Player> iterateBranches(Player pl, TreeBranch<ICondition> root) {
        return root.hasChildren()
            ? root.children(c -> c.meets(pl))
                    .map(b -> iterateBranches(pl, b))
                    .filter(Objects::nonNull)
                    .sorted()
                    .findFirst()
                    .map(c -> c.andThen(root.get()::execute))
                    .orElse(null)
            : IWeightedConsumer.weight(root.get().weight(), root.get()::execute);
    }

    public TreeBranch<ICondition> addBranch(ICondition condition) {
        return cTree.child(condition);
    }

    private static class NoOpCondition implements ICondition {

        @Override
        public boolean meets(Player pl) {
            return true;
        }

        @Override
        public void execute(Player pl) {
            // NO-OP
        }

        @Override
        public int weight() {
            return -1;
        }

    }

}
