package me.wisdom.thepit.boosters;

import me.wisdom.thepit.controllers.objects.Booster;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PvPBooster extends Booster {
    public static PvPBooster INSTANCE;

    public PvPBooster() {
        super("PvP 增益器", "pvp", 13, ChatColor.RED);
        INSTANCE = this;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.GOLD_SWORD)
                .setLore(new PitLoreBuilder(
                        "&7所有玩家可以使用物品而不会失去生命。同时，死亡时不会失去 &f灵魂"
                )).getItemStack();
    }
}
