package com.github.sachin.tweakin.modules.nopotionglint;

import java.util.ArrayList;
import java.util.List;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

public class NoPotionGlintTweak extends BaseTweak implements Listener{

    public NoPotionGlintTweak(Tweakin plugin) {
        super(plugin, "no-potion-glint");
    }

    @EventHandler
    public void onBrew(BrewEvent e){
        BrewerInventory inv = e.getContents();
        new BukkitRunnable(){
            public void run() {
                for(int i=0;i<3;i++){
                    ItemStack item = inv.getItem(i);
                    if(item != null && !item.getType().isAir() && (item.getItemMeta() instanceof PotionMeta)){
                        PotionMeta meta = (PotionMeta) item.getItemMeta();
                        PotionType type = meta.getBasePotionData().getType();
                        ItemStack resultStack = new ItemStack(item.getType());
                        PotionMeta resultMeta = (PotionMeta) resultStack.getItemMeta();
                        String potionMaterial = null;
                        List<String> lore = new ArrayList<>();
                        switch (item.getType()) {
                            case POTION:
                                potionMaterial = "Potion";
                                break;
                            case SPLASH_POTION:
                                potionMaterial = "Splash Potion";
                            case LINGERING_POTION:
                                potionMaterial = "Lingeing Potion";
                                break;
                            default:
                                potionMaterial = "Potion";    
                        }
                        if(type.getEffectType() != null){
                            switch (type) {
                                case STRENGTH:
                                    PotionData data = meta.getBasePotionData();
                                    boolean isLingering = !(item.getType()==Material.POTION || item.getType()==Material.SPLASH_POTION);
                                    String duration = null;
                                    String amplifier = data.isUpgraded() ? "II" : "I";
                                    String attackDamage = data.isUpgraded() ? "6" : "3";
                                    
                                    if(isLingering){
                                        if(!data.isExtended() && amplifier.equals("I")){
                                            duration = "0:45";
                                        }
                                        else if(data.isExtended() && amplifier.equals("I")){
                                            duration = "2:00";
                                        }
                                        else if(data.isExtended() && amplifier.equals("II")){
                                            duration = "0:22";
                                        }
                                    }
                                    if(!data.isExtended() && !isLingering){
                                        duration = "3:00";
                                    }
                                    else if(data.isExtended() && amplifier.equals("I")){
                                        duration = "8:00";
                                    }
                                    else if(data.isExtended() && amplifier.equals("II")){
                                        duration = "1:30";
                                    }

                                    lore.add(ChatColor.translateAlternateColorCodes('&', "&9Strength "+amplifier+" ("+duration+")"));
                                    lore.add("");
                                    lore.add(ChatColor.translateAlternateColorCodes('&', "&5When Applied:"));
                                    lore.add(ChatColor.translateAlternateColorCodes('&', "&9+"+attackDamage+" Attack Damage"));
                                    resultMeta.setDisplayName(ChatColor.WHITE+potionMaterial+" of Strength");
                                    resultMeta.setLore(lore);
                                    resultMeta.setColor(meta.getColor());
                                    resultMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                                    resultStack.setItemMeta(resultMeta);
                                    inv.setItem(i, resultStack);
                                    break;

                            
                                default:
                                    break;
                            }
                        }
                    }
                }

            };
        }.runTaskLater(plugin, 1);
    }


    
    
}
