package me.wisdom.thepit.controllers;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import org.bukkit.entity.Player;

public class HitCounter {

    public static void incrementCounter(Player player, PitEnchant pitEnchant) {
        if(player == null) return;

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        pitPlayer.enchantHits.putIfAbsent(pitEnchant, 0);
        Integer currentCounter = pitPlayer.enchantHits.get(pitEnchant);

        pitPlayer.enchantHits.put(pitEnchant, currentCounter + 1);
    }

    public static boolean hasReachedThreshold(Player player, PitEnchant pitEnchant, int threshold) {
        if(player == null) return false;

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        pitPlayer.enchantHits.putIfAbsent(pitEnchant, 0);
        Integer currentCounter = pitPlayer.enchantHits.get(pitEnchant);

        if(currentCounter < threshold) return false;

        pitPlayer.enchantHits.put(pitEnchant, 0);
        return true;
    }

    public static void resetCombo(Player player, PitEnchant pitEnchant) {
        if(player == null) return;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        pitPlayer.enchantHits.put(pitEnchant, 0);
    }

    public static void incrementCharge(Player player, PitEnchant pitEnchant) {
        if(player == null) return;

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        pitPlayer.enchantCharge.putIfAbsent(pitEnchant, 0);
        Integer currentCharge = pitPlayer.enchantCharge.get(pitEnchant);

        pitPlayer.enchantCharge.put(pitEnchant, currentCharge + 1);
    }

    public static int getCharge(Player player, PitEnchant pitEnchant) {
        if(player == null) return 0;

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        pitPlayer.enchantCharge.putIfAbsent(pitEnchant, 0);

        return pitPlayer.enchantCharge.get(pitEnchant);
    }

    public static void setCharge(Player player, PitEnchant pitEnchant, int charge) {
        if(player == null) return;

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        pitPlayer.enchantCharge.put(pitEnchant, charge);
    }
}
