package com.github.sachin.tweakin.modules.swingthroughgrass;

import com.google.common.base.Predicate;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityTest<E> implements Predicate<Entity> {

    private Player owner;

    public EntityTest(Player owner){
        this.owner = owner;
    }
    @Override
    public boolean test(Entity e) {
        return owner.getUniqueId() != e.getUniqueId();
    }

    @Override
    public boolean apply(Entity input) {
        return false;
    }
}
