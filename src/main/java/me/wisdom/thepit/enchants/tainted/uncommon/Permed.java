package me.wisdom.thepit.enchants.tainted.uncommon;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Permed extends PitEnchant {
    public static Permed INSTANCE;

    public Permed() {
        super("Permed", false, ApplyType.SCYTHES,
                "permed", "perm", "playerkiller");
        isUncommonEnchant = true;
        isTainted = true;
        INSTANCE = this;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        attackEvent.increasePercent += getDamageIncrease(enchantLvl);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Deal &c+" + getDamageIncrease(enchantLvl) + "% &7damage against other players"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "increases the damage you deal to other players";
    }

    public int getDamageIncrease(int enchantLvl) {
        return enchantLvl * 5 + 2;
    }
}
