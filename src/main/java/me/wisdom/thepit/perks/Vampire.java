package me.wisdom.thepit.perks;

import me.wisdom.thepit.controllers.objects.PitPerk;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.HealEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class Vampire extends PitPerk {
    public static Vampire INSTANCE;

    public static double initialHealing = 1;

    public Vampire() {
        super("Vampire", "vampire");
        INSTANCE = this;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if (!hasPerk(attackEvent.getAttacker())) return;

        double healing = initialHealing;
        if (attackEvent.getArrow() != null && attackEvent.getArrow().isCritical()) healing *= 2;
        HealEvent healEvent = attackEvent.getAttackerPitPlayer().heal(healing);
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.FERMENTED_SPIDER_EYE)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, Player player) {
        loreBuilder.addLongLine(
                "&7每次攻击恢复 &c" + Misc.getHearts(initialHealing) + " &7生命值。箭矢暴击时恢复双倍"
        );
    }

    @Override
    public String getSummary() {
        return "&aVampire &7是一个每次攻击时恢复生命值的 perk";
    }
}
