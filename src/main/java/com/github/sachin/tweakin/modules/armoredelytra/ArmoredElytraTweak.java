package com.github.sachin.tweakin.modules.armoredelytra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.nbtapi.NBTItem;
import com.github.sachin.tweakin.utils.InventoryUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

// Permission: tweakin.armoredelytra.craft
public class ArmoredElytraTweak extends TweakItem implements Listener{

    private final String CHEST_KEY = "armored-elytra-chestplate";
    private final String ELYTRA_KEY = "armored-elytra-elytra";

    public ArmoredElytraTweak(Tweakin plugin) {
        super(plugin, "armored-elytra");
        
    }

    @EventHandler
    public void onItemBurn(EntityDamageByBlockEvent e){
        if(e.getDamager() == null) return;
        if(e.getDamager().getType() != Material.LAVA) return;
        if(e.getEntity().getType() == EntityType.DROPPED_ITEM){
            Item item = (Item) e.getEntity();
            if(item.getItemStack().getType() == Material.ELYTRA){
                if(isSimilar(item.getItemStack())){
                    ItemStack itemStack = item.getItemStack();
                    NBTItem nbti = new NBTItem(itemStack);
                    if(nbti.hasKey(CHEST_KEY) && nbti.hasKey(ELYTRA_KEY)){
                        ItemStack chest = InventoryUtils.deserializeItem(nbti.getString(CHEST_KEY));
                        ItemStack elytra = InventoryUtils.deserializeItem(nbti.getString(ELYTRA_KEY));
                        ItemMeta meta = elytra.getItemMeta();
                        Damageable damage = (Damageable) meta;
                        damage.setDamage(((Damageable)itemStack.getItemMeta()).getDamage());
                        elytra.setItemMeta(meta);
                        item.getWorld().dropItem(item.getLocation(), chest);
                        item.getWorld().dropItem(item.getLocation(), elytra);
                        item.remove();
                    }    
                }
            }
        }
    }


    @EventHandler
    public void onAnvilDrop(EntityChangeBlockEvent e){
        
        if(e.getTo() == Material.ANVIL || e.getTo() == Material.CHIPPED_ANVIL || e.getTo() == Material.DAMAGED_ANVIL){
            List<Entity> list = plugin.getNmsHelper().getEntitiesWithinRadius(1, e.getEntity());
        
            for(Entity en : list){
                if(en instanceof Item){
                    Item i = (Item) en;
                    ItemStack item = i.getItemStack();
                    if(isSimilar(item)){
                        NBTItem nbti = new NBTItem(item);
                        if(nbti.hasKey(CHEST_KEY) && nbti.hasKey(ELYTRA_KEY)){
                            ItemStack chest = InventoryUtils.deserializeItem(nbti.getString(CHEST_KEY));
                            ItemStack elytra = InventoryUtils.deserializeItem(nbti.getString(ELYTRA_KEY));
                            ItemMeta meta = elytra.getItemMeta();
                            Damageable damage = (Damageable) meta;
                            damage.setDamage(((Damageable)item.getItemMeta()).getDamage());
                            elytra.setItemMeta(meta);
                            Location loc = e.getBlock().getLocation().clone().add(0.5,0.9,0.5);
                            i.getWorld().dropItem(loc, chest);
                            i.getWorld().dropItem(loc, elytra);
                            i.remove();
                        }
                         
                    }
                }
            }
        }
    }


    @EventHandler
    public void onElytraCombine(PrepareAnvilEvent e){
        if(e.getView().getBottomInventory().getHolder() instanceof Player){
            Player player = (Player) e.getView().getBottomInventory().getHolder();
            if(!player.hasPermission("tweakin.armoredelytra.craft")) return;
            new BukkitRunnable(){
                @Override
                public void run() {
                    AnvilInventory inv = e.getInventory();
                    ItemStack item1 = inv.getItem(0);
                    ItemStack item2 = inv.getItem(1);
                    if(item1 != null && item2 != null){
                        if(getConfig().getStringList("combineable-materials").contains(item1.getType().toString()) && item2.getType() == Material.ELYTRA){
                            if(isSimilar(item2)) return;
                            ItemStack combinedElytra = getItem().clone();
                            ItemMeta meta = combinedElytra.getItemMeta();
                            Map<Enchantment,Integer> enchants = new HashMap<>();
                            int armor = 0;
                            int toughness = 0;
                            double knockbackres = 0.0;
                            switch(item1.getType()){
        
                                case LEATHER_CHESTPLATE:
                                    armor = 3;
                                    break;
                                case CHAINMAIL_CHESTPLATE:
                                case GOLDEN_CHESTPLATE:
                                    armor = 5;
                                    break;
                                case IRON_CHESTPLATE:
                                    armor = 6;
                                    break;
                                case DIAMOND_CHESTPLATE:
                                    armor = 8;
                                    toughness = 2;
                                    break;
                                case NETHERITE_CHESTPLATE:
                                    armor = 8;
                                    toughness = 3;
                                    knockbackres = 0.1;
                                    break;            
                                default:
                                    break;
                                    
                            }
                            if(armor != 0){
                                AttributeModifier mod = new AttributeModifier(UUID.randomUUID(), "generic.armor", armor, Operation.ADD_NUMBER, EquipmentSlot.CHEST);
                                meta.addAttributeModifier(Attribute.GENERIC_ARMOR, mod);
                            }
                            if(toughness != 0){
                                AttributeModifier mod = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", toughness, Operation.ADD_NUMBER, EquipmentSlot.CHEST);
                                meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, mod);
                            }
                            if(knockbackres != 0){
                                AttributeModifier mod = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", knockbackres, Operation.ADD_NUMBER, EquipmentSlot.CHEST);
                                meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, mod);
                            }
                            for(Enchantment ench : item1.getEnchantments().keySet()){
                                enchants.put(ench, item1.getEnchantments().get(ench));
                            }
                            for(Enchantment ench : item2.getEnchantments().keySet()){
                                if(enchants.containsKey(ench)){
                                    if(enchants.get(ench) < item2.getEnchantments().get(ench)){
                                        enchants.put(ench, item2.getEnchantments().get(ench));
                                    }
                                }
                                else{
                                    enchants.put(ench, item2.getEnchantments().get(ench));
                                }
                            }
                            combinedElytra.setItemMeta(meta);
                            for(Enchantment ench : enchants.keySet()){
                                combinedElytra.addUnsafeEnchantment(Enchantment.getByKey(ench.getKey()), enchants.get(ench));
                            }
                            NBTItem nbti = new NBTItem(combinedElytra);
                            nbti.setString(CHEST_KEY, InventoryUtils.serializeItem(item1));
                            nbti.setString(ELYTRA_KEY, InventoryUtils.serializeItem(item2));
                            combinedElytra = nbti.getItem();
                            ItemMeta elytraMeta = combinedElytra.getItemMeta();
                            elytraMeta.setDisplayName(inv.getRenameText());
                            combinedElytra.setItemMeta(elytraMeta);
                            inv.setRepairCost(getConfig().getInt("cost",10));
                            inv.setItem(2, combinedElytra);
                        }
                    }
                    
                }
            }.runTaskLater(plugin, 2);
        }
    }


    
}
