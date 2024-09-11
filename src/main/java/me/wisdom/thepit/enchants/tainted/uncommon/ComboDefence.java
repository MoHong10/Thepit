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

import java.text.DecimalFormat;
import java.util.List;

public class ComboDefence extends PitEnchant {
    public static ComboDefence INSTANCE;

    public ComboDefence() {
        super("Combo: Defence", false, ApplyType.SCYTHES,
                "combodefence", "defence", "cdefence", "combodefense", "defense", "cdefense");
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

        Misc.applyPotionEffect(attackEvent.getAttacker(), PotionEffectType.DAMAGE_RESISTANCE, getTicks(enchantLvl),
                getAmplifier(enchantLvl), true, false);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        DecimalFormat decimalFormat = new DecimalFormat("0.#");
        double seconds = getTicks(enchantLvl) / 20.0;
        return new PitLoreBuilder(
                "&7Every &e" + Misc.ordinalWords(getStrikes(enchantLvl)) + " &7strike gain &9Resistance[]" +
                        AUtil.toRoman(getAmplifier(enchantLvl) + 1) + " &7(" + decimalFormat.format(seconds) + "s)"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "gives you &9Resistance &7every few strikes";
    }

    public int getTicks(int enchantLvl) {
        if(enchantLvl == 1) return 40;
        return enchantLvl * 20;
    }

    public int getAmplifier(int enchantLvl) {
        return Misc.linearEnchant(enchantLvl, 0.5, 0);
    }

    public int getStrikes(int enchantLvl) {
        return Math.max(Misc.linearEnchant(enchantLvl, -0.5, 7.0), 1);
    }
}
