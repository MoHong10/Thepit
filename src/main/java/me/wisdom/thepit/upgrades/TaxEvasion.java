package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.objects.TieredRenownUpgrade;
import me.wisdom.thepit.enchants.overworld.Billionaire;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class TaxEvasion extends TieredRenownUpgrade {
    public static TaxEvasion INSTANCE;

    public TaxEvasion() {
        super("Tax Evasion", "TAX_EVASION", 22);
        INSTANCE = this;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.IRON_FENCE)
                .getItemStack();
    }

    @Override
    public String getCurrentEffect(int tier) {
        return "&6-" + (tier * 5) + "% 黄金";
    }

    @Override
    public String getEffectPerTier() {
        return "&7使用 " + Billionaire.INSTANCE.getDisplayName(false, true) +
                " &7的攻击消耗 &6减少5%";
    }

    @Override
    public String getSummary() {
        return "&e税收逃避&7 是一个 &e声望&7 升级，使得附魔 &d稀有! &9亿万富翁&7 的使用成本减少 &6黄金";
    }

    @Override
    public List<Integer> getTierCosts() {
        return Arrays.asList(25, 50, 75);
    }
}
