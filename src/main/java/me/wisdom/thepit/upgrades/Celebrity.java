package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.UnlockableRenownUpgrade;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class Celebrity extends UnlockableRenownUpgrade {
    public static Celebrity INSTANCE;

    public Celebrity() {
        super("Celebrity", "CELEBRITY", 40);
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!UpgradeManager.hasUpgrade(killEvent.getKillerPlayer(), this)) return;
        killEvent.goldMultipliers.add(2.0);
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.RAW_FISH, 1, 3)
                .getItemStack();
    }

    @Override
    public String getEffect() {
        return "&7从击杀中获得 &62 倍 &7的金币";
    }

    @Override
    public String getSummary() {
        return "&6Celebrity &7是一个 &e声望&7 升级，它使你在击杀所有机器人和玩家时获得的 &6金币&7 翻倍";
    }

    @Override
    public int getUnlockCost() {
        return 300;
    }
}
