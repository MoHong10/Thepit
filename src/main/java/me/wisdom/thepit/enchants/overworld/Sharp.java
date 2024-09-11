package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Sharp extends PitEnchant {

    public Sharp() {
        super("Sharp", false, ApplyType.MELEE,
                "sharp", "s");
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
                "&7Deal &c+" + getDamageIncrease(enchantLvl) + "% &7melee damage"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that increases the " +
                "damage that you deal";
    }

    public int getDamageIncrease(int enchantLvl) {
        return (int) (Math.pow(enchantLvl, 1.2) * 3 + 1);
    }
}