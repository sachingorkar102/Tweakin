package com.github.sachin.tweakin.utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.github.sachin.tweakin.modules.betterarmorstands.GuiItems;
import com.github.sachin.tweakin.nbtapi.NBTItem;
import com.google.common.base.Enums;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
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
                mutateItemMeta(skullMeta, options.getString("texture"));
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
            NBTItem nbtItem = new NBTItem(item);
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



    public static void mutateItemMeta(SkullMeta meta, String b64) {
        Field metaProfileField ;
        Method metaSetProfileMethod;
		try {
            metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            metaSetProfileMethod.setAccessible(true);

			metaSetProfileMethod.invoke(meta, makeProfile(b64));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
			try {
                metaProfileField = meta.getClass().getDeclaredField("profile");
                metaProfileField.setAccessible(true);

				metaProfileField.set(meta, makeProfile(b64));

			} catch (NoSuchFieldException | IllegalAccessException ex2) {
				ex2.printStackTrace();
			}
		}
	}
    

    private static GameProfile makeProfile(String b64) {
		UUID id = new UUID(b64.substring(b64.length() - 20).hashCode(),b64.substring(b64.length() - 10).hashCode());
		GameProfile profile = new GameProfile(id, "someName");
		profile.getProperties().put("textures", new Property("textures", b64));
		return profile;
	}

    public static ItemStack guiItemFromFile(ConfigurationSection section,String itemType){
        ItemStack item = itemFromFile(section, null);
        NBTItem nbti = new NBTItem(item);
        nbti.setString("misc-item", itemType);
        return nbti.getItem();

    }

    public static ItemStack asGuiItem(ConfigurationSection section,GuiItems guiItem){
        ItemStack item = itemFromFile(section, null);
        NBTItem nbti = new NBTItem(item);
        nbti.setString("tweakin-armor-stand-gui", guiItem.toString());
        return nbti.getItem();
    }

    public static boolean hasKey(ItemStack item,String key){
        NBTItem nbti = new NBTItem(item);
        if(nbti.hasKey("misc-item")){
            return nbti.getString("misc-item").equals(key);
        }

        return false;
    }

    
}
