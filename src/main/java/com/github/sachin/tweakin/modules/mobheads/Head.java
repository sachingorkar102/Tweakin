package com.github.sachin.tweakin.modules.mobheads;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.prilib.nms.NBTItem;
import com.github.sachin.tweakin.utils.TConstants;
import com.google.common.base.Predicates;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.entity.Fox.Type;
import org.bukkit.entity.Llama.Color;
import org.bukkit.entity.Panda.Gene;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.function.Predicate;


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
    CREEPER("CREEPER","CREEPER_HEAD"),
    CHARGED_CREEPER("CREEPER",(cr)-> ((Creeper)cr).isPowered()),
    DOLPHIN,
    DONKEY,
    DROWNED,
    ELDER_GUARDIAN,
    ENDER_DRAGON("ENDER_DRAGON","DRAGON_HEAD"),
    ENDERMAN,
    ENDERMITE,
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
    
    
    RED_MOOSHROOM("MOOSHROOM",(cow)-> ((MushroomCow)cow).getVariant()==MushroomCow.Variant.RED),
    BROWN_MOOSHROOM("MOOSHROOM",(cow)-> ((MushroomCow)cow).getVariant()==MushroomCow.Variant.BROWN),

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

    TROPICAL_FISH,
    SILVERFISH,
    SKELETON("SKELETON","SKELETON_SKULL"),
    SKELETON_HORSE,
    SLIME,
    SNOW_GOLEM("SNOW_GOLEM"),
    SPIDER,
    SQUID,
    CREAMY_TRADER_LLAMA("TRADER_LLAMA",(llama)-> ((TraderLlama)llama).getColor()== Color.CREAMY),
    WHITE_TRADER_LLAMA("TRADER_LLAMA",(llama)-> ((TraderLlama)llama).getColor()== Color.WHITE),
    BROWN_TRADER_LLAMA("TRADER_LLAMA",(llama)-> ((TraderLlama)llama).getColor()== Color.BROWN),
    GRAY_TRADER_LLAMA("TRADER_LLAMA",(llama)-> ((TraderLlama)llama).getColor()== Color.GRAY),
    TURTLE,
    VEX,
    VEX_2("VEX"),
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
    WITHER_SKELETON("WITHER_SKELETON","WITHER_SKELETON_SKULL"),
    NORMAL_WITHER("WITHER"),
    INVULNERABLE_WITHER("WITHER"),
    NORMAL_ARMORED_WITHER("WITHER"),
    INVULNERABLE_ARMORED_WITHER("WITHER"),
    STRIDER("STRIDER",(strider)-> !((Strider)strider).isShivering()),
    FREEZING_STRIDER("STRIDER",(strider)-> ((Strider)strider).isShivering()),
    ZOGLIN,
    HOGLIN,
    ZOMBIFIED_PIGLIN,
    PIGLIN_BRUTE,
    ZOMBIE("ZOMBIE","ZOMBIE_HEAD"),
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

//    1.18+
    BLUE_AXOLOTL("AXOLOTL",(entity)->Tweakin.getPlugin().getNMSHandler().matchAxoltlVariant(entity, "BLUE")),
    WILD_AXOLOTL("AXOLOTL",(entity)->Tweakin.getPlugin().getNMSHandler().matchAxoltlVariant(entity, "WILD")),
    GOLD_AXOLOTL("AXOLOTL",(entity)->Tweakin.getPlugin().getNMSHandler().matchAxoltlVariant(entity, "GOLD")),
    CYAN_AXOLOTL("AXOLOTL",(entity)->Tweakin.getPlugin().getNMSHandler().matchAxoltlVariant(entity, "CYAN")),
    LUCY_AXOLOTL("AXOLOTL",(entity)->Tweakin.getPlugin().getNMSHandler().matchAxoltlVariant(entity, "LUCY")),
    GLOW_SQUID,
    GOAT("GOAT",(entity)->!Tweakin.getPlugin().getNMSHandler().isScreamingGoat(entity)),
    SCREAMING_GOAT("GOAT",(entity)->Tweakin.getPlugin().getNMSHandler().isScreamingGoat(entity)),

//    1.19+
    WARDEN,
    COLD_FROG("FROG",(entity) -> Tweakin.getPlugin().getNMSHandler().matchFrogVariant(entity,"COLD")),
    TEMPERATE_FROG("FROG",(entity) -> Tweakin.getPlugin().getNMSHandler().matchFrogVariant(entity,"TEMPERATE")),
    WARM_FROG("FROG",(entity) -> Tweakin.getPlugin().getNMSHandler().matchFrogVariant(entity,"WARM")),
    TADPOLE,
    ALLAY,
    CAMEL,
    SNIFFER,

    STRAY,

