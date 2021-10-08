package com.github.sachin.tweakin.modules.mobheads;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.nbtapi.NBTItem;
import com.github.sachin.tweakin.utils.ItemBuilder;
import com.google.common.base.Predicates;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Fox.Type;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Llama.Color;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Panda.Gene;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Strider;
import org.bukkit.entity.TraderLlama;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;


public enum Head {
    
    BAT,
    BEE_PLAIN("BEE",(bee)-> {Bee b=(Bee)bee; return !b.hasNectar() && b.getAnger() ==0;}),
    BEE_NECTAR("BEE",(bee)-> {Bee b=(Bee)bee; return b.hasNectar() && b.getAnger() ==0;}),
    BEE_ANGRY("BEE",(bee)-> {Bee b=(Bee)bee; return !b.hasNectar() && b.getAnger() !=0;}),
    BEE_NECTAR_ANGRY("BEE",(bee)-> {Bee b=(Bee)bee; return b.hasNectar() && b.getAnger() !=0;}),
    BLAZE,
    
    TABBY_CAT("CAT",(cat) -> {Cat c=(Cat)cat; return c.getCatType()==Cat.Type.TABBY;}),
    TUXEDO_CAT("CAT",(cat) -> {Cat c=(Cat)cat; return c.getCatType()==Cat.Type.BLACK;}),
    GINGER_CAT("CAT",(cat) -> {Cat c=(Cat)cat; return c.getCatType()==Cat.Type.RED;}),
    SIAMESE_CAT("CAT",(cat) -> {Cat c=(Cat)cat; return c.getCatType()==Cat.Type.SIAMESE;}),
    BRITISH_SHORTHAIR_CAT("CAT",(cat) -> {Cat c=(Cat)cat; return c.getCatType()==Cat.Type.BRITISH_SHORTHAIR;}),
    CALICO_CAT("CAT",(cat) -> {Cat c=(Cat)cat; return c.getCatType()==Cat.Type.CALICO;}),
    PERSIAN_CAT("CAT",(cat) -> {Cat c=(Cat)cat; return c.getCatType()==Cat.Type.PERSIAN;}),
    RAGDOLL_CAT("CAT",(cat) -> {Cat c=(Cat)cat; return c.getCatType()==Cat.Type.RAGDOLL;}),
    WHITE_CAT("CAT",(cat) -> {Cat c=(Cat)cat; return c.getCatType()==Cat.Type.WHITE;}),
    JELLIE_CAT("CAT",(cat) -> {Cat c=(Cat)cat; return c.getCatType()==Cat.Type.JELLIE;}),
    BLACK_CAT("CAT",(cat) -> {Cat c=(Cat)cat; return c.getCatType()==Cat.Type.ALL_BLACK;}),
    CAVE_SPIDER,
    CHICKEN,
    COD,
    COW,
    CREEPER("CREEPER",Material.CREEPER_HEAD),
    CHARGED_CREEPER("CREEPER",(cr)-> ((Creeper)cr).isPowered()),
    DOLPHIN,
    DONKEY,
    DROWNED,
    ELDER_GUARDIAN,
    ENDER_DRAGON("ENDER_DRAGON",Material.DRAGON_HEAD),
    ENDERMAN,
    EVOKER,
    RED_FOX("FOX",(fox)-> ((Fox)fox).getFoxType()==Type.RED),
    SNOW_FOX("FOX",(fox)-> ((Fox)fox).getFoxType()==Type.SNOW),
    GHAST,
    GUARDIAN,
    WHITE_HORSE("HORSE",(horse)-> ((Horse)horse).getColor()==Horse.Color.WHITE),
    CREAMY_HORSE("HORSE",(horse)-> ((Horse)horse).getColor()==Horse.Color.CREAMY),
    CHESTNUT_HORSE("HORSE",(horse)-> ((Horse)horse).getColor()==Horse.Color.CHESTNUT),
    BROWN_HORSE("HORSE",(horse)-> ((Horse)horse).getColor()==Horse.Color.BROWN),
    BLACK_HORSE("HORSE",(horse)-> ((Horse)horse).getColor()==Horse.Color.BLACK),
    GRAY_HORSE("HORSE",(horse)-> ((Horse)horse).getColor()==Horse.Color.GRAY),
    DARK_BROWN_HORSE("HORSE",(horse)-> ((Horse)horse).getColor()==Horse.Color.DARK_BROWN),
    HUSK,
    ILLUSIONER,
    IRON_GOLEM,
    CREAMY_LLAMA("LLAMA",(llama)-> ((Llama)llama).getColor()==Color.CREAMY),
    WHITE_LLAMA("LLAMA",(llama)-> ((Llama)llama).getColor()==Color.WHITE),
    BROWN_LLAMA("LLAMA",(llama)-> ((Llama)llama).getColor()==Color.BROWN),
    GRAY_LLAMA("LLAMA",(llama)-> ((Llama)llama).getColor()==Color.GRAY),
    MAGMA_CUBE,
    
    
    RED_MOOSHROOM("MUSHROOM_COW",(cow)-> ((MushroomCow)cow).getVariant()==MushroomCow.Variant.RED),
    BROWN_MOOSHROOM("MUSHROOM_COW",(cow)-> ((MushroomCow)cow).getVariant()==MushroomCow.Variant.BROWN),
    MULE,
    OCELOT,
    AGGRESSIVE_PANDA("PANDA",(panda)-> ((Panda)panda).getMainGene()==Gene.AGGRESSIVE),
    LAZY_PANDA("PANDA",(panda)-> ((Panda)panda).getMainGene()==Gene.LAZY),
    PLAYFUL_PANDA("PANDA",(panda)-> ((Panda)panda).getMainGene()==Gene.PLAYFUL),
    WORRIED_PANDA("PANDA",(panda)-> ((Panda)panda).getMainGene()==Gene.WORRIED),
    BROWN_PANDA("PANDA",(panda)-> ((Panda)panda).getMainGene()==Gene.BROWN),
    WEAK_PANDA("PANDA",(panda)-> ((Panda)panda).getMainGene()==Gene.WEAK),
    OTHER_PANDA("PANDA"),
    RED_PARROT("PARROT",(parrot) -> ((Parrot)parrot).getVariant()==Parrot.Variant.RED),
    BLUE_PARROT("PARROT",(parrot) -> ((Parrot)parrot).getVariant()==Parrot.Variant.BLUE),
    GREEN_PARROT("PARROT",(parrot) -> ((Parrot)parrot).getVariant()==Parrot.Variant.GREEN),
    CYAN_PARROT("PARROT",(parrot) -> ((Parrot)parrot).getVariant()==Parrot.Variant.CYAN),
    GRAY_PARROT("PARROT",(parrot) -> ((Parrot)parrot).getVariant()==Parrot.Variant.GRAY),
    PHANTOM,
    PIG,
    PILLAGER,
    POLAR_BEAR,
    PUFFERFISH,
    TOAST_RABBIT("RABBIT",(rabbit)-> ((Rabbit)rabbit).getName().equals("Toast")),
    BROWN_RABBIT("RABBIT",(rabbit)-> ((Rabbit)rabbit).getRabbitType()==Rabbit.Type.BROWN),
    WHITE_RABBIT("RABBIT",(rabbit)-> ((Rabbit)rabbit).getRabbitType()==Rabbit.Type.WHITE),
    BLACK_RABBIT("RABBIT",(rabbit)-> ((Rabbit)rabbit).getRabbitType()==Rabbit.Type.BLACK),
    BLACK_WHITE_RABBIT("RABBIT",(rabbit)-> ((Rabbit)rabbit).getRabbitType()==Rabbit.Type.BLACK_AND_WHITE),
    GOLD_RABBIT("RABBIT",(rabbit)-> ((Rabbit)rabbit).getRabbitType()==Rabbit.Type.GOLD),
    SALT_PEPPER_RABBIT("RABBIT",(rabbit)-> ((Rabbit)rabbit).getRabbitType()==Rabbit.Type.SALT_AND_PEPPER),
    KILLER_RABBIT("RABBIT",(rabbit)-> ((Rabbit)rabbit).getRabbitType()==Rabbit.Type.THE_KILLER_BUNNY),
    RAVAGER,
    SALMON,
    WHITE_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.WHITE;}),
    ORANGE_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.ORANGE;}),
    MAGENTA_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.MAGENTA;}),
    LIGHT_BLUE_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.LIGHT_BLUE;}),
    LIME_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.LIME;}),
    PURPLE_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.PURPLE;}),
    RED_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.RED;}),
    GREEN_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.GREEN;}),
    BROWN_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.BROWN;}),
    PINK_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.PINK;}),
    GRAY_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.GRAY;}),
    JEB_SHEEP("SHEEP",(sheep) -> ((Sheep)sheep).getName().equals("jeb_")),
    CYAN_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.CYAN;}),
    BLACK_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.BLACK;}),
    YELLOW_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.YELLOW;}),
    LIGHT_GRAY_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.LIGHT_GRAY;}),
    BLUE_SHEEP("SHEEP",(sheep) -> {Sheep s = (Sheep) sheep; return !s.getName().equals("jeb_") && s.getColor()==DyeColor.BLUE;}),
    SHULKER,
    SILVERFISH,
    SKELETON("SKELETON",Material.SKELETON_SKULL),
    SKELETON_HORSE,
    SLIME,
    SNOW_GOLEM("SNOWMAN"),
    SPIDER,
    SQUID,
    CREAMY_TRADER_LLAMA("TRADER_LLAMA",(llama)-> ((TraderLlama)llama).getColor()==Llama.Color.CREAMY),
    WHITE_TRADER_LLAMA("TRADER_LLAMA",(llama)-> ((TraderLlama)llama).getColor()==Llama.Color.WHITE),
    BROWN_TRADER_LLAMA("TRADER_LLAMA",(llama)-> ((TraderLlama)llama).getColor()==Llama.Color.BROWN),
    GRAY_TRADER_LLAMA("TRADER_LLAMA",(llama)-> ((TraderLlama)llama).getColor()==Llama.Color.GRAY),
    TURTLE,
    VEX,
    ARMORER_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.ARMORER),
    BUTCHER_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.BUTCHER),
    CARTOGRAPHER_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.CARTOGRAPHER),
    CLERIC_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.CLERIC),
    FARMER_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.FARMER),
    FISHERMAN_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.FISHERMAN),
    FLETCHER_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.FLETCHER),
    LEATHERWORKER_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.LEATHERWORKER),
    LIBRARIAN_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.LIBRARIAN),
    MASON_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.MASON),
    NITWIT_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.NITWIT),
    UNEMPLOYED_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.NONE),
    SHEPHERD_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.SHEPHERD),
    TOOLSMITH_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.TOOLSMITH),
    WEAPONSMITH_VILLAGER("VILLAGER",(vil)-> ((Villager)vil).getProfession()==Villager.Profession.WEAPONSMITH),
    WANDERING_TRADER,
    VINDICATOR,
    WITCH,
    WITHER_SKELETON("WITHER_SKELETON",Material.WITHER_SKELETON_SKULL),
    NORMAL_WITHER("WITHER"),
    INVULNERABLE_WITHER("WITHER"),
    NORMAL_ARMORED_WITHER("WITHER"),
    INVULNERABLE_ARMORED_WITHER("WITHER"),
    WOLF("WOLF",(wolf)-> !((Wolf)wolf).isAngry()),
    ANGRY_WOLF("WOLF",(wolf)-> ((Wolf)wolf).isAngry()),
    STRIDER("STRIDER",(strider)-> !((Strider)strider).isShivering()),
    FREEZING_STRIDER("STRIDER",(strider)-> ((Strider)strider).isShivering()),
    PIGLIN,
    ZOGLIN,
    HOGLIN,
    ZOMBIEFIED_PIGLIN,
    PIGLIN_BRUTE,
    ZOMBIE("ZOMBIE",Material.ZOMBIE_HEAD),
    ZOMBIE_HORSE,
    ZOMBIE_ARMORER("ZOMBIE_VILLAGER",(vil)->cast(vil, ZombieVillager.class).getVillagerProfession()==Villager.Profession.ARMORER),
    ZOMBIE_BUTCHER("ZOMBIE_VILLAGER",(vil)-> ((ZombieVillager)vil).getVillagerProfession()==Villager.Profession.BUTCHER),
    ZOMBIE_CARTOGRAPHER("ZOMBIE_VILLAGER",(vil)->((ZombieVillager)vil).getVillagerProfession()==Villager.Profession.CARTOGRAPHER),
    ZOMBIE_CLERIC("ZOMBIE_VILLAGER",(vil)->((ZombieVillager)vil).getVillagerProfession()==Villager.Profession.CLERIC),
    ZOMBIE_FARMER("ZOMBIE_VILLAGER",(vil)->((ZombieVillager)vil).getVillagerProfession()==Villager.Profession.FARMER),
    ZOMBIE_FISHERMAN("ZOMBIE_VILLAGER",(vil)->((ZombieVillager)vil).getVillagerProfession()==Villager.Profession.FISHERMAN),
    ZOMBIE_FLETCHER("ZOMBIE_VILLAGER",(vil)->((ZombieVillager)vil).getVillagerProfession()==Villager.Profession.FLETCHER),
    ZOMBIE_LEATHERWORKER("ZOMBIE_VILLAGER",(vil)->((ZombieVillager)vil).getVillagerProfession()==Villager.Profession.LEATHERWORKER),
    ZOMBIE_LIBRARIAN("ZOMBIE_VILLAGER",(vil)->((ZombieVillager)vil).getVillagerProfession()==Villager.Profession.LIBRARIAN),
    ZOMBIE_MASON("ZOMBIE_VILLAGER",(vil)->((ZombieVillager)vil).getVillagerProfession()==Villager.Profession.MASON),
    ZOMBIE_NITWIT("ZOMBIE_VILLAGER",(vil)->((ZombieVillager)vil).getVillagerProfession()==Villager.Profession.NITWIT),
    ZOMBIE_UNEMPLOYED("ZOMBIE_VILLAGER",(vil)->((ZombieVillager)vil).getVillagerProfession()==Villager.Profession.NONE),
    ZOMBIE_SHEPHERD("ZOMBIE_VILLAGER",(vil)->((ZombieVillager)vil).getVillagerProfession()==Villager.Profession.SHEPHERD),
    ZOMBIE_TOOLSMITH("ZOMBIE_VILLAGER",(vil)->((ZombieVillager)vil).getVillagerProfession()==Villager.Profession.TOOLSMITH),
    ZOMBIE_WEAPONSMITH("ZOMBIE_VILLAGER",(vil)->((ZombieVillager)vil).getVillagerProfession()==Villager.Profession.WEAPONSMITH),
    BLUE_AXOLOTL("AXOLOTL",(entity)->Tweakin.getPlugin().getNmsHelper().matchAxoltlVariant(entity, "BLUE")),
    WILD_AXOLOTL("AXOLOTL",(entity)->Tweakin.getPlugin().getNmsHelper().matchAxoltlVariant(entity, "WILD")),
    GOLD_AXOLOTL("AXOLOTL",(entity)->Tweakin.getPlugin().getNmsHelper().matchAxoltlVariant(entity, "GOLD")),
    CYAN_AXOLOTL("AXOLOTL",(entity)->Tweakin.getPlugin().getNmsHelper().matchAxoltlVariant(entity, "CYAN")),
    LUCY_AXOLOTL("AXOLOTL",(entity)->Tweakin.getPlugin().getNmsHelper().matchAxoltlVariant(entity, "LUCY")),
    GLOW_SQUID,
    GOAT("GOAT",(entity)->!Tweakin.getPlugin().getNmsHelper().isScreamingGoat(entity)),
    SCREAMING_GOAT("GOAT",(entity)->Tweakin.getPlugin().getNmsHelper().isScreamingGoat(entity)),
    ;



    private String entityType;
    private ItemStack skull;
    private double chance;
    private double lootingMul;
    private ConfigurationSection section;
    private final String initValue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv";
    
    
    private Predicate<Entity> check;

    private Head(String entityType,Predicate<Entity> check){
        this.check = check;
        this.section = MobHeadsTweak.headConfig.getConfigurationSection(toString().toLowerCase());
        this.lootingMul = section.getDouble("looting",0.0);
        this.chance = section.getDouble("chance",0);
        this.skull = createSkull(section);
        this.entityType = entityType;

    }

    public void reload(){
        this.section = MobHeadsTweak.headConfig.getConfigurationSection(toString().toLowerCase());
        this.lootingMul = section.getDouble("looting",0.0);
        this.chance = section.getDouble("chance",0);
        this.skull = createSkull(section);
    }


    private Head(String entityType,Material mat){
        this.entityType = entityType;
        this.check = Predicates.alwaysTrue();
        this.section = MobHeadsTweak.headConfig.getConfigurationSection(toString().toLowerCase());
        this.lootingMul = section.getDouble("looting",0.0);
        this.chance = section.getDouble("chance",0);
        this.skull = new ItemStack(mat);
    }

    private Head(String entityType){
        this.check = Predicates.alwaysTrue();
        this.entityType = entityType;
        this.section = MobHeadsTweak.headConfig.getConfigurationSection(toString().toLowerCase());
        this.lootingMul = section.getDouble("looting",0.0);
        this.chance = section.getDouble("chance",0);
        this.skull = createSkull(section);
    }

    private Head(){
        this.check = Predicates.alwaysTrue();
        this.entityType = toString();
        this.section = MobHeadsTweak.headConfig.getConfigurationSection(toString().toLowerCase());
        this.lootingMul = section.getDouble("looting",0.0);
        this.chance = section.getDouble("chance",0);
        this.skull = createSkull(section);
    }

    public boolean check(Entity entity){
        return check.test(entity);
    }

    public double getChance() {
        return chance;
    }

    public ItemStack getSkull() {
        return skull;
    }

    public double getLootingMultiplier() {
        return lootingMul;
    }


    public String getEntityType() {
        return entityType;
    }

    public boolean hasChance(int lootingLvl){
        if(entityType.equals("WITHER")){
            return true;
        }
        return Math.random() < chance + (lootingMul * lootingLvl);
    }

    public boolean hasChance(){
        if(entityType.equals("WITHER")){
            return true;
        }
        return Math.random() < chance;
    }

    private ItemStack createSkull(ConfigurationSection section){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW+ChatColor.translateAlternateColorCodes('&', section.getString("display","")));
        
        ItemBuilder.mutateItemMeta((SkullMeta)meta, initValue+section.getString("texture").replace(initValue, ""));
        item.setItemMeta(meta);
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("mob-head-item", toString());
        return nbtItem.getItem();
    }

    private static <T extends LivingEntity> T cast(Entity en,Class<T> caster){
        
        return caster.cast(en);

    }


}
