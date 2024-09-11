package me.wisdom.thepit.perks;

import me.wisdom.thepit.controllers.objects.PitPerk;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NoPerk extends PitPerk {
    public static NoPerk INSTANCE;

    public NoPerk() {
        super("No Perk", "none");
        INSTANCE = this;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.DIAMOND_BLOCK)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, Player player) {
        loreBuilder.addLongLine(
                "&7你够硬核到不需要这个槽位的任何技能吗？?"
        );
    }

    @Override
    public String getSummary() {
        return null;
    }
}
