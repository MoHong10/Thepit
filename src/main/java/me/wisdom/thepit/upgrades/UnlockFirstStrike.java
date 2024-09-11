package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.objects.UnlockableRenownUpgrade;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UnlockFirstStrike extends UnlockableRenownUpgrade {
    public static UnlockFirstStrike INSTANCE;

    public UnlockFirstStrike() {
        super("Perk Unlock: First Strike", "FIRST_STRIKE", 10);
        INSTANCE = this;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.COOKED_CHICKEN)
                .getItemStack();
    }

    @Override
    public String getEffect() {
        return "&7对玩家的第一次攻击造成 &c+30% 伤害";
    }

    @Override
    public int getUnlockCost() {
        return 25;
    }

    @Override
    public String getSummary() {
        return "&e首次打击&7 是一个在 &e声望商店&7 中解锁的特权，它增加了你的 &c伤害&7，并在对机器人和玩家的第一次攻击中提供 &e速度&7";
    }
}
