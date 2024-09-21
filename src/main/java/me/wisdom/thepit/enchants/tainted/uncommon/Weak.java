package me.wisdom.thepit.enchants.tainted.uncommon;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Weak extends PitEnchant {
    public static Weak INSTANCE;

    public Weak() {
        super("Weak", false, ApplyType.SCYTHES,
                "weak");
        isUncommonEnchant = true;
        isTainted = true;
        INSTANCE = this;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        attackEvent.multipliers.add(Misc.getReductionMultiplier(getDecrease(enchantLvl)));
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7This item is &4cursed&7. Your attacks deal &9-" + getDecrease(enchantLvl) + "% &7damage"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant (curse) that " +
                "makes you deal less damage";
    }

    public static int getDecrease(int enchantLvl) {
        return (int) Math.pow(10, enchantLvl - 1);
    }
}
