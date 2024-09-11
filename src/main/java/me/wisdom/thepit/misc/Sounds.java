package me.wisdom.thepit.misc;

import me.wisdom.thepit.Thepit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Sounds {

    //	General
    public static final SoundEffect ITEM_PICKUP = new SoundEffect(Sound.ENTITY_ITEM_PICKUP, 1, 1);
    public static final SoundEffect SOUL_PICKUP = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.7936507);
    public static final SoundEffect SUCCESS = new SoundEffect(Sound.BLOCK_NOTE_PLING, 1, 2);
    public static final SoundEffect ERROR = new SoundEffect(Sound.ENTITY_ENDERMEN_TELEPORT, 1, 0.5);
    public static final SoundEffect NO = new SoundEffect(Sound.ENTITY_VILLAGER_NO, 1, 1);
    public static final SoundEffect WARNING_LOUD = new SoundEffect(Sound.BLOCK_NOTE_PLING, 1000, 1);

    //	Game / Misc
    public static final SoundEffect SOUL_EXPLOSION = new SoundEffect(Sound.ENTITY_FIREWORK_BLAST, 1, 1);
    public static final SoundEffect LEVEL_UP = new SoundEffect(Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    public static final SoundEffect PRESTIGE = new SoundEffect(Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
    public static final SoundEffect ASSIST = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.7301587);
    public static final SoundEffect BOUNTY = new SoundEffect();
    public static final SoundEffect DEATH_FALL = new SoundEffect(Sound.ENTITY_PLAYER_BIG_FALL, 1000, 1);
    public static final SoundEffect JEWEL_FIND = new SoundEffect(Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
    public static final SoundEffect MYSTIC_WELL_OPEN_1 = new SoundEffect(Sound.ENTITY_GHAST_SHOOT, 0.1F, 0.5F);
    public static final SoundEffect MYSTIC_WELL_OPEN_2 = new SoundEffect(Sound.ENTITY_ITEM_PICKUP, 1F, 0.9F);
    public static final SoundEffect MULTI_1 = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.7936507);
    public static final SoundEffect MULTI_2 = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.8253968);
    public static final SoundEffect MULTI_3 = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.8730159);
    public static final SoundEffect MULTI_4 = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.9047619);
    public static final SoundEffect MULTI_5 = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.9523809);
    public static final SoundEffect IRON_1 = new SoundEffect(Sound.BLOCK_ANVIL_LAND, 0.5, 1.7936507);
    public static final SoundEffect IRON_2 = new SoundEffect(Sound.BLOCK_ANVIL_LAND, 0.5, 1.8253968);
    public static final SoundEffect IRON_3 = new SoundEffect(Sound.BLOCK_ANVIL_LAND, 0.5, 1.8730159);
    public static final SoundEffect IRON_4 = new SoundEffect(Sound.BLOCK_ANVIL_LAND, 0.5, 1.9047619);
    public static final SoundEffect IRON_5 = new SoundEffect(Sound.BLOCK_ANVIL_LAND, 0.5, 1.9523809);
    public static final SoundEffect GEM_CRAFT = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 1.5F);
    public static final SoundEffect GEM_USE = new SoundEffect(Sound.BLOCK_GLASS_BREAK, 1, 2);
    public static final SoundEffect JEWEL_SHRED1 = new SoundEffect(Sound.ENTITY_HORSE_ARMOR, 1, 0.8);
    public static final SoundEffect JEWEL_SHRED2 = new SoundEffect(Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 0.5, 0.75);
    public static final SoundEffect SHARD_FIND = new SoundEffect(Sound.BLOCK_GLASS_BREAK, 1, 2);
    public static final SoundEffect WITHERCRAFT_1 = new SoundEffect(Sound.ENTITY_ENDERMEN_DEATH, 2F, 1.2F);
    public static final SoundEffect WITHERCRAFT_2 = new SoundEffect(Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1.5F);
    public static final SoundEffect ENDERCHEST_OPEN = new SoundEffect(Sound.BLOCK_CHEST_OPEN, 1, 1);
    public static final SoundEffect ARMOR_SWAP = new SoundEffect(Sound.ENTITY_HORSE_ARMOR, 1F, 1.3F);
    public static final SoundEffect COMPENSATION = new SoundEffect(Sound.BLOCK_NOTE_PLING, 2, 1.5F);
    public static final SoundEffect RENOWN_SHOP_PURCHASE = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 1.5F);
    public static final SoundEffect FUNKY_FEATHER = new SoundEffect(Sound.ENTITY_BAT_TAKEOFF, 2, 2F);
    public static final SoundEffect CLEAR_JEWEL = new SoundEffect(Sound.ENTITY_SHEEP_SHEAR, 1, 2);
    public static final SoundEffect YUMMY_BREAD = new SoundEffect(Sound.ENTITY_HORSE_EAT, 1, 1.2);
    public static final SoundEffect BREAD_GIVE = new SoundEffect(Sound.ENTITY_GHAST_AMBIENT, 1, 0.3);
    public static final SoundEffect SURVIVOR_HEAL = new SoundEffect()
            .add(new SoundMoment(0).add(Sound.ENTITY_SILVERFISH_AMBIENT, 1, 1.8))
            .add(new SoundMoment(1).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2))
            .add(new SoundMoment(3).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.9))
            .add(new SoundMoment(5).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.85))
            .add(new SoundMoment(10).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.9))
            .add(new SoundMoment(13).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2));
    public static final SoundEffect SHOCKWAVE = new SoundEffect(Sound.ENTITY_GENERIC_EXPLODE, 2, 1.6);
    public static final SoundEffect BOOSTER_REMIND = new SoundEffect(Sound.ENTITY_CHICKEN_EGG, 2, 1.6);
    public static final SoundEffect BUTTON = new SoundEffect(Sound.UI_BUTTON_CLICK, 2, 1);
    public static final SoundEffect POTION_BUBBLE = new SoundEffect(Sound.BLOCK_LAVA_POP, 1.5, 1);
    public static final SoundEffect AEGIS = new SoundEffect(Sound.BLOCK_ANVIL_LAND, 1, 1.5);
    public static final SoundEffect TAINTED_CRAFT = new SoundEffect(Sound.BLOCK_ANVIL_USE, 2, 1.25F);
    public static final SoundEffect SPLASH_CRAFT1 = new SoundEffect(Sound.BLOCK_GLASS_BREAK, 2, 1.5F);
    public static final SoundEffect SPLASH_CRAFT2 = new SoundEffect(Sound.BLOCK_LAVA_POP, 2, 1.75F);
    public static final SoundEffect TUTORIAL_MESSAGE = new SoundEffect(Sound.ENTITY_CHICKEN_EGG, 2, 1.6);
    public static final SoundEffect LOAD_INITIAL = new SoundEffect(Sound.BLOCK_NOTE_PLING, 1000, 1.2);
    public static final SoundEffect LOAD_FINAL = new SoundEffect(Sound.BLOCK_NOTE_PLING, 1000, 1.8);
    public static final SoundEffect PEDESTAL_ACTIVATE = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5);
    public static final SoundEffect PEDESTAL_DEACTIVATE = new SoundEffect(Sound.BLOCK_STONE_STEP, 2, 0.2);
    public static final SoundEffect XP_GAIN = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    public static final SoundEffect ALTAR_ROLL = new SoundEffect(Sound.ENTITY_WITHER_SPAWN, 1, 1);
    public static final SoundEffect VOUCHER_USE = new SoundEffect(Sound.ENTITY_IRONGOLEM_DEATH, 1, 0.8);
    public static final SoundEffect HEARTBEAT1 = new SoundEffect(Sound.BLOCK_NOTE_BASEDRUM, 1, 0.6);
    public static final SoundEffect HEARTBEAT2 = new SoundEffect(Sound.BLOCK_NOTE_BASEDRUM, 1, 0.8);
    public static final SoundEffect OUTPOST = new SoundEffect(Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 0.5, 1.2);

    public static final SoundEffect SNAKE_ICE = new SoundEffect(Sound.BLOCK_GLASS_BREAK, 1.3, 1.5);
    public static final SoundEffect BONE_SNAKE = new SoundEffect(Sound.ENTITY_SKELETON_HURT, 1.3, 1.5);
    public static final SoundEffect WITHER_SNAKE = new SoundEffect(Sound.BLOCK_STONE_STEP, 1.3, 1.5);
    public static final SoundEffect ANVIL_RAIN = new SoundEffect(Sound.BLOCK_ANVIL_LAND, 0.5, 0.45);
    public static final SoundEffect FIRE_EXTINGUISH = new SoundEffect(Sound.ENTITY_FIREWORK_TWINKLE, 1.3, 0.45);
    public static final SoundEffect BLOCK_LAND = new SoundEffect(Sound.BLOCK_STONE_BREAK, 1.3, 0.45);
    public static final SoundEffect TNT_PLACE = new SoundEffect(Sound.BLOCK_GLASS_BREAK, 1.3, 1);
    public static final SoundEffect TNT_PRIME = new SoundEffect("game.tnt.primed", 1.3, 1);
    public static final SoundEffect DISORDER = new SoundEffect(Sound.ENTITY_ENDERMEN_TELEPORT, 1.3, 1.5);
    public static final SoundEffect DISORDER2 = new SoundEffect(Sound.BLOCK_PORTAL_TRAVEL, 1, 1.25);
    public static final SoundEffect SLAM = new SoundEffect(Sound.BLOCK_GLASS_BREAK, 1.0, 0.65);
    public static final SoundEffect SLAM_2 = new SoundEffect(Sound.ENTITY_GENERIC_EXPLODE, 0.5, 1);
    public static final SoundEffect COLLAPSE = new SoundEffect(Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1, 0.4);
    public static final SoundEffect COLLAPSE2 = new SoundEffect(Sound.BLOCK_GRASS_BREAK, 1, 0.4);
    public static final SoundEffect CHARGE = new SoundEffect(Sound.ENTITY_GHAST_SHOOT, 1, 1.2);
    public static final SoundEffect POPUP = new SoundEffect(Sound.BLOCK_FIRE_AMBIENT, 1, 1.2);
    public static final SoundEffect TELEPORT = new SoundEffect(Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1.4);
    public static final SoundEffect SOUL_DROP = new SoundEffect(Sound.ENTITY_ITEM_PICKUP, 1, 1.4);
    public static final SoundEffect BEAM_CONNECT = new SoundEffect(Sound.BLOCK_FIRE_AMBIENT, 1, 1.4);
    public static final SoundEffect TURMOIL = new SoundEffect(Sound.BLOCK_NOTE_BASEDRUM, 1, 1.4);
    public static final SoundEffect FAST_TRAVEL = new SoundEffect(Sound.ENTITY_ENDERMEN_TELEPORT, 2, 1.4);
    public static final SoundEffect SHIELD_REACTIVATE = new SoundEffect(Sound.BLOCK_ANVIL_LAND, 1, 2.0);
    public static final SoundEffect SHIELD_BREAK = new SoundEffect(Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1, 1);

    public static final SoundEffect CAGE = new SoundEffect()
            .add(new SoundMoment(0).add(Sound.BLOCK_ANVIL_LAND, 1, 0.7))
            .add(new SoundMoment(2).add(Sound.BLOCK_ANVIL_LAND, 1, 0.8))
            .add(new SoundMoment(4).add(Sound.BLOCK_ANVIL_LAND, 1, 0.9))
            .add(new SoundMoment(6).add(Sound.BLOCK_ANVIL_LAND, 1, 1.0))
            .add(new SoundMoment(8).add(Sound.BLOCK_ANVIL_LAND, 1, 1.1))
            .add(new SoundMoment(10).add(Sound.BLOCK_ANVIL_LAND, 1, 1.2));

    public static final SoundEffect PULL = new SoundEffect()
            .add(new SoundMoment(0).add(Sound.BLOCK_GRASS_BREAK, 1, 0.7))
            .add(new SoundMoment(2).add(Sound.BLOCK_GRASS_BREAK, 1, 0.8))
            .add(new SoundMoment(4).add(Sound.BLOCK_GRASS_BREAK, 1, 0.9))
            .add(new SoundMoment(6).add(Sound.BLOCK_GRASS_BREAK, 1, 1.0))
            .add(new SoundMoment(8).add(Sound.BLOCK_GRASS_BREAK, 1, 1.1))
            .add(new SoundMoment(10).add(Sound.BLOCK_GRASS_BREAK, 1, 1.2))
            .add(new SoundMoment(12).add(Sound.BLOCK_GRASS_BREAK, 1, 1.3))
            .add(new SoundMoment(14).add(Sound.BLOCK_GRASS_BREAK, 1, 1.4));

    public static final SoundEffect REINCARNATION = new SoundEffect()
            .add(new SoundMoment(2).add(Sound.BLOCK_NOTE_BASS, 1, 0.8))
            .add(new SoundMoment(4).add(Sound.BLOCK_NOTE_BASS, 1, 0.9))
            .add(new SoundMoment(6).add(Sound.BLOCK_NOTE_BASS, 1, 1.0))
            .add(new SoundMoment(8).add(Sound.BLOCK_NOTE_BASS, 1, 1.1))
            .add(new SoundMoment(10).add(Sound.BLOCK_NOTE_BASS, 1, 1.2))
            .add(new SoundMoment(60).add(Sound.BLOCK_FIRE_EXTINGUISH, 1, 0.8))
            .add(new SoundMoment(62).add(Sound.BLOCK_FIRE_EXTINGUISH, 1, 0.9))
            .add(new SoundMoment(64).add(Sound.BLOCK_FIRE_EXTINGUISH, 1, 1.0))
            .add(new SoundMoment(66).add(Sound.BLOCK_FIRE_EXTINGUISH, 1, 1.1))
            .add(new SoundMoment(68).add(Sound.BLOCK_FIRE_EXTINGUISH, 1, 1.2));
    public static final SoundEffect REINCARNATION_END = new SoundEffect(Sound.ENTITY_WITHER_SPAWN, 1, 1);


    //	Enchants
    public static final SoundEffect BILLIONAIRE = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.73);
    public static final SoundEffect BULLET_TIME = new SoundEffect(Sound.BLOCK_FIRE_EXTINGUISH, 1, 1.5);
    public static final SoundEffect COMBO_PROC = new SoundEffect(Sound.ENTITY_DONKEY_DEATH, 1, 0.5);
    public static final SoundEffect COMBO_STUN = new SoundEffect(Sound.BLOCK_ANVIL_LAND, 1, 1);
    public static final SoundEffect CRUSH = new SoundEffect(Sound.BLOCK_GLASS_FALL, 1, 0.80);
    public static final SoundEffect EXE = new SoundEffect(Sound.ENTITY_VILLAGER_DEATH, 1, 0.5);
    public static final SoundEffect EXPLOSIVE_1 = new SoundEffect(Sound.ENTITY_GENERIC_EXPLODE, 0.75, 2);
    public static final SoundEffect EXPLOSIVE_2 = new SoundEffect(Sound.ENTITY_GENERIC_EXPLODE, 0.75, 1);
    public static final SoundEffect EXPLOSIVE_3 = new SoundEffect(Sound.ENTITY_GENERIC_EXPLODE, 0.75, 1.4);
    public static final SoundEffect BERSERKER = new SoundEffect(Sound.ENTITY_SILVERFISH_DEATH, 1, 1.4);
    public static final SoundEffect GAMBLE_YES = new SoundEffect(Sound.BLOCK_NOTE_PLING, 1, 3);
    public static final SoundEffect GAMBLE_NO = new SoundEffect(Sound.BLOCK_NOTE_PLING, 1, 1.5);
    public static final SoundEffect LAST_STAND = new SoundEffect(Sound.BLOCK_ANVIL_BREAK, 1, 1);
    public static final SoundEffect LUCKY_SHOT = new SoundEffect(Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1, 1);
    public static final SoundEffect PIN_DOWN = new SoundEffect(Sound.ENTITY_PLAYER_BURP, 1, 1);
    public static final SoundEffect RGM = new SoundEffect(Sound.ENTITY_ENDERDRAGON_HURT, 1, 1);
    public static final SoundEffect TELEBOW = new SoundEffect(Sound.ENTITY_ENDERMEN_TELEPORT, 1, 2);
    public static final SoundEffect VENOM = new SoundEffect(Sound.ENTITY_SPIDER_DEATH, 1, 1);
    public static final SoundEffect VOLLEY = new SoundEffect(Sound.ENTITY_ARROW_SHOOT, 1, 1);
    public static final SoundEffect PULLBOW = new SoundEffect(Sound.ENTITY_BAT_TAKEOFF, 1, 1);

    public static final SoundEffect REPEL = new SoundEffect(Sound.BLOCK_ANVIL_LAND, 1, 0.5);
    public static final SoundEffect FREEZE1 = new SoundEffect(Sound.BLOCK_SNOW_BREAK, 2, 0.5);
    public static final SoundEffect FREEZE2 = new SoundEffect(Sound.BLOCK_GLASS_FALL, 1, 1.2);
    public static final SoundEffect SWEEP = new SoundEffect(Sound.ENTITY_BAT_TAKEOFF, 1, 0.5);
    public static final SoundEffect CLEAVE1 = new SoundEffect(Sound.ENTITY_IRONGOLEM_HURT, 1, 0.5);
    public static final SoundEffect CLEAVE2 = new SoundEffect(Sound.ENTITY_ITEM_BREAK, 1, 0.75);
    public static final SoundEffect CLEAVE3 = new SoundEffect(Sound.BLOCK_FIRE_AMBIENT, 1, 1.25);
    public static final SoundEffect WARP = new SoundEffect(Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1.25);
    public static final SoundEffect EXTRACT = new SoundEffect(Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1, 0.8);

    //	Darkzone
    public static final SoundEffect MOB_KILL = new SoundEffect().add(new SoundMoment(0)
            .add(Sound.BLOCK_LAVA_POP, 1, 1.3)
            .add(Sound.BLOCK_STONE_BREAK, 0.8, 0.7));
    public static final SoundEffect DEVOUR = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.4);
    public static final SoundEffect ELECTRIC_SHOCK = new SoundEffect(Sound.BLOCK_FIRE_AMBIENT, 1, 2);
    public static final SoundEffect MEDIC = new SoundEffect()
            .add(new SoundMoment(0).add("random.pop", 1, 0.7))
            .add(new SoundMoment(2).add("random.pop", 1, 0.8))
            .add(new SoundMoment(4).add("random.pop", 1, 0.9))
            .add(new SoundMoment(6).add("random.pop", 1, 1.0))
            .add(new SoundMoment(8).add("random.pop", 1, 1.1))
            .add(new SoundMoment(10).add("random.pop", 1, 1.2));
    public static final SoundEffect BIPOLAR_VENGEANCE = new SoundEffect(Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
    public static final SoundEffect BIPOLAR_PEACE = new SoundEffect(Sound.ENTITY_CAT_HISS, 1, 1);
    public static final SoundEffect LEECH = new SoundEffect(Sound.ENTITY_SILVERFISH_DEATH, 1, 1);
    public static final SoundEffect ROLLING_THUNDER_START = new SoundEffect(Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
    public static final SoundEffect PITPOCKET = new SoundEffect(Sound.ENTITY_PLAYER_BURP, 1, 1);
    public static final SoundEffect ADRENALINE = new SoundEffect(Sound.BLOCK_NOTE_PLING, 1, 1.65);
    public static final SoundEffect HEMORRHAGE = new SoundEffect(Sound.BLOCK_STONE_BREAK, 1, 0.8);
    public static final SoundEffect METEOR = new SoundEffect(Sound.BLOCK_FIRE_AMBIENT, 0.7, 0.7);
    public static final SoundEffect METEOR_2 = new SoundEffect(Sound.ENTITY_GENERIC_EXPLODE, 3, 1.4);
    public static final SoundEffect BOSS_SPAWN = new SoundEffect(Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
    public static final SoundEffect CAKE_CONSUME = new SoundEffect(Sound.BLOCK_PORTAL_TRAVEL, 1, 1);

    //	Megastreaks
    public static final SoundEffect MEGA_GENERAL = new SoundEffect(Sound.ENTITY_WITHER_SPAWN, 1000, 1);
    public static final SoundEffect MEGA_RNGESUS = new SoundEffect(Sound.BLOCK_PORTAL_TRIGGER, 1000, 1);
    public static final SoundEffect RNGESUS_DESTABILIZE = new SoundEffect(Sound.ENTITY_ENDERMEN_DEATH, 1000, 1);
    public static final SoundEffect UBER_100 = new SoundEffect("mob.guardian.curse", 1000, 1);
    public static final SoundEffect UBER_200 = new SoundEffect("mob.guardian.curse", 1000, 1);
    public static final SoundEffect UBER_300 = new SoundEffect("mob.guardian.curse", 1000, 1);
    public static final SoundEffect UBER_400 = new SoundEffect("mob.guardian.curse", 1000, 1);
    public static final SoundEffect UBER_500 = new SoundEffect("mob.guardian.curse", 1000, 1);

    //	Upgrades
    public static final SoundEffect STREAKER = new SoundEffect(Sound.ENTITY_PLAYER_BURP, 2, 1.2F);

    //	Helmets
    public static final SoundEffect HELMET_CRAFT = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 1.5F);
    public static final SoundEffect HELMET_GUI_OPEN = new SoundEffect(Sound.BLOCK_ANVIL_BREAK, 1, 2);
    public static final SoundEffect HELMET_DOWNGRADE = new SoundEffect(Sound.BLOCK_ANVIL_BREAK, 1, 2);
    public static final SoundEffect HELMET_ACTIVATE = new SoundEffect(Sound.BLOCK_NOTE_PLING, 1.3, 2);
    public static final SoundEffect HELMET_DEPOSIT_GOLD = new SoundEffect(Sound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 1, 2);
    public static final SoundEffect HELMET_TICK = new SoundEffect(Sound.BLOCK_NOTE_SNARE, 5, 1.5);
    public static final SoundEffect GOLD_RUSH = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.9);
    public static final SoundEffect LEAP = new SoundEffect(Sound.ENTITY_BAT_TAKEOFF, 1, 1);
    public static final SoundEffect MANA = new SoundEffect(Sound.BLOCK_PORTAL_TRAVEL, 0.2, 2);
    public static final SoundEffect PHOENIX = new SoundEffect()
            .add(new SoundMoment(0).add(Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1).add(Sound.BLOCK_FIRE_AMBIENT, 1, 1));
    //	Judgement
    public static final SoundEffect JUDGEMENT_HEAL = new SoundEffect(Sound.ENTITY_PLAYER_BURP, 1, 1);
    public static final SoundEffect JUDGEMENT_WITHER = new SoundEffect(Sound.ENTITY_WITHER_SHOOT, 1, 1);
    public static final SoundEffect JUDGEMENT_RESISTANCE = new SoundEffect(Sound.ENTITY_IRONGOLEM_HURT, 1, 1);
    public static final SoundEffect JUDGEMENT_STRENGTH = new SoundEffect(Sound.ENTITY_ENDERMEN_SCREAM, 1, 1);
    public static final SoundEffect JUDGEMENT_SLOW = new SoundEffect(Sound.BLOCK_ANVIL_LAND, 1, 1);
    public static final SoundEffect JUDGEMENT_HALF_ATTACKER = new SoundEffect(Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1, 1);
    public static final SoundEffect JUDGEMENT_HALF_DEFENDER = new SoundEffect("mob.guardian.curse", 1000, 1);
    public static final SoundEffect JUDGEMENT_ZEUS_ATTACKER = new SoundEffect(Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
    public static final SoundEffect JUDGEMENT_ZEUS_DEFENDER = new SoundEffect(Sound.ENTITY_IRONGOLEM_DEATH, 1, 1);
    public static final SoundEffect JUDGEMENT_HOPPER = new SoundEffect(Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);

    public static final SoundEffect ANVIL_LAND = new SoundEffect(Sound.BLOCK_ANVIL_LAND, 1, 1);
    public static final SoundEffect ANVIL_BREAK = new SoundEffect(Sound.BLOCK_ANVIL_BREAK, 1, 1);
    public static final SoundEffect ANVIL_USE = new SoundEffect(Sound.BLOCK_ANVIL_USE, 1, 1);

    public static final SoundEffect WITHER_SHOOT = new SoundEffect(Sound.ENTITY_WITHER_SHOOT, 1, 1);

    //	Events
    public static final SoundEffect EVENT_START = new SoundEffect(Sound.ENTITY_ENDERDRAGON_GROWL, 2, 1);
    public static final SoundEffect EVENT_PING = new SoundEffect(Sound.BLOCK_NOTE_PLING, 2, 1F);
    public static final SoundEffect CTF_EXPLOSION = new SoundEffect(Sound.ENTITY_GENERIC_EXPLODE, 1, 2);
    public static final SoundEffect CTF_FLAG_STEAL = new SoundEffect(Sound.BLOCK_NOTE_PLING, 2, 2F);
    public static final SoundEffect CTF_FLAG_STOLEN = new SoundEffect(Sound.BLOCK_NOTE_PLING, 2, 0.5F);
    public static final SoundEffect CTF_FLAG_CAPTURED = new SoundEffect(Sound.ENTITY_BLAZE_DEATH, 2, 0.5F);
    public static final SoundEffect CTF_FLAG_SCORE = new SoundEffect(Sound.ENTITY_PLAYER_LEVELUP, 2, 0.5F);
    public static final SoundEffect JUGGERNAUT_EXPLOSION = new SoundEffect(Sound.ENTITY_GENERIC_EXPLODE, 1, 2);
    public static final SoundEffect JUGGERNAUT_END = new SoundEffect(Sound.ENTITY_ENDERDRAGON_DEATH, 1, 2);

    //	Cosmetics
    public static final SoundEffect KILL_FIRE = new SoundEffect(Sound.BLOCK_FIRE_AMBIENT, 2, 2);
    public static final SoundEffect DEATH_GHAST_SCREAM = new SoundEffect(Sound.ENTITY_GHAST_SCREAM, 1, 1);
    public static final SoundEffect DEATH_HOWL = new SoundEffect(Sound.ENTITY_WOLF_HOWL, 1, 1);
    public static final SoundEffect DEATH_EXPLOSION = new SoundEffect(Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
    public static final SoundEffect KYRO_LIFESTEAL_LOSE = new SoundEffect(Sound.BLOCK_FIRE_AMBIENT, 1, 1);
    public static final SoundEffect KYRO_LIFESTEAL_GAIN = new SoundEffect(Sound.ENTITY_PLAYER_BURP, 1, 1);

    //	Quests
    public static final SoundEffect COMPLETE_QUEST = new SoundEffect(Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    public static final SoundEffect GIVE_REWARD = new SoundEffect(Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    public static final SoundEffect PUNCH_UNIQUE_PLAYER = new SoundEffect(Sound.ENTITY_VILLAGER_YES, 1, 1);

    //  Mobs
    public static final SoundEffect CREEPER_EXPLODE = new SoundEffect(Sound.ENTITY_GENERIC_EXPLODE, 1, 1);

	/*
	public static final SoundEffect SIMPLE_SOUND = new SoundEffect(Sound.ZOMBIE_WOODBREAK, 1, 1);
	public static final SoundEffect COMPLEX_SOUND = new SoundEffect();
	static {
		COMPLEX_SOUND
				.add(new SoundMoment(0)
						.add(Sound.ORB_PICKUP, 1, 1)
						.add(Sound.EXPLODE, 1, 1))
				.add(new SoundMoment(2)
						.add(Sound.ORB_PICKUP, 1, 1)
						.add(Sound.IRONGOLEM_DEATH, 1, 1));
	}
	*/

    public static class SoundEffect {
        private SoundMoment soundMoment;
        private final List<SoundMoment> soundTimeList = new ArrayList<>();

        public SoundEffect() {
        }

        public SoundEffect(Sound sound, double volume, double pitch) {
            this.soundMoment = new SoundMoment(new SoundMoment.BukkitSound(sound, volume, pitch));
        }

        public SoundEffect(String soundString, double volume, double pitch) {
            this.soundMoment = new SoundMoment(new SoundMoment.BukkitSound(soundString, volume, pitch));
        }

        public SoundEffect add(SoundMoment soundMoment) {
            soundTimeList.add(soundMoment);
            return this;
        }

        public void play(Location location, double radius) {
            for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if(onlinePlayer.getWorld() != location.getWorld() || onlinePlayer.getLocation().distance(location) > radius)
                    continue;
                play(onlinePlayer, -1, -1);
            }
        }

        public void play(Location location, double radius, float volume, float pitch) {
            for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if(onlinePlayer.getWorld() != location.getWorld() || onlinePlayer.getLocation().distance(location) > radius)
                    continue;
                play(onlinePlayer, volume, pitch);
            }
        }

        public void play(LivingEntity checkPlayer) {
            play(checkPlayer, -1, -1);
        }

        public void play(LivingEntity checkPlayer, float volume, float pitch) {
            if(!(checkPlayer instanceof Player)) return;
            Player player = (Player) checkPlayer;

            if(!player.isOnline()) return;
            if(soundMoment != null) {
                if(volume >= 0 && pitch >= 0) soundMoment.play(player, volume, pitch);
                else soundMoment.play(player);
                return;
            }
            List<SoundMoment> soundTimeList = new ArrayList<>(this.soundTimeList);
            new BukkitRunnable() {
                int count = 0;
                @Override
                public void run() {
                    for(SoundMoment soundMoment : new ArrayList<>(soundTimeList)) {
                        if(soundMoment.tick != count) continue;
                        soundTimeList.remove(soundMoment);
                        soundMoment.play(player);
                    }
                    if(soundTimeList.isEmpty()) cancel();
                    count++;
                }
            }.runTaskTimer(Thepit.INSTANCE, 0L, 1L);
        }

        public void play(Location location) {
            if(soundMoment != null) {
                soundMoment.play(location);
                return;
            }
            List<SoundMoment> soundTimeList = new ArrayList<>(this.soundTimeList);
            new BukkitRunnable() {
                @Override
                public void run() {
                    int count = 0;
                    List<SoundMoment> toRemove = new ArrayList<>();
                    for(SoundMoment soundMoment : soundTimeList) {
                        if(soundMoment.tick == count) {
                            toRemove.add(soundMoment);
                            soundMoment.play(location);
                        }
                        count++;
                    }
                    soundTimeList.removeAll(toRemove);
                    if(soundTimeList.isEmpty()) cancel();
                }
            }.runTaskTimer(Thepit.INSTANCE, 0L, 1L);
        }
    }

    public static class SoundMoment {
        private final List<BukkitSound> bukkitSounds = new ArrayList<>();
        private int tick;

        //			Time constructor
        public SoundMoment(int tick) {
            this.tick = tick;
        }

        //			Sound with no time
        public SoundMoment(BukkitSound bukkitSound) {
            this.bukkitSounds.add(bukkitSound);
        }

        //			Add sound to time constructed with time constructor
        public SoundMoment add(Sound sound, double volume, double pitch) {
            bukkitSounds.add(new BukkitSound(sound, volume, pitch));
            return this;
        }

        public SoundMoment add(String soundString, int volume, double pitch) {
            bukkitSounds.add(new BukkitSound(soundString, volume, pitch));
            return this;
        }

        public void play(Player player) {
            if(!player.isOnline()) return;
            for(BukkitSound bukkitSound : bukkitSounds) {
                if(bukkitSound.sound != null) {
                    player.playSound(player.getLocation(), bukkitSound.sound, bukkitSound.volume, bukkitSound.pitch);
                } else {
                    player.playSound(player.getLocation(), bukkitSound.soundString, bukkitSound.volume, bukkitSound.pitch);
                }
            }
        }

        public void play(Player player, float volume, float pitch) {
            if(!player.isOnline()) return;
            for(BukkitSound bukkitSound : bukkitSounds) {
                if(bukkitSound.sound != null) {
                    player.playSound(player.getLocation(), bukkitSound.sound, volume, pitch);
                } else {
                    player.playSound(player.getLocation(), bukkitSound.soundString, volume, pitch);
                }
            }
        }

        public void play(Location location) {
            for(BukkitSound bukkitSound : bukkitSounds) {
                if(bukkitSound.sound != null)
                    location.getWorld().playSound(location, bukkitSound.sound, bukkitSound.volume, bukkitSound.pitch);
            }
        }

        public static class BukkitSound {
            private Sound sound;
            private String soundString;
            private final float volume;
            private final float pitch;

            private BukkitSound(Sound sound, double volume, double pitch) {
                this.sound = sound;
                this.volume = (float) volume;
                this.pitch = (float) pitch;
            }

            private BukkitSound(String soundString, double volume, double pitch) {
                this.soundString = soundString;
                this.volume = (float) volume;
                this.pitch = (float) pitch;
            }
        }
    }
}