//    special head after 1.20
    PIGLIN("PIGLIN","PIGLIN_HEAD"),

//    1.20.5

    ARMADILLO,
    PALE_WOLF("WOLF",(entity) -> !((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:PALE")),
    PALE_ANGRY_WOLF("WOLF",(entity) -> ((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:PALE")),


    ASHEN_WOLF("WOLF",(entity) -> !((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:ASHEN")),
    ASHEN_ANGRY_WOLF("WOLF",(entity) -> ((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:ASHEN")),
    BLACK_WOLF("WOLF",(entity) -> !((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:BLACK")),
    BLACK_ANGRY_WOLF("WOLF",(entity) -> ((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:BLACK")),
    CHESTNUT_WOLF("WOLF",(entity) -> !((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:CHESTNUT")),
    CHESTNUT_ANGRY_WOLF("WOLF",(entity) -> ((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:CHESTNUT")),
    RUSTY_WOLF("WOLF",(entity) -> !((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:RUSTY")),
    RUSTY_ANGRY_WOLF("WOLF",(entity) -> ((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:RUSTY")),
    SNOWY_WOLF("WOLF",(entity) -> !((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:SNOWY")),
    SNOWY_ANGRY_WOLF("WOLF",(entity) -> ((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:SNOWY")),
    SPOTTED_WOLF("WOLF",(entity) -> !((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:SPOTTED")),
    SPOTTED_ANGRY_WOLF("WOLF",(entity) -> ((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:SPOTTED")),
    STRIPED_WOLF("WOLF",(entity) -> !((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:STRIPED")),
    STRIPED_ANGRY_WOLF("WOLF",(entity) -> ((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:STRIPED")),
    WOODS_WOLF("WOLF",(entity) -> !((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:WOODS")),
    WOODS_ANGRY_WOLF("WOLF",(entity) -> ((Wolf)entity).isAngry() && Tweakin.getPlugin().getPrilib().getNmsHandler().matchWolfVariant(entity,"MINECRAFT:WOODS")),

//    1.21
    BREEZE,
    BOGGED,
    CREAKING
    ;



    private String entityType;
    private ItemStack skull;
    private double chance;
    private double lootingMul;
    private ConfigurationSection section;


    private String wolfVarient;
    
    
    private Predicate<Entity> check;

    private Head(String entityType,Predicate<Entity> check){
        this.check = check;
        
        this.section = MobHeadsTweak.headConfig.getConfigurationSection(toString().toLowerCase());
        this.lootingMul = section.getDouble("looting",0.0);
        this.chance = section.getDouble("chance",0);
        this.skull = createSkull(section);
        this.entityType = entityType;

    }


    private Head(String entityType,String mat){
        this.entityType = entityType;
        this.check = Predicates.alwaysTrue();
        this.section = MobHeadsTweak.headConfig.getConfigurationSection(toString().toLowerCase());
        this.lootingMul = section.getDouble("looting",0.0);
        this.chance = section.getDouble("chance",0);
        Material material = Material.getMaterial(mat);

        if(material != null){
            this.skull = new ItemStack(material);
        }
        else{
            this.skull = createSkull(section);
        }
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


    public void reload(){
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
        SkullMeta skullMeta = (SkullMeta) meta;
        Tweakin.getPlugin().getNMSHandler().applyHeadTexture(skullMeta, TConstants.INIT_HEAD_TEXTURE_VALUE+section.getString("texture").replace(TConstants.INIT_HEAD_TEXTURE_VALUE, ""));
        if(Tweakin.getPlugin().isPost1_20()){
            String sound = section.getString("note-block-sound","none");
            if(!sound.equalsIgnoreCase("none")){
                skullMeta.setNoteBlockSound(NamespacedKey.minecraft(sound));
            }
        }
        skullMeta.setDisplayName(ChatColor.YELLOW+ChatColor.translateAlternateColorCodes('&', section.getString("display","")));
        skullMeta.getPersistentDataContainer().set(Tweakin.getKey("mob-head-enum"), PersistentDataType.STRING,toString());
        item.setItemMeta(skullMeta);
//        NBTItem nbtItem = Tweakin.getPlugin().getNMSHandler().newItem(item);
//        nbtItem.setString("mob-head-item", toString());

//        return nbtItem.getItem();
        return item;
    }

    private static <T extends LivingEntity> T cast(Entity en,Class<T> caster){
        
        return caster.cast(en);

    }


}
