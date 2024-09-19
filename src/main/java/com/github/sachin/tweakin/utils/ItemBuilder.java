package com.github.sachin.tweakin.utils;

import com.github.sachin.prilib.nms.NBTItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.betterarmorstands.GuiItems;
import com.google.common.base.Enums;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.util.*;
import java.util.stream.Collectors;

public class ItemBuilder {
    

    
    public static ItemStack itemFromFile(ConfigurationSection section,String itemType){
        Preconditions.checkNotNull(section, "item cant be null");
        Preconditions.checkArgument(section.contains("id"), "item should atleast contain id");
        ItemStack item = new ItemStack(Enums.getIfPresent(Material.class, section.getString("id")).or(Material.DIRT));
        
        // check if section has amount
        if(section.contains("amount")){
            item.setAmount(section.getInt("amount",1));
        }

        // this should only happen if material type is air
        ItemMeta meta = item.getItemMeta();
        if(meta == null){
            return item;
        }

        // check for display name
        if(section.contains("display")){
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',section.getString("display")));
        }

        // check for lore
        if(section.contains("lore")){
            List<String> lore = new ArrayList<>();
            section.getStringList("lore").forEach(s -> {
                lore.add(ChatColor.translateAlternateColorCodes('&', s));
            });
            meta.setLore(lore);
        }

        // check for damage, can be usefull versions below 1.14 for resource pack
        if(section.contains("damage")){
            if (meta instanceof Damageable) {
                int damage = section.getInt("damage");
                if (damage > 0) ((Damageable) meta).setDamage(damage);
            }
        }

        // check for extra options on item
        if(section.contains("options")){
            ConfigurationSection options = section.getConfigurationSection("options");
            if(options.getBoolean("enchanted",false)){
                meta.addEnchant(Enchantment.MENDING, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(options.contains("patterns")){
                if(item.getType() == Material.SHIELD){
                    BlockStateMeta blockStateMeta = (BlockStateMeta) meta;
                    BlockState state = blockStateMeta.getBlockState();
                    Banner bannerState = (Banner) state;
                    bannerState.setPatterns(getBannerPatterns(options.getConfigurationSection("patterns")));
                    bannerState.update();
                    blockStateMeta.setBlockState(bannerState);
                }
                else if(meta instanceof BannerMeta){
                    BannerMeta banner = (BannerMeta) meta;
                    banner.setPatterns(getBannerPatterns(options.getConfigurationSection("patterns")));
                }
            }
            else if(meta instanceof LeatherArmorMeta){
                LeatherArmorMeta leather = (LeatherArmorMeta) meta;
                String colorStr = options.getString("color");
                if (colorStr != null) {
                    leather.setColor(parseColor(colorStr));
                }
            }
            else if ((meta instanceof PotionMeta) && options.contains("color")){
                PotionMeta potion = (PotionMeta) meta;
                potion.setColor(Color.fromRGB(options.getInt("color",0)));
                
            }
            else if((meta instanceof SkullMeta) && options.contains("texture")){
                SkullMeta skullMeta = (SkullMeta) meta;
//                Tweakin.getPlugin().getNMSHandler().applyHeadTexture(skullMeta, options.getString("texture"));
            }
            if(options.contains("model")){
                meta.setCustomModelData(options.getInt("model", 0));
            }
        }

        // enchants
        if (section.contains("enchants")) {
            for (String string : section.getStringList("enchants")) {
                String[] l = string.split(" ");
                if(l.length != 2) continue;
                String name = l[0].toLowerCase();
                int level = Integer.parseInt(l[1]);
                Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(name));
                if(enchant == null) continue;
                meta.addEnchant(enchant, level, true);
            }
        }

        // item flags
        if(section.contains("flags")){
            List<String> itemFlags = section.getStringList("flags").stream().map(m -> m.toUpperCase()).collect(Collectors.toList());
            for(String str : itemFlags){
                if(str.equals("ALL")){
                    meta.addItemFlags(ItemFlag.values());
                    break;
                }
                ItemFlag itemFlag = Enums.getIfPresent(ItemFlag.class, str).orNull();
                if(itemFlag != null){
                    meta.addItemFlags(itemFlag);
                }
            }
        }
        if((meta instanceof FireworkEffectMeta) && section.contains("firework")) {
            FireworkEffectMeta firework = (FireworkEffectMeta) meta;
            ConfigurationSection fireworkConfig = section.getConfigurationSection("firework");
            if(fireworkConfig != null){

                FireworkEffect.Builder builder = FireworkEffect.builder();
                builder.with(Type.STAR);
                List<String> strColors = fireworkConfig.getStringList("colors");
                List<Color> colors = new ArrayList<>(strColors.size());
                for (String str: strColors) {
                    colors.add(parseColor(str));
                }
                builder.withColor(colors);
                firework.setEffect(builder.build());
            }
        }
           
        item.setItemMeta(meta);
        if(itemType != null){
            NBTItem nbtItem = Tweakin.getPlugin().getNMSHandler().newItem(item);
            nbtItem.setString(itemType, " ");
            item = nbtItem.getItem();

        }

        return item;
    }

    private static Color parseColor(String str) {
        if (Strings.isNullOrEmpty(str)) return Color.BLACK;
        String[] rgb = StringUtils.split(StringUtils.deleteWhitespace(str), ',');
        if (rgb.length < 3){
            return getColorFromString(str);
        }
        return Color.fromRGB(NumberUtils.toInt(rgb[0], 0), NumberUtils.toInt(rgb[1], 0), NumberUtils.toInt(rgb[2], 0));
    }

    private static Color getColorFromString(String str){
        switch (str) {
            case "WHITE":
                return Color.WHITE;
            case "RED":
                return Color.RED;
            case "BLUE":
                return Color.BLUE;
            case "GREEN":
                return Color.GREEN;
            case "AQUA":
                return Color.AQUA;
            case "BLACK":
                return Color.BLACK;
            case "SILVER":
                return Color.SILVER;
            case "MAROON":
                return Color.MAROON;
            case "YELLOW":
                return Color.YELLOW;
            case "OLIVE":
                return Color.OLIVE;
            case "ORANGE":
                return Color.ORANGE;
            case "PURPLE":
                return Color.PURPLE;
            case "TEAL":
                return Color.TEAL;                                          
            default:
                return Color.WHITE;
        }
    }


    private static List<Pattern> getBannerPatterns(ConfigurationSection patterns){
        List<Pattern> list = new ArrayList<>();
        if (patterns != null) {
            for (String pattern : patterns.getKeys(false)) {
                PatternType type = PatternType.getByIdentifier(pattern);
                if (type == null) type = Enums.getIfPresent(PatternType.class, pattern.toUpperCase(Locale.ENGLISH)).or(PatternType.BASE);
                DyeColor color = Enums.getIfPresent(DyeColor.class, patterns.getString(pattern).toUpperCase(Locale.ENGLISH)).or(DyeColor.WHITE);
                list.add(new Pattern(color, type));
                
            }
        }
        return list;
    }




    public static ItemStack guiItemFromFile(ConfigurationSection section,String itemType){
        ItemStack item = itemFromFile(section, null);
        NBTItem nbti = Tweakin.getPlugin().getNMSHandler().newItem(item);
        nbti.setString("misc-item", itemType);
        return nbti.getItem();

    }

    public static ItemStack asGuiItem(ConfigurationSection section,GuiItems guiItem){
        ItemStack item = itemFromFile(section, null);
        NBTItem nbti = Tweakin.getPlugin().getNMSHandler().newItem(item);
        nbti.setString("tweakin-armor-stand-gui", guiItem.toString());
        return nbti.getItem();
    }

    public static boolean hasKey(ItemStack item,String key){
        NBTItem nbti = Tweakin.getPlugin().getNMSHandler().newItem(item);
        if(nbti.hasKey("misc-item")){
            return nbti.getString("misc-item").equals(key);
        }

        return false;
    }

    public static ItemStack damageItem(int amount, ItemStack item, Random random, Player player){
        ItemMeta meta = item.getItemMeta();
        if(!(meta instanceof Damageable) || amount < 0) return item;

        int m = item.getEnchantmentLevel(Enchantment.DURABILITY);
        int k = 0;
        for (int l = 0; m > 0 && l < amount; l++) {
            if (random.nextInt(m +1) > 0){
                k++;
            }
        }
        amount -= k;
        if(player != null){
            PlayerItemDamageEvent damageEvent = new PlayerItemDamageEvent(player, item, amount);
            Tweakin.getPlugin().getServer().getPluginManager().callEvent(damageEvent);
            if(amount != damageEvent.getDamage() || damageEvent.isCancelled()){
                damageEvent.getPlayer().updateInventory();
            }
            else if(damageEvent.isCancelled()){
                return item;
            }
            amount = damageEvent.getDamage();

        }
        if (amount <= 0)
            return item;

        Damageable damageable = (Damageable) meta;
        if(damageable.getDamage()>=item.getType().getMaxDurability()-1){
            int slot = player.getInventory().first(item);
            if(slot != -1){
                player.getInventory().setItem(slot,null);
            }
            else if(player.getInventory().getItem(EquipmentSlot.OFF_HAND).equals(item)){
                player.getInventory().setItem(EquipmentSlot.OFF_HAND,null);
            }
            player.getWorld().playSound(player.getLocation(),Sound.ENTITY_ITEM_BREAK,1,1);
            return item;
        }
        damageable.setDamage(damageable.getDamage()+amount);
        item.setItemMeta(meta);
        return item;
    }

    
}
