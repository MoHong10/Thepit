package me.wisdom.thepit.boosters;

import me.wisdom.thepit.controllers.objects.Booster;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ChaosBooster extends Booster {
    public static ChaosBooster INSTANCE;

    public ChaosBooster() {
        super("混沌增益器", "chaos", 15, ChatColor.GREEN);
        INSTANCE = this;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.TNT)
                .setLore(new PitLoreBuilder(
                        "&7在中间区域双倍增加机器人数量"
                )).getItemStack();
    }
}
