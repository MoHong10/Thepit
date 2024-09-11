package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.objects.UnlockableRenownUpgrade;
import me.wisdom.thepit.inventories.HelmetryPanel;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Helmetry extends UnlockableRenownUpgrade {
    public static Helmetry INSTANCE;

    public Helmetry() {
        super("Helmetry", "HELMETRY", 15, HelmetryPanel.class);
        INSTANCE = this;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.GOLD_HELMET)
                .getItemStack();
    }

    @Override
    public String getEffect() {
        return "&7解锁制作 &6黄金头盔 &7的能力，每个需要 &e10 声望&7。 " +
                "可以通过将 &6黄金 &7放入其中来升级它们";
    }

    @Override
    public String getSummary() {
        return "&e头盔学&7 是一个 &e声望&7 升级，可以让你用 &e5 声望&7 购买一个 &6黄金头盔&7，" +
                "它根据头盔中包含的 &6黄金&7 数量提供各种增益";
    }

    @Override
    public int getUnlockCost() {
        return 25;
    }
}
