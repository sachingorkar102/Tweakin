package com.github.sachin.tweakin.nbtapi.nms;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.betterflee.AnimalFleeTweak;
import com.github.sachin.tweakin.utils.PaperUtils;
import com.github.sachin.tweakin.utils.ReflectionUtil;
import com.google.common.base.Enums;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.util.ColorUtil;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalAvoidTarget;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.item.context.BlockActionContext;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.IBlockData;
// import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;

public class NBTItem_1_17_R1 extends NMSHelper{
    private net.minecraft.world.item.ItemStack nmsItem;
    private NBTTagCompound compound;

    public NBTItem_1_17_R1(ItemStack item){
        if(item == null) return;
        ItemStack bukkitItem = item.clone();
        this.nmsItem = CraftItemStack.asNMSCopy(bukkitItem);
        this.compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();

    }

    @Override
    public NMSHelper newItem(ItemStack item) {
        NMSHelper nbti = new NBTItem_1_17_R1(item);
        return nbti;
    }
    
    @Override
    public void setString(String key,String value){
        compound.setString(key, value);
    }

    @Override
    public ItemStack getItem() {
        nmsItem.setTag(compound);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public boolean hasKey(String key) {
        return compound.hasKey(key);
    }

    @Override
    public String getString(String key) {
        return compound.getString(key);
    }

    @Override
    public void removeKey(String key) {
        compound.remove(key);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        compound.setBoolean(key, value);
    }

    @Override
    public boolean getBoolean(String key) {
        return compound.getBoolean(key);
    }

    @Override
    public void setInt(String key, int value) {
        compound.setInt(key, value);
    }

    
    @Override
    public void setLong(String key, long value) {
        compound.setLong(key, value);
    }

    @Override
    public void setDouble(String key, double value) {
        compound.setDouble(key, value);
        
    }

    @Override
    public int getInt(String key) {
        return compound.getInt(key);
    }

    @Override
    public long getLong(String key) {
        return compound.getLong(key);
    }

    @Override
    public double getDouble(String key) {
        return compound.getDouble(key);
    }

    @Override
    public void attack(Player player, Entity target) {
        
        ((CraftPlayer)player).getHandle().attack(((CraftEntity)target).getHandle());
        ((CraftPlayer)player).getHandle().resetAttackCooldown();
        
    }

    public boolean placeItem(Player player, Location location,ItemStack item,BlockFace hitFace,String tweakName,boolean playSound){
        
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        BlockPosition pos = new BlockPosition(location.getX(), location.getY(), location.getZ());
        EntityPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        
        MovingObjectPositionBlock mop = new MovingObjectPositionBlock(new Vec3D(location.getX(),location.getY(),location.getZ()),Enums.getIfPresent(EnumDirection.class, hitFace.toString()).or(EnumDirection.c),pos,false);
        
        EnumInteractionResult result = nmsItem.placeItem(new BlockActionContext(nmsPlayer,EnumHand.valueOf("MAIN_HAND"),nmsItem,mop), EnumHand.valueOf("MAIN_HAND"));
        // nmsItem.a(nmsPlayer, TooltipFlag.a.a).;
        if(result.toString() == "CONSUME"){
            player.swingMainHand();
            if(playSound){
                player.getWorld().playSound(location, location.getBlock().getBlockData().getSoundGroup().getPlaceSound(), 1F, 1F);
            }
            if(tweakName != null){
                Tweakin plugin = Tweakin.getPlugin();
                if(plugin.placedBlocksMap.containsKey(tweakName)){
                    plugin.placedBlocksMap.put(tweakName, plugin.placedBlocksMap.get(tweakName)+1);
                }
                else{
                    plugin.placedBlocksMap.put(tweakName, 1);
                }
            }
            return true;
        }
        else{
            return false;
        }
        
        
    }
    @Override
    public void spawnVillager(Villager villager) {
        EntityVillager vil = (EntityVillager) ((CraftEntity)villager).getHandle();
        vil.bP.a(2, new FollowPathFinder(vil));
        
    }

    @Override
    public void avoidPlayer(Entity entity,Player player,ConfigurationSection config) {
        
        EntityAnimal animal = (EntityAnimal) ((CraftEntity)entity).getHandle();
        List<EntityAnimal> list = animal.getWorld().a(EntityAnimal.class,animal.getBoundingBox().g(5));
        if(Tweakin.getPlugin().isRunningPaper){
            PaperUtils.removePanicGoal(entity);
        }
        if(!list.isEmpty()){
            for (EntityAnimal en : list) {
                Entity bEn = en.getBukkitEntity();
                
                if(bEn.getType() == entity.getType()){
                    if(bEn.getPersistentDataContainer().has(AnimalFleeTweak.key, PersistentDataType.INTEGER) && config.getBoolean("ignore-breeded")) continue;
                    en.bP.a(1, new FleePathFinder<EntityPlayer>(en,EntityPlayer.class,config.getInt("max-radius"), config.getDouble("walk-speed"), config.getDouble("sprint-speed"),(pl) -> pl.getUniqueID() == player.getUniqueId(),config.getInt("cooldown")));
                }
            }
        }
    }

    @Override
    public List<Entity> getEntitiesWithinRadius(int radius, Entity center) {
        net.minecraft.world.entity.Entity nmsEn = ((CraftEntity)center).getHandle();
        
        List<Entity> list = nmsEn.getWorld().a(net.minecraft.world.entity.Entity.class,nmsEn.getBoundingBox().g(radius)).stream().map(e -> e.getBukkitEntity()).collect(Collectors.toList());
        return list;
    }

    public void harvestBlock(Player player,Location location,ItemStack tool){
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(tool);
        EntityPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        
        BlockPosition pos = new BlockPosition(location.getX(),location.getY(),location.getZ());
        
        World world = ((CraftWorld)player.getWorld()).getHandle();
        
        IBlockData blockData = world.getType(pos);
        Block nmsBlock = blockData.getBlock();
        nmsBlock.a(world, nmsPlayer, pos, blockData, world.getTileEntity(pos), nmsItem);
        world.a(pos,false);
    }


    public int getColor(String str,int transparency){
        String[] array = str.replace(" ", "").split(",");
        if(array == null || array.length == 0){
            return 100;
        }
        if(array.length != 3) return 100;
        int red = Integer.parseInt(array[0]);
        int green = Integer.parseInt(array[1]);
        int blue = Integer.parseInt(array[2]);
        return ColorUtil.a.a(transparency, red, green, blue);
    }

    @Override
    public boolean matchAxoltlVariant(Entity entity, String color) {
        
        return ((Axolotl)entity).getVariant().toString().equals(color);
    }

    @Override
    public boolean isScreamingGoat(Entity entity) {
        return ((Goat)entity).isScreaming();
    }

    private class FleePathFinder<T extends EntityLiving> extends PathfinderGoalAvoidTarget<T>{
        private int tick = 0;
        private int cooldown;

        public FleePathFinder(EntityCreature entity, Class<T> avoider, float maxDis, double walkSpeedModifier, double sprintSpeedModifier,
                Predicate<EntityLiving> condition,int cooldown) {
            super(entity, avoider, maxDis, walkSpeedModifier, sprintSpeedModifier, condition);
            this.cooldown = cooldown*20;
        }

        @Override
        public boolean a() {
            if(tick > cooldown){
                return false;
            }
            else{
                tick++;
                return super.a();
            }
        }
    }


    private class FollowPathFinder extends PathfinderGoal{

        private EntityInsentient a;
        private EntityPlayer player;

        public FollowPathFinder(EntityInsentient var0) {
            this.a = var0;
            a(EnumSet.of(Type.valueOf("MOVE"),Type.valueOf("LOOK")));
        }

        @Override
        public boolean a() {
            List<EntityLiving> list = a.getWorld().a(EntityLiving.class, a.getBoundingBox().g(10));
            if(!list.isEmpty()){
                for(EntityLiving e : list){
                    if(e instanceof EntityPlayer){
                        EntityPlayer target = (EntityPlayer) e;
                        
                        if(CraftItemStack.asBukkitCopy(target.getItemInMainHand()).getType() == Material.EMERALD_BLOCK){
                            this.player = target;
                            return true;
                        }
                        else if(CraftItemStack.asBukkitCopy(target.getItemInOffHand()).getType() == Material.EMERALD_BLOCK){
                            this.player = target;
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public void c() {
            if(player != null){
                a.getNavigation().a(player.locX(),player.locY(),player.locZ(),0.6);
            }
        }

        
        @Override
        public boolean b() {
            return !this.player.a(this.a,10);
        }

        @Override
        public void d() {
            this.player = null;
        }

    }


}