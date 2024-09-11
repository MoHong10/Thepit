package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;

import java.text.DecimalFormat;
import java.util.List;

public class FractionalReserve extends PitEnchant {

    public FractionalReserve() {
        super("Fractional Reserve", false, ApplyType.PANTS,
                "fractionalreserve", "frac", "frac-reserve", "fractional-reserve", "fracreserve");
        isUncommonEnchant = true;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isDefenderPlayer()) return;
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
        if(enchantLvl == 0) return;

        double reduction = Math.max((int) Math.log10(attackEvent.getDefenderPitPlayer().gold) + 1, 0);
        attackEvent.multipliers.add(Misc.getReductionMultiplier(reduction * getReduction(enchantLvl)));
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        DecimalFormat decimalFormat = new DecimalFormat("0.#");
        return new PitLoreBuilder(
                "&7Receive &9-" + decimalFormat.format(getReduction(enchantLvl)) +
                        "% &7damage per &6digit &7in your gold"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that decreases " +
                "damage based on the amount of gold you currently have";
    }

    public static double getReduction(int enchantLvl) {
        if(enchantLvl == 1) return 2;
        return enchantLvl * 1.5 + 0.5;
    }
}
