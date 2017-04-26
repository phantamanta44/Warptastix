package io.github.phantamanta44.warptastix.condition;

import org.bukkit.entity.Player;

public interface ICondition {

    boolean meets(Player pl);

    void execute(Player pl);

    int weight();

}
