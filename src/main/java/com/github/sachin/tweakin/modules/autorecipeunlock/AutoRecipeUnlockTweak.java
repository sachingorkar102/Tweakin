package com.github.sachin.tweakin.modules.autorecipeunlock;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

// Permission: tweakin.autorecipeunlock
@Tweak(name = "auto-recipe-unlock",clashingTeaksTweak = "Unlock All Recipes")
public class AutoRecipeUnlockTweak extends BaseTweak implements Listener{

    private List<NamespacedKey> recipes = new ArrayList<>();


    @Override
    public void register() {
        super.register();
        loadRecipes();
        Bukkit.getOnlinePlayers().forEach(p -> discoverRecipes(p));
    }

    @Override
    public void unregister() {
        super.unregister();
        recipes.clear();
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        discoverRecipes(e.getPlayer());
    }

    public void loadRecipes(){
        
        Bukkit.recipeIterator().forEachRemaining(recipe -> {
            if(recipe instanceof Keyed){
                recipes.add(((Keyed)recipe).getKey());
            }
        });
    }

    public void discoverRecipes(Player player){
        if(hasPermission(player, Permissions.AUTORECIPEUNLOCK)){
            for (NamespacedKey namespacedKey : recipes) {
                if(!player.hasDiscoveredRecipe(namespacedKey)){
                    player.discoverRecipe(namespacedKey);
                }
                
            }
        }
    }
    
}
