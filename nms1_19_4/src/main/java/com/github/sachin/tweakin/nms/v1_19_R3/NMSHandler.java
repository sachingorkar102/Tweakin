package com.github.sachin.tweakin.nms.v1_19_R3;

import com.github.sachin.tweakin.modules.betterflee.AnimalFleeTweak;
import com.github.sachin.tweakin.nbtapi.nms.NMSHelper;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NMSHandler extends NMSHelper {


    private net.minecraft.world.item.ItemStack nmsItem;
    private CompoundTag compound;

    public NMSHandler(){}

    public NMSHandler(ItemStack item){
        if(item==null) return;

        this.nmsItem = CraftItemStack.asNMSCopy(item);
        this.compound = nmsItem.getOrCreateTag();
    }

    @Override
    public NMSHelper newItem(ItemStack item) {
        return new NMSHandler(item);
    }

    @Override
    public void setString(String key, String value) {
        compound.putString(key,value);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        compound.putBoolean(key,value);
    }

    @Override
    public void setInt(String key, int value) {
        compound.putInt(key,value);
    }

    @Override
    public void setLong(String key, long value) {
        compound.putLong(key,value);
    }

    @Override
    public void setDouble(String key, double value) {
        compound.putDouble(key,value);
    }

    @Override
    public String getString(String key) {
        return compound.getString(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return compound.getBoolean(key);
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
    public boolean hasKey(String key) {
        return compound.contains(key);
    }

    @Override
    public org.bukkit.inventory.ItemStack getItem() {
        nmsItem.save(compound);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public void removeKey(String key) {
        compound.remove(key);
    }

    @Override
    public void placeWater(Block block) {
        Location loc = block.getLocation();
        BlockPos blockPos = new BlockPos(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ());
        ServerLevel level = ((CraftWorld) block.getWorld()).getHandle();
        BlockState iBlockData = level.getBlockState(blockPos);
        net.minecraft.world.level.block.Block nmsBlock = iBlockData.getBlock();
        if(nmsBlock instanceof LiquidBlockContainer){
            ((LiquidBlockContainer)nmsBlock).placeLiquid(level,blockPos,iBlockData, Fluids.WATER.getSource(false));
        }
    }

    @Override
    public void attack(Player player, Entity target) {

        ((CraftPlayer)player).getHandle().attack(((CraftEntity)target).getHandle());
        ((CraftPlayer)player).getHandle().resetAttackStrengthTicker();
    }

    @Override
    public boolean placeItem(Player player, Location location, ItemStack item, BlockFace hitFace, String tweakName, boolean playSound) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        BlockPos pos = new BlockPos((int) location.getX(), (int) location.getY(), (int) location.getZ());
        ServerPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        UseOnContext context = new UseOnContext(nmsPlayer, InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(0.5F, 1F, 0.5F), Direction.UP, pos, false));
        InteractionResult res = nmsItem.useOn(context,InteractionHand.MAIN_HAND);

        if(res==InteractionResult.CONSUME){
            player.swingMainHand();
            BlockPos placedPos = context.getClickedPos().relative(context.getClickedFace());
            Block placedBlock = player.getWorld().getBlockAt(placedPos.getX(),placedPos.getY(),placedPos.getZ());
            if (placedBlock.getType()==Material.BARRIER){
                placedBlock.setType(Material.AIR);
            }
            if(playSound){
                player.getWorld().playSound(location, location.getBlock().getBlockData().getSoundGroup().getPlaceSound(), 1F, 1F);
            }
            return true;
        }
        return false;
    }



    @Override
    public void spawnVillager(Villager villager,boolean update) {
        net.minecraft.world.entity.npc.Villager vil = (net.minecraft.world.entity.npc.Villager) ((CraftEntity)villager).getHandle();
        TemptGoal goal = new TemptGoal(vil, 0.6, Ingredient.of(Items.EMERALD_BLOCK), false);
        if(update){
            vil.goalSelector.removeGoal(goal);
        }
        vil.goalSelector.addGoal(2,goal);

    }

    @Override
    public void avoidPlayer(Entity entity, Player player, ConfigurationSection config) {
        Animal animal = (Animal) ((CraftEntity)entity).getHandle();
        List<Animal> list = animal.getLevel().getEntitiesOfClass(Animal.class,animal.getBoundingBox().inflate(5));
        animal.goalSelector.removeGoal(new PanicGoal(animal, 2.0));
        if(!list.isEmpty()){
            for (Animal en : list) {
                Entity bEn = en.getBukkitEntity();

                if(bEn.getType() == entity.getType()){
                    if(bEn.getPersistentDataContainer().has(AnimalFleeTweak.key, PersistentDataType.INTEGER) && config.getBoolean("ignore-breeded")) continue;
                    FleePathFinder<ServerPlayer> goal = new FleePathFinder<ServerPlayer>(en, ServerPlayer.class, config.getInt("max-radius"), config.getDouble("walk-speed"), config.getDouble("sprint-speed"), (pl) -> pl.getUUID() == player.getUniqueId(), config.getInt("cooldown"));
                    en.goalSelector.removeGoal(goal);
                    en.goalSelector.addGoal(1, goal);
                }
            }
        }
    }

    @Override
    public ItemStack createMap(Location dist,byte zoom,boolean biomePreview){
        BlockPos loc = new BlockPos(dist.getBlockX(),dist.getBlockY(),dist.getBlockZ());
        ServerLevel level = ((CraftWorld) dist.getWorld()).getHandle();
        net.minecraft.world.item.ItemStack mapItem = MapItem.create(level,dist.getBlockX(),dist.getBlockZ(),zoom,true,true);
        MapItemSavedData.addTargetDecoration(mapItem,loc,"+", MapDecoration.Type.RED_X);
        if(biomePreview){
            MapItem.renderBiomePreviewMap(level,mapItem);
        }
        return CraftItemStack.asBukkitCopy(mapItem);
    }

    @Override
    public boolean matchAxoltlVariant(Entity entity, String color) {
        return ((Axolotl)entity).getVariant().toString().equals(color);
    }

    @Override
    public boolean matchFrogVariant(Entity entity, String variant) {
        return ((Frog)entity).getVariant().toString().equals(variant);
    }

    @Override
    public boolean isScreamingGoat(Entity entity) {
        return ((Goat)entity).isScreaming();
    }

    @Override
    public List<Entity> getEntitiesWithinRadius(int radius, Entity center) {
        net.minecraft.world.entity.Entity entity = ((CraftEntity)center).getHandle();
        return entity.getLevel().getEntities(entity,entity.getBoundingBox().inflate(radius)).stream().map(net.minecraft.world.entity.Entity::getBukkitEntity).collect(Collectors.toList());
    }

    @Override
    public void applyHeadTexture(SkullMeta meta, String b64) {
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

    public GameProfile makeProfile(String b64) {
        UUID id = new UUID(b64.substring(b64.length() - 20).hashCode(),b64.substring(b64.length() - 10).hashCode());
        GameProfile profile = new GameProfile(id, "someName");
        profile.getProperties().put("textures", new Property("textures", b64));
        return profile;
    }


    private static class FleePathFinder<T extends net.minecraft.world.entity.LivingEntity> extends AvoidEntityGoal<T>{

        private int cooldown;

        private final int finalCoolDown;


        public FleePathFinder(PathfinderMob entity, Class<T> avoider, float maxDis, double walkSpeedModifier, double sprintSpeedModifier,
                              Predicate<net.minecraft.world.entity.LivingEntity> condition, int cooldown) {
            super(entity, avoider, maxDis, walkSpeedModifier, sprintSpeedModifier, condition);
            this.finalCoolDown = cooldown*20;
            this.cooldown = finalCoolDown;
        }

        @Override
        public boolean canUse() {
            if(cooldown==0){
                return false;
            }
            else{
                cooldown--;
                return super.canUse();
            }
        }

        @Override
        public boolean canContinueToUse() {
            if(cooldown==0){
                this.cooldown = this.finalCoolDown;
            }
            if(cooldown==finalCoolDown){
                return false;
            }
            return super.canContinueToUse();
        }
    }

}

