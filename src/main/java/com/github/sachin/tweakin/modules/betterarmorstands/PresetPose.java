package com.github.sachin.tweakin.modules.betterarmorstands;

import com.github.sachin.tweakin.nbtapi.NBTItem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

public class PresetPose{


    public PresetPose(String id, String display, EulerAngle head, EulerAngle torso, EulerAngle leftarm,
            EulerAngle rightarm, EulerAngle leftleg, EulerAngle rightleg) {
        this.id = id;
        this.display = display;
        this.head = head;
        this.torso = torso;
        this.leftarm = leftarm;
        this.rightarm = rightarm;
        this.leftleg = leftleg;
        this.rightleg = rightleg;
        setItem();
    }
    
    private void setItem(){
        this.item = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', display));
        item.setItemMeta(meta);
        NBTItem nbti = new NBTItem(item);
        nbti.setString("preset-pose-button", id);
        this.item = nbti.getItem();
    }

    public void setPose(ArmorStand as){
        as.setHeadPose(head);
        as.setBodyPose(torso);
        as.setLeftArmPose(leftarm);
        as.setRightArmPose(rightarm);
        as.setLeftLegPose(leftleg);
        as.setRightLegPose(rightleg);
        
    }


    public final String id;
    public ItemStack item;
    public final String display;
    public final EulerAngle head;
    public final EulerAngle torso;
    public final EulerAngle leftarm;
    public final EulerAngle rightarm;
    public final EulerAngle leftleg;
    public final EulerAngle rightleg;
    

    // @Override
    // public Map<String, Object> serialize() {
    //     Map<String,Object> map = new HashMap<>();
    //     map.put("id",id);
    //     map.put("display", display);
    //     map.put("head", new SerializableEulerAngle(head));
    //     map.put("torso",new SerializableEulerAngle(torso));
    //     map.put("leftarm",new SerializableEulerAngle(leftarm));
    //     map.put("rightarm",new SerializableEulerAngle(rightarm));
    //     map.put("leftleg",new SerializableEulerAngle(leftleg));
    //     map.put("rightleg",new SerializableEulerAngle(rightleg));
    //     return map;
    // }


    // public static PresetPose deserialize(String id,ConfigurationSection section){
    //     EulerAngle head = ((SerializableEulerAngle)section.get("head")).getAngle();
    //     EulerAngle torso = ((SerializableEulerAngle)section.get("torso")).getAngle();
    //     EulerAngle leftarm = ((SerializableEulerAngle)section.get("leftarm")).getAngle();
    //     EulerAngle rightarm = ((SerializableEulerAngle)section.get("rightarm")).getAngle();
    //     EulerAngle leftleg = ((SerializableEulerAngle)section.get("leftleg")).getAngle();
    //     EulerAngle rightleg = ((SerializableEulerAngle)section.get("rightleg")).getAngle();
    //     return new PresetPose(id, section.getString("display"), head, torso, leftarm, rightarm, leftleg, rightleg);
    // }


    
    
}
