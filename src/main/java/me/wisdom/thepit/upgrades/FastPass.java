package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.objects.UnlockableRenownUpgrade;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FastPass extends UnlockableRenownUpgrade {
    public static FastPass INSTANCE;

    public FastPass() {
        super("Fast Pass", "FAST_PASS", 38);
        INSTANCE = this;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.ACTIVATOR_RAIL)
                .getItemStack();
    }

    @Override
    public String getEffect() {
        return "&7在你 &e声望&7 从等级 50 开始后";
    }

    @Override
    public String getSummary() {
        return "&e快速通行证&7 是一个 &e声望&7 升级，增加你完成 &e声望&7 后的起始等级";
    }

    @Override
    public int getUnlockCost() {
        return 100;
    }
}
