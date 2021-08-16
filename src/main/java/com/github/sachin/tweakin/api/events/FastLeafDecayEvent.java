package com.github.sachin.tweakin.api.events;

import com.github.sachin.tweakin.fastleafdecay.FastLeafDecayTweak;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FastLeafDecayEvent extends Event implements Cancellable{

    private FastLeafDecayTweak tweak;
    private Block leafBlock;
    private boolean isCancelled;
    private static final HandlerList HANDLERS = new HandlerList();

    public FastLeafDecayEvent(FastLeafDecayTweak tweak,Block leafBlock){
        this.tweak = tweak;
        this.leafBlock = leafBlock;
        this.isCancelled = false;
    }

    public FastLeafDecayTweak getTweak() {
        return tweak;
    }

    public Block getLeafBlock() {
        return leafBlock;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    
}
