package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Berserker extends PitEnchant {

    public Berserker() {
        super("Berserker", false, ApplyType.MELEE,
                "berserker", "bez", "bes");
        isUncommonEnchant = true;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        if(!Misc.isCritical(attackEvent.getAttacker()) || Math.random() > getChance(enchantLvl) / 100.0) return;
        attackEvent.multipliers.add(1.5);
        Sounds.BERSERKER.play(attackEvent.getAttackerPlayer());
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7You can now critical hit on the ground. &a" + getChance(enchantLvl) +
                        "% chance &7to crit for &c50% extra &7damage"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that increases " +
                "the chance to land critical strikes while on the ground";
    }

    public int getChance(int enchantLvl) {
        return enchantLvl * 17 + 24;
    }
}