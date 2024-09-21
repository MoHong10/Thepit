package me.wisdom.thepit.enchants.tainted.uncommon;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Frail extends PitEnchant {
    public static Frail INSTANCE;

    public Frail() {
        super("Frail", false, ApplyType.CHESTPLATES,
                "frail");
        isUncommonEnchant = true;
        isTainted = true;
        INSTANCE = this;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
        if(enchantLvl == 0) return;

        attackEvent.increasePercent += getIncrease(enchantLvl);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7This item is &4cursed&7. You take &c+" + getIncrease(enchantLvl) + "%[]&7damage when attacked"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant (curse) that " +
                "makes you take more damage";
    }

    public static int getIncrease(int enchantLvl) {
        return (int) Math.pow(10, enchantLvl - 1);
    }
}
