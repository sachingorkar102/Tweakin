package com.github.sachin.tweakin.api.events;

import com.github.sachin.tweakin.fastleafdecay.FastLeafDecayTweak;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.LeavesDecayEvent;

public class FastLeafDecayEvent extends LeavesDecayEvent{

    private FastLeafDecayTweak tweak;
    private Block leafBlock;
    private static final HandlerList HANDLERS = new HandlerList();

    public FastLeafDecayEvent(FastLeafDecayTweak tweak,Block leafBlock){
        super(leafBlock);
        this.tweak = tweak;
        this.leafBlock = leafBlock;
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


    
}
