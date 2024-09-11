package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;

import java.util.List;

public class GoldBump extends PitEnchant {

    public GoldBump() {
        super("Gold Bump", false, ApplyType.ALL,
                "goldbump", "gold-bump", "bump", "gbump");
        levelStacks = true;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {

        int enchantLvl = killEvent.getKillerEnchantLevel(this);
        if(enchantLvl == 0) return;

        killEvent.goldReward += getGoldIncrease(enchantLvl);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Earn &6+" + getGoldIncrease(enchantLvl) + "g &7per kill"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that increases " +
                "the gold you get on kill";
    }

    public int getGoldIncrease(int enchantLvl) {

        return enchantLvl * 4;
    }
}
