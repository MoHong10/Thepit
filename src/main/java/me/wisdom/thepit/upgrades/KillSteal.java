package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.objects.TieredRenownUpgrade;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class KillSteal extends TieredRenownUpgrade {
    public static KillSteal INSTANCE;

    public KillSteal() {
        super("Kill Steal", "KILL_STEAL", 27);
        INSTANCE = this;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.SHEARS)
                .getItemStack();
    }

    @Override
    public String getCurrentEffect(int tier) {
        return "&e+" + (tier * 10) + "% &7对 &a助攻";
    }

    @Override
    public String getEffectPerTier() {
        return "&7在你的 &a助攻&7 上获得 &e+10% &7。100% 的 &a助攻&7 被转换为 &c击杀";
    }

    @Override
    public String getSummary() {
        return "&e击杀抢夺&7 是一个 &e声望&7 升级，增加你所有助攻的百分比 " +
                "（并将 100% 的助攻转换为击杀）";
    }

    @Override
    public List<Integer> getTierCosts() {
        return Arrays.asList(20, 30, 40);
    }
}
