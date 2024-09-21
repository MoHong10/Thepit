package me.wisdom.thepit.enchants.tainted.uncommon;

import me.wisdom.thepit.controllers.HitCounter;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enchants.overworld.Regularity;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class ComboSlow extends PitEnchant {
    public static ComboSlow INSTANCE;

    public ComboSlow() {
        super("Combo: Slow", false, ApplyType.SCYTHES,
                "comboslow", "slow", "cslow");
        isUncommonEnchant = true;
        isTainted = true;
        INSTANCE = this;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isAttackerPlayer()) return;
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;
        if(attackEvent.isFakeHit()) return;

        int regLvl = attackEvent.getAttackerEnchantLevel(Regularity.INSTANCE);
        if(Regularity.isRegHit(attackEvent.getDefender()) && Regularity.skipIncrement(regLvl)) return;

        PitPlayer pitPlayer = attackEvent.getAttackerPitPlayer();
        HitCounter.incrementCounter(pitPlayer.player, this);
        if(!HitCounter.hasReachedThreshold(pitPlayer.player, this, getStrikes(enchantLvl))) return;

        Misc.applyPotionEffect(attackEvent.getDefender(), PotionEffectType.SLOW, getSeconds(enchantLvl) * 20,
                getAmplifier(enchantLvl), true, false);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7When you hit your opponent &e" + getStrikes(enchantLvl) + "[]times&7, apply &9Slowness " +
                        AUtil.toRoman(getAmplifier(enchantLvl) + 1) + " &7(" + getSeconds(enchantLvl) + "s)"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "effects your opponent with &9Slowness &7every few strikes";
    }

    public int getSeconds(int enchantLvl) {
        return Math.min(enchantLvl + 1, 4);
    }

    public int getAmplifier(int enchantLvl) {
        return Misc.linearEnchant(enchantLvl, 0.5, 0);
    }

    public int getStrikes(int enchantLvl) {
        return Math.max(Misc.linearEnchant(enchantLvl, -0.5, 6.5), 5);
    }
}
