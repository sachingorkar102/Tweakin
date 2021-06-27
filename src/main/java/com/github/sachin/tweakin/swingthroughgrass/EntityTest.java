package com.github.sachin.tweakin.swingthroughgrass;

import com.google.common.base.Predicate;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityTest<E> implements Predicate<Entity> {
    @Override
    public boolean test(Entity e) {
        return !(e instanceof Player);
    }

    @Override
    public boolean apply(Entity input) {
        return false;
    }
}
