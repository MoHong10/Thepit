package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.TieredRenownUpgrade;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class VentureCapitalist extends TieredRenownUpgrade {
    public static VentureCapitalist INSTANCE;

    public VentureCapitalist() {
        super("Venture Capitalist", "UBER_INCREASE", 25);
        INSTANCE = this;
    }

    public static int getUberIncrease(Player player) {
        return UpgradeManager.getTier(player, VentureCapitalist.INSTANCE);
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.WATCH)
                .getItemStack();
    }

    @Override
    public String getCurrentEffect(int tier) {
        return "&d+" + tier + " 个 Uberstreak" + Misc.s(tier);
    }

    @Override
    public String getEffectPerTier() {
        return "&7每日 &dUberstreak &7限制增加 &d1";
    }

    @Override
    public String getSummary() {
        return "&d风险投资家&7 是一个 &e声望&7 升级，增加你每日的 &dUberstreak&7 限制";
    }

    @Override
    public List<Integer> getTierCosts() {
        return Arrays.asList(25, 50, 75, 100, 125);
    }
}
