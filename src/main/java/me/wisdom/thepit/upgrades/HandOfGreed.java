package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.TieredRenownUpgrade;
import me.wisdom.thepit.megastreaks.Prosperity;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class HandOfGreed extends TieredRenownUpgrade {
    public static HandOfGreed INSTANCE;

    public HandOfGreed() {
        super("Hand of Greed", "HAND_OF_GREED", 35);
        INSTANCE = this;
    }

    public static int getGoldIncrease(Player player) {
        return getGoldIncrease(UpgradeManager.getTier(player, INSTANCE));
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.BOWL)
                .getItemStack();
    }

    @Override
    public String getEffectPerTier() {
        return "&7在 " + Prosperity.INSTANCE.getCapsDisplayName() + " 上从击杀中获得 &6正好 +50 黄金 &7（忽略增益和上限）";
    }

    @Override
    public String getCurrentEffect(int tier) {
        return "&6正好 +" + getGoldIncrease(tier) + " 黄金";
    }

    @Override
    public String getSummary() {
        return "&e这个途径&7 是一个 &e声望&7 升级，降低 &cMegastreaks&7、&aKillstreakers&7 和 &6交易&7 的等级要求";
    }

    @Override
    public List<Integer> getTierCosts() {
        return Arrays.asList(50, 60, 65, 70, 75, 80, 85, 90, 95, 100);
    }

    public static int getGoldIncrease(int tier) {
        return tier * 50;
    }
}
