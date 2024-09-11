package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.TieredRenownUpgrade;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class RenownGoldBoost extends TieredRenownUpgrade {
    public static RenownGoldBoost INSTANCE;

    public RenownGoldBoost() {
        super("Renown Gold Boost", "GOLD_BOOST", 3);
        INSTANCE = this;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.GOLD_NUGGET)
                .getItemStack();
    }

    @Override
    public String getCurrentEffect(int tier) {
        return "&6+" + decimalFormat.format(tier * 2.5) + "% 黄金";
    }

    @Override
    public String getEffectPerTier() {
        return "&7从击杀中获得 &6+2.5% 黄金";
    }

    @Override
    public String getSummary() {
        return "&e声望 &6黄金提升&7 是一个声望升级，每提升一层在击杀玩家/机器人时给你额外 &62.5% 黄金";
    }

    @Override
    public List<Integer> getTierCosts() {
        return Arrays.asList(10, 12, 14, 16, 18, 20, 22, 24, 26, 28);
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!killEvent.isKillerPlayer()) return;
        if(!UpgradeManager.hasUpgrade(killEvent.getKillerPlayer(), this)) return;

        int tier = UpgradeManager.getTier(killEvent.getKillerPlayer(), this);
        if(tier == 0) return;

        double percent = 2.5 * tier;

        killEvent.goldMultipliers.add((percent / 100D) + 1);
    }
}
