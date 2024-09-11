package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.HealEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;

public class Sufferance extends PitEnchant {

    public Sufferance() {
        super("Sufferance", false, ApplyType.PANTS,
                "sufferance", "suffer", "sufference");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isDefenderPlayer()) return;
        if(!canApply(attackEvent)) return;

        int defenderLvl = attackEvent.getDefenderEnchantLevel(this);
        if(defenderLvl == 0) return;

        PitPlayer pitDefender = attackEvent.getDefenderPitPlayer();
        pitDefender.heal((attackEvent.trueDamage) * getReductionPercent(defenderLvl) / 100D,
                HealEvent.HealType.ABSORPTION, 4);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Convert &e" + getReductionPercent(enchantLvl) + "% &7of true damage taken into &6absorption"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that converts a " +
                "portion of the true damage that you receive into absorption";
    }

    public static int getReductionPercent(int enchantLvl) {
        if(enchantLvl == 0) return 15;

        return 10 + (enchantLvl - 1) * 20;
    }
}
