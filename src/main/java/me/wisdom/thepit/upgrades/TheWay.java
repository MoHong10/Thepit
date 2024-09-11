package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.TieredRenownUpgrade;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class TheWay extends TieredRenownUpgrade {
    public static TheWay INSTANCE;

    public TheWay() {
        super("The Way", "THE_WAY", 33);
        INSTANCE = this;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.ACACIA_DOOR_ITEM)
                .getItemStack();
    }

    @Override
    public String getEffectPerTier() {
        return "&7降低等级要求 &e[5]等级";
    }

    @Override
    public String getCurrentEffect(int tier) {
        return "&e-" + (tier * 5) + " 等级";
    }

    @Override
    public String getSummary() {
        return "&e这个途径&7 是一个 &e声望&7 升级，降低 &cMegastreaks&7、&aKillstreakers&7 和 &6交易&7 的等级要求";
    }

    @Override
    public List<Integer> getTierCosts() {
        return Arrays.asList(50, 100, 150, 200, 250, 300, 350, 400, 450, 500);
    }

    public int getLevelReduction(Player player) {
        return UpgradeManager.getTier(player, this) * 5;
    }
}
