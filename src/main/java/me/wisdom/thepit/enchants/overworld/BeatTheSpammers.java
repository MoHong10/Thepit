package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import java.util.List;

public class BeatTheSpammers extends PitEnchant {

    public BeatTheSpammers() {
        super("Beat the Spammers", false, ApplyType.SWORDS,
                "bts", "beat", "beat-the-spammers", "beatthespammers");
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        if(!attackEvent.getDefender().getEquipment().getItemInHand().getType().equals(Material.BOW)) return;
        attackEvent.increasePercent += getDamage(enchantLvl);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Deal &c+" + getDamage(enchantLvl) + "% &7damage vs. players holding a bow"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that increases " +
                "your damage vs players holding bows";
    }

    public int getDamage(int enchantLvl) {

        return enchantLvl * 25;
    }
}
