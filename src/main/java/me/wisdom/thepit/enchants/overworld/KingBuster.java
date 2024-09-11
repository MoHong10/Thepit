package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;

import java.util.List;

public class KingBuster extends PitEnchant {

    public KingBuster() {
        super("King Buster", false, ApplyType.MELEE,
                "kb", "kingbuster", "kbuster", "king-buster");
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        if(attackEvent.getDefender().getHealth() / attackEvent.getDefender().getMaxHealth() < 0.5) return;
        attackEvent.increasePercent += getDamage(enchantLvl);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Deal &c+" + getDamage(enchantLvl) + "% &7damage vs. players above 50% HP"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that does " +
                "more damage when your opponent is above half health";
    }

    public int getDamage(int enchantLvl) {
        return enchantLvl * 7 + 5;
    }
}
