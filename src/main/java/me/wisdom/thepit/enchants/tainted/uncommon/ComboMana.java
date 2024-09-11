package me.wisdom.thepit.enchants.tainted.uncommon;

import me.wisdom.thepit.controllers.HitCounter;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enchants.overworld.Regularity;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;

import java.util.List;

public class ComboMana extends PitEnchant {
    public static ComboMana INSTANCE;

    public ComboMana() {
        super("Combo: Mana", false, ApplyType.SCYTHES,
                "combomana", "cmana");
        isUncommonEnchant = true;
        isTainted = true;
        INSTANCE = this;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isAttackerPlayer()) return;
        if(!canApply(attackEvent)) return;
        if(attackEvent.getAttacker() != attackEvent.getRealDamager() || attackEvent.getWrapperEvent().hasAttackInfo()) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        int regLvl = attackEvent.getAttackerEnchantLevel(Regularity.INSTANCE);
        if(Regularity.isRegHit(attackEvent.getDefender()) && Regularity.skipIncrement(regLvl)) return;

        PitPlayer pitPlayer = attackEvent.getAttackerPitPlayer();
        HitCounter.incrementCounter(pitPlayer.player, this);
        if(!HitCounter.hasReachedThreshold(pitPlayer.player, this, getStrikes(enchantLvl))) return;

        pitPlayer.giveMana(getMana(enchantLvl));
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Every &e" + Misc.ordinalWords(getStrikes(enchantLvl)) + " &7melee strike gain &b+" + getMana(enchantLvl) +
                        "[]mana"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "gives you &bmana &7every few melee strikes";
    }

    public int getMana(int enchantLvl) {
        return enchantLvl * 2 + 2;
    }

    public int getStrikes(int enchantLvl) {
        return Math.max(Misc.linearEnchant(enchantLvl, -0.5, 6), 1);
    }
}
