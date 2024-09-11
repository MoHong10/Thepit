package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.objects.UnlockableRenownUpgrade;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UnlockStreaker extends UnlockableRenownUpgrade {
    public static UnlockStreaker INSTANCE;

    public UnlockStreaker() {
        super("Perk Unlock: Streaker", "STREAKER", 7);
        INSTANCE = this;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.WHEAT)
                .getItemStack();
    }

    @Override
    public String getEffect() {
        return "&7在达到你的 &cMegastreak&7 时，击杀速度越快，获得的 &bXP&7 越多。被动增加 &b+80 最大 XP";
    }

    @Override
    public String getSummary() {
        return "&e连击者&7 是一个在 &e声望商店&7 中解锁的特权，根据你激活 &cMegastreak&7 的速度，" +
                "提供更高的 &bXP 上限&7 和更多的 &bXP";
    }

    @Override
    public int getUnlockCost() {
        return 30;
    }
}
