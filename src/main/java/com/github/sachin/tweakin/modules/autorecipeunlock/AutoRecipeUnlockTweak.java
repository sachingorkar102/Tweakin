package com.github.sachin.tweakin.modules.autorecipeunlock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Recipe;

// Permission: tweakin.autorecipeunlock
public class AutoRecipeUnlockTweak extends BaseTweak implements Listener{

    private List<NamespacedKey> recipes = new ArrayList<>();

    public AutoRecipeUnlockTweak(Tweakin plugin) {
        super(plugin, "auto-recipe-unlock");
    }

    @Override
    public void register() {
        super.register();
        loadRecipes();
        Bukkit.getOnlinePlayers().forEach(p -> discoverRecipes(p));
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        discoverRecipes(e.getPlayer());
    }

    public void loadRecipes(){
        recipes.clear();
        Bukkit.recipeIterator().forEachRemaining(recipe -> {
            if(recipe instanceof Keyed){
                recipes.add(((Keyed)recipe).getKey());
            }
        });
    }

    public void discoverRecipes(Player player){
        if(player.hasPermission("tweakin.autorecipeunlock")){
            for (NamespacedKey namespacedKey : recipes) {
                if(!player.hasDiscoveredRecipe(namespacedKey)){
                    player.discoverRecipe(namespacedKey);
                }
                
            }
        }
    }
    
}
