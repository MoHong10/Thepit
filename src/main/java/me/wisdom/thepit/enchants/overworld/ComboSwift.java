package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.HitCounter;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class ComboSwift extends PitEnchant {

    public ComboSwift() {
        super("Combo: Swift", false, ApplyType.MELEE,
                "comboswift", "swift", "cs", "combo-swift");
        isUncommonEnchant = true;
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

        Misc.applyPotionEffect(attackEvent.getAttacker(), PotionEffectType.SPEED, getSeconds(enchantLvl) * 20,
                getAmplifier(enchantLvl), true, false);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Every &e" + Misc.ordinalWords(getStrikes(enchantLvl)) + " &7strike gain &eSpeed[]" +
                        AUtil.toRoman(getAmplifier(enchantLvl) + 1) + " &7(" + getSeconds(enchantLvl) + "s)"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that gives you " +
                "&eSpeed &7every few strikes";
    }

    public int getSeconds(int enchantLvl) {
        return enchantLvl + 2;
    }

    public int getAmplifier(int enchantLvl) {
        return Misc.linearEnchant(enchantLvl, 0.5, 0);
    }

    public int getStrikes(int enchantLvl) {
        return Math.max(Misc.linearEnchant(enchantLvl, -0.5, 4.5), 1);
    }
}
