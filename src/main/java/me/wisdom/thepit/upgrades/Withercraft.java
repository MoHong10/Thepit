package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.objects.UnlockableRenownUpgrade;
import me.wisdom.thepit.inventories.WithercraftPanel;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Withercraft extends UnlockableRenownUpgrade {
    public static Withercraft INSTANCE;

    public Withercraft() {
        super("Withercraft", "WITHERCRAFT", 18, WithercraftPanel.class);
        INSTANCE = this;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.COAL)
                .getItemStack();
    }

    @Override
    public String getEffect() {
        return "&7可以右键点击一个 &5邪恶碎片&7，将其牺牲来修复一个 &3珠宝&7 物品上的生命";
    }

    @Override
    public String getSummary() {
        return "&e凋零工艺&7 是一个 &e声望&7 升级，让你使用 &5邪恶碎片&7 来修复 &3珠宝&7 物品上的生命";
    }

    @Override
    public int getUnlockCost() {
        return 50;
    }
}
