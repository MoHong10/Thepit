package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;

public class TrueShot extends PitEnchant {

    public TrueShot() {
        super("True Shot", true, ApplyType.BOWS,
                "trueshot", "true");
    }

    @Override
    public boolean isEnabled() {
        return Thepit.status.isOverworld();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        double damage = attackEvent.getFinalPitDamage();
        attackEvent.trueDamage += damage * (getPercent(enchantLvl) / 100.0);
        attackEvent.multipliers.add(Misc.getReductionMultiplier(getPercent(enchantLvl)));
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        if(enchantLvl == 1) {
            return new PitLoreBuilder(
                    "&7Deal &c" + getPercent(enchantLvl) + "% &7of arrow damage as true damage (ignores armor)"
            ).getLore();
        }
        return new PitLoreBuilder(
                "&7Deal &c" + getPercent(enchantLvl) + "% &7+ &c" + Misc.getHearts(getBase(enchantLvl)) +
                        " &7of arrow damage as true damage (ignores armor)"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that converts a " +
                "portion of the damage that you arrow does into true damage";
    }

    public int getPercent(int enchantLvl) {
        return Math.min(enchantLvl * 10, 100) + 20;
    }

    public double getBase(int enchantLvl) {
        return enchantLvl * 0.5 - 0.5;
    }
}
