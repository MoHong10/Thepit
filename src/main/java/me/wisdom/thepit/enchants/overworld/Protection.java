package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Protection extends PitEnchant {

    public Protection() {
        super("Protection", false, ApplyType.PANTS,
                "prot", "protection", "p");
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
        if(enchantLvl == 0) return;

        attackEvent.multipliers.add(Misc.getReductionMultiplier(getDamageReduction(enchantLvl)));
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Receive &9-" + Misc.roundString(getDamageReduction(enchantLvl)) + "% &7damage"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that reduces " +
                "the damage that you take";
    }

    public double getDamageReduction(int enchantLvl) {
        if(enchantLvl == 1) return 12;
        return enchantLvl * 8;
    }
}
