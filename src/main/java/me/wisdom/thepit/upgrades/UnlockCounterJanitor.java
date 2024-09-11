package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.objects.UnlockableRenownUpgrade;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UnlockCounterJanitor extends UnlockableRenownUpgrade {
    public static UnlockCounterJanitor INSTANCE;

    public UnlockCounterJanitor() {
        super("Perk Unlock: Counter-Janitor", "COUNTER_JANITOR", 14);
        INSTANCE = this;
    }

    @Override
    public String getSummary() {
        return "&e反清理员&7 是一个在 &e声望商店&7 中解锁的特权，它在击杀玩家时 &c显著恢复生命&7。该特权与 &c吸血鬼&7 不兼容";
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.SPONGE)
                .getItemStack();
    }

    @Override
    public String getEffect() {
        return "&7在击杀玩家时立即恢复一半的 &c血量";
    }

    @Override
    public int getUnlockCost() {
        return 20;
    }
}
