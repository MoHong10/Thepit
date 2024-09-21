package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.HealEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;

public class Lifesteal extends PitEnchant {

    public Lifesteal() {
        super("Lifesteal", false, ApplyType.MELEE,
                "ls", "lifesteal", "life");
        isUncommonEnchant = true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isAttackerPlayer()) return;
        if(!canApply(attackEvent)) return;
        PitPlayer pitAttacker = attackEvent.getAttackerPitPlayer();

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;
        if(attackEvent.isFakeHit()) return;

        double damage = attackEvent.getFinalPitDamageIncrease();
        double healing = damage * (getHealing(enchantLvl) / 100D);
        HealEvent healEvent = pitAttacker.heal(healing);

        pitAttacker.stats.lifesteal += healEvent.getEffectiveHeal();
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Heal for &c+" + Misc.roundString(getHealing(enchantLvl)) + "% &7of damage dealt"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that heals you " +
                "for a percentage of the damage that you deal. Despite its low value, this enchant is one of the " +
                "most important when fighting other players";
    }

    public double getHealing(int enchantLvl) {
        return enchantLvl * 3 - 2;
    }
}
