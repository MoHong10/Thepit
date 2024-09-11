package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.UnlockableRenownUpgrade;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class XPComplex extends UnlockableRenownUpgrade {
    public static XPComplex INSTANCE;

    public XPComplex() {
        super("经验工业综合体", "XP_COMPLEX", 23);
        INSTANCE = this;
    }

    @Override
    public String getSummary() {
        return "&e经验工业综合体&7 是一个 &e声望&7 升级，增加你的最大 &bXP";
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.DIAMOND_BARDING)
                .getItemStack();
    }

    @Override
    public String getEffect() {
        return "&7获得 &b+150 最大 XP";
    }

    @Override
    public int getUnlockCost() {
        return 50;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!killEvent.isKillerPlayer()) return;
        if(!UpgradeManager.hasUpgrade(killEvent.getKillerPlayer(), this)) return;

        killEvent.xpCap += 150;
    }
}
