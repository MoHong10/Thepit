package me.wisdom.thepit.boosters;

import me.wisdom.thepit.controllers.objects.Booster;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SoulBooster extends Booster {
    public static SoulBooster INSTANCE;

    public SoulBooster() {
        super("灵魂 增益器", "darkzone", 17, ChatColor.WHITE);
        INSTANCE = this;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.GHAST_TEAR)
                .setLore(new PitLoreBuilder(
                        "&7在&5黑暗区&7从怪物和首领那里获得&f+" + getSoulsIncrease() + "%&7的灵魂"
                )).getItemStack();
    }

    public static int getSoulsIncrease() {
        return 50;
    }
}
