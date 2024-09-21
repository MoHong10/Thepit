package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.objects.PitEnchant;

import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import java.util.List;

public class Singularity extends PitEnchant {
    public static Singularity INSTANCE;

    public Singularity() {
        super("Singularity", true, ApplyType.PANTS,
                "singularity", "sing");
        INSTANCE = this;
    }

    public static double getAdjustedFinalDamage(AttackEvent attackEvent) {
        double finalDamage = attackEvent.getWrapperEvent().getSpigotEvent().getFinalDamage();
        if(attackEvent.isDefenderRealPlayer() && attackEvent.getDefenderPitPlayer().isOnMega()) return finalDamage;

        int enchantLvl = attackEvent.getDefenderEnchantLevel(INSTANCE);
        if(enchantLvl == 0) return finalDamage;

        double maxDamage = getMaxDamage(enchantLvl);
        return Math.min(finalDamage, maxDamage);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Hits you receive deal at most &c" + Misc.getHearts(getMaxDamage(enchantLvl)) + " &7damage"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that makes it so " +
                "there is a maximum amount of damage that you can take on each hit (doesn't account for/affect " +
                "true damage)";
    }

    public static double getMaxDamage(int enchantLvl) {
        return Math.max(6.6 - enchantLvl * 1.0, 0);
    }
}
