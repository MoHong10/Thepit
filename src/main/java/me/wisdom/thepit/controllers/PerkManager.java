package me.wisdom.thepit.controllers;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.*;
import me.wisdom.thepit.megastreaks.NoMegastreak;
import me.wisdom.thepit.perks.NoPerk;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PerkManager {
    public static List<PitPerk> pitPerks = new ArrayList<>();
    public static List<Megastreak> megastreaks = new ArrayList<>();
    public static List<Killstreak> killstreaks = new ArrayList<>();

    public static void registerUpgrade(PitPerk pitPerk) {
        pitPerks.add(pitPerk);
        if(Thepit.status.isOverworld()) Thepit.INSTANCE.getServer().getPluginManager().registerEvents(pitPerk, Thepit.INSTANCE);
    }

    public static void registerMegastreak(Megastreak megastreak) {
        megastreaks.add(megastreak);
        if(Thepit.status.isOverworld()) Thepit.INSTANCE.getServer().getPluginManager().registerEvents(megastreak, Thepit.INSTANCE);
    }

    public static void registerKillstreak(Killstreak killstreak) {
        killstreaks.add(killstreak);
        Thepit.INSTANCE.getServer().getPluginManager().registerEvents(killstreak, Thepit.INSTANCE);
    }

    public static ChatColor getChatColor(Player player, PitPerk pitPerk) {
        if(!isUnlocked(player, pitPerk)) return ChatColor.RED;
        if(isEquipped(player, pitPerk)) return ChatColor.GREEN;
        return ChatColor.YELLOW;
    }

    public static ChatColor getChatColor(Player player, Megastreak megastreak) {
        if(!isUnlocked(player, megastreak)) return ChatColor.RED;
        if(isEquipped(player, megastreak)) return ChatColor.GREEN;
        return ChatColor.YELLOW;
    }

    public static boolean isEquipped(Player player, PitPerk pitPerk) {
        if(!isUnlocked(player, pitPerk)) return false;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        for(PitPerk testPerk : pitPlayer.pitPerks) if(testPerk == pitPerk) return true;
        return false;
    }

    public static boolean isEquipped(Player player, Megastreak megastreak) {
        if(!isUnlocked(player, megastreak)) return false;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        return pitPlayer.getMegastreak() == megastreak;
    }

    public static boolean isUnlocked(Player player, PitPerk pitPerk) {
        if(pitPerk.renownUpgradeClass == null) return true;
        RenownUpgrade upgrade = UpgradeManager.getUpgrade(pitPerk.renownUpgradeClass);
        return UpgradeManager.hasUpgrade(player, upgrade);
    }

    public static boolean isUnlocked(Player player, Megastreak megastreak) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        return pitPlayer.prestige >= megastreak.prestigeReq;
    }

    public static PitPerk getPitPerk(String refName) {
        if(refName == null) return NoPerk.INSTANCE;
        for(PitPerk pitPerk : pitPerks) if(pitPerk.refName.equalsIgnoreCase(refName)) return pitPerk;
        return NoPerk.INSTANCE;
    }

    public static Megastreak getMegastreak(String refName) {
        if(refName == null) return NoMegastreak.INSTANCE;
        for(Megastreak megastreak : megastreaks) if(megastreak.refName.equalsIgnoreCase(refName)) return megastreak;
        return NoMegastreak.INSTANCE;
    }
}
