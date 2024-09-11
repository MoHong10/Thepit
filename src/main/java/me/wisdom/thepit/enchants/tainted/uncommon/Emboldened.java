package me.wisdom.thepit.enchants.tainted.uncommon;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Emboldened extends PitEnchant {
    public static Emboldened INSTANCE;

    public Emboldened() {
        super("Emboldened", false, ApplyType.SCYTHES,
                "emboldened");
        isUncommonEnchant = true;
        isTainted = true;
        INSTANCE = this;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        if(attackEvent.getAttacker().getHealth() <= attackEvent.getDefender().getHealth()) return;

        attackEvent.increasePercent += getDamageIncrease(enchantLvl);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Deal &c+" + getDamageIncrease(enchantLvl) + "% &7more damage when you have more " +
                        "health than your target"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "does more damage when you have more health than your opponent";
    }

    public int getDamageIncrease(int enchantLvl) {
        return enchantLvl * 8 + 4;
    }
}
