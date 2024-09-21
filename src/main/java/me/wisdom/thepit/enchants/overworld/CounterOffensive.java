package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.HitCounter;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class CounterOffensive extends PitEnchant {

    public CounterOffensive() {
        super("Counter-Offensive", false, ApplyType.PANTS,
                "counteroffensive", "counter-offensive", "co", "offensive");
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isDefenderPlayer()) return;
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
        if(enchantLvl == 0) return;

        HitCounter.incrementCounter(attackEvent.getDefenderPlayer(), this);
        if(!HitCounter.hasReachedThreshold(attackEvent.getDefenderPlayer(), this, getStrikes(enchantLvl))) return;
        Misc.applyPotionEffect(attackEvent.getDefender(), PotionEffectType.SPEED, getSeconds(enchantLvl) * 20,
                getAmplifier(enchantLvl), false, false);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Gain &eSpeed " + AUtil.toRoman(getAmplifier(enchantLvl) + 1) + " &7(" +
                        getSeconds(enchantLvl) + "s) when hit &e" + getStrikes(enchantLvl) + " times &7by a player"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that gives you " +
                "&eSpeed &7when you are attacked by other players";
    }

    public int getStrikes(int enchantLvl) {
        return Math.max(6 - enchantLvl, 1);
    }

    public int getAmplifier(int enchantLvl) {
        return Misc.linearEnchant(enchantLvl, 0.5, 0);
    }

    public int getSeconds(int enchantLvl) {
        return Misc.linearEnchant(enchantLvl, 0.5, 2);
    }
}
