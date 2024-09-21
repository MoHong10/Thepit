package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DiamondStomp extends PitEnchant {

    public DiamondStomp() {
        super("Diamond Stomp", false, ApplyType.MELEE,
                "diamondstomp", "stomp", "ds", "dstomp", "diamond-stomp");
        isUncommonEnchant = true;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        for(ItemStack armorContent : attackEvent.getDefender().getEquipment().getArmorContents()) {
            if(!(armorContent.getType() == Material.DIAMOND_HELMET || armorContent.getType() == Material.DIAMOND_CHESTPLATE
                    || armorContent.getType() == Material.DIAMOND_LEGGINGS || armorContent.getType() == Material.DIAMOND_BOOTS))
                return;
        }

        attackEvent.increasePercent += getDamage(enchantLvl);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Deal &c+" + getDamage(enchantLvl) + "% &7damage vs. players wearing diamond armor"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that does more " +
                "damage against players wearing any diamond armor " +
                "(basically everyone)";
    }

    public int getDamage(int enchantLvl) {
        return enchantLvl * 8 + 6;
    }
}
