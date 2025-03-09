package com.github.sachin.tweakin.modules.armoredelytra;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.compat.AdvancedEnchantments;
import com.github.sachin.tweakin.compat.EnchantsSquaredCompat;
import com.github.sachin.tweakin.compat.ExcellentEnchantsCompat;
import com.github.sachin.prilib.nms.NBTItem;
import com.github.sachin.tweakin.utils.InventoryUtils;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.TConstants;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// Permission: tweakin.armoredelytra.craft
@Tweak(name = "armored-elytra",clashingTeaksTweak = "Armored Elytra")
public class ArmoredElytraTweak extends TweakItem implements Listener{

    private final String CHEST_KEY = "armored-elytra-chestplate";
    private final String ELYTRA_KEY = "armored-elytra-elytra";


    @EventHandler
    public void onAnvilDestroyItem(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof FallingBlock)) return;
        FallingBlock block = (FallingBlock) e.getDamager();
        if(!TConstants.ANVILS.contains(block.getBlockData().getMaterial())) return;
        if(e.getEntity().getType() == Registry.ENTITY_TYPE.get(NamespacedKey.minecraft("item"))){
            Item item = (Item) e.getEntity();
            if(item.getItemStack().getType() == Material.ELYTRA){
                if(isSimilar(item.getItemStack())){
                    e.setCancelled(true);

                }
            }
        }
    }

    @EventHandler
    public void onItemBurn(EntityDamageByBlockEvent e){

        if(e.getDamager() == null) return;
        if(e.getDamager().getType() != Material.LAVA) return;
        if(e.getEntity().getType() == Registry.ENTITY_TYPE.get(NamespacedKey.minecraft("item"))){
            Item item = (Item) e.getEntity();
            if(item.getItemStack().getType() == Material.ELYTRA){
                if(isSimilar(item.getItemStack())){
                    ItemStack itemStack = item.getItemStack();
                    NBTItem nbti = plugin.getNMSHandler().newItem(itemStack);
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
        
        if(TConstants.ANVILS.contains(e.getTo())){

            List<Entity> list = plugin.getNMSHandler().getEntitiesWithinRadius(1, e.getEntity());
        
            for(Entity en : list){
                if(en instanceof Item){
                    Item i = (Item) en;
                    ItemStack item = i.getItemStack();
                    if(isSimilar(item)){
                        NBTItem nbti = plugin.getNMSHandler().newItem(item);
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
            if(!hasPermission(player, Permissions.ARMOREDELYTRA_CRAFT)) return;
            new BukkitRunnable(){
                @Override
                public void run() {
                    AnvilInventory inv = e.getInventory();
                    AnvilView anvilView = e.getView();
                    ItemStack item1 = inv.getItem(0); // chestplate
                    ItemStack item2 = inv.getItem(1); // elytra
                    if(item1 != null && item2 != null){
                        if(getConfig().getStringList("combineable-materials").contains(item1.getType().toString()) && item2.getType() == Material.ELYTRA){
                            if(isSimilar(item2)) return;
                            ItemStack combinedElytra = getItem().clone();
                            ItemMeta meta = combinedElytra.getItemMeta();
                            ItemMeta chestPlateMeta = item1.getItemMeta();
                            Map<Enchantment,Integer> enchants = new HashMap<>();
                            double armor = 0;
                            double toughness = 0;
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
                            if(chestPlateMeta.hasAttributeModifiers()){
                                if(chestPlateMeta.getAttributeModifiers(Attribute.ARMOR) != null){
                                    armor=0;
                                    for(AttributeModifier modifier : chestPlateMeta.getAttributeModifiers(Attribute.ARMOR)){
                                        armor = (int) modifier.getAmount();
                                    }
                                }
                                if(chestPlateMeta.getAttributeModifiers(Attribute.ARMOR_TOUGHNESS) != null){
                                    toughness=0;
                                    for(AttributeModifier modifier : chestPlateMeta.getAttributeModifiers(Attribute.ARMOR_TOUGHNESS)){
                                        toughness = (int) modifier.getAmount();
                                    }
                                }
                                if(chestPlateMeta.getAttributeModifiers(Attribute.KNOCKBACK_RESISTANCE) != null){
                                    knockbackres=0;
                                    for(AttributeModifier modifier : chestPlateMeta.getAttributeModifiers(Attribute.KNOCKBACK_RESISTANCE)){
                                        knockbackres = (int) modifier.getAmount();
                                    }
                                }
                            }
                            if(armor != 0){
                                AttributeModifier mod = new AttributeModifier(Tweakin.getKey("armor"),armor,Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST);
//                                AttributeModifier mod = new AttributeModifier(Tweakin.getKey("gen"), "generic.armor", armor, Operation.ADD_NUMBER, EquipmentSlot.CHEST);
                                meta.addAttributeModifier(Attribute.ARMOR, mod);

                            }
                            if(toughness != 0){
                                AttributeModifier mod = new AttributeModifier(Tweakin.getKey("armor_toughness"),toughness,Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST);
//                                AttributeModifier mod = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", toughness, Operation.ADD_NUMBER, EquipmentSlot.CHEST);
                                meta.addAttributeModifier(Attribute.ARMOR_TOUGHNESS, mod);
                            }
                            if(knockbackres != 0){
                                AttributeModifier mod = new AttributeModifier(Tweakin.getKey("knockback_resistance"),knockbackres,Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST);
//                                AttributeModifier mod = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", knockbackres, Operation.ADD_NUMBER, EquipmentSlot.CHEST);
                                meta.addAttributeModifier(Attribute.KNOCKBACK_RESISTANCE, mod);
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
                            ItemMeta elytraMeta = combinedElytra.getItemMeta();
                            if(!elytraMeta.hasDisplayName()){
                                elytraMeta.setDisplayName(anvilView.getRenameText());
                            }
                            combinedElytra.setItemMeta(elytraMeta);
                            for(Enchantment ench : enchants.keySet()){
                                combinedElytra.addUnsafeEnchantment(ench, enchants.get(ench));
                            }
//                            if(ExcellentEnchantsCompat.isEnabled){
//                                ExcellentEnchantsCompat.applyEnchantMents(item1, combinedElytra);
//                                ExcellentEnchantsCompat.applyEnchantMents(item2, combinedElytra);
//                            }
                            if(EnchantsSquaredCompat.isEnabled){
                                EnchantsSquaredCompat.applyEnchants(item1,combinedElytra);
                                EnchantsSquaredCompat.applyEnchants(item2,combinedElytra);
                            }
                            NBTItem nbti = plugin.getNMSHandler().newItem(combinedElytra);
                            nbti.setString(CHEST_KEY, InventoryUtils.serializeItem(item1));
                            nbti.setString(ELYTRA_KEY, InventoryUtils.serializeItem(item2));
                            combinedElytra = nbti.getItem();
                            anvilView.setRepairCost(getConfig().getInt("cost",10));
//                            inv.setRepairCost(getConfig().getInt("cost",10));
                            inv.setItem(2, combinedElytra);
                        }
                    }
                    
                }
            }.runTaskLater(plugin, 2);
        }
    }


    
}
