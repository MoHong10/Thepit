package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.TieredRenownUpgrade;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.items.misc.VeryYummyBread;
import me.wisdom.thepit.items.misc.YummyBread;
import me.wisdom.thepit.misc.PlayerItemLocation;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BreadDealer extends TieredRenownUpgrade {
    public static BreadDealer INSTANCE;

    public BreadDealer() {
        super("Bread Dealer", "BREADDEALER", 30);
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!UpgradeManager.hasUpgrade(killEvent.getDeadPlayer(), this)) return;
        double percentSaved = UpgradeManager.getTier(killEvent.getDeadPlayer(), this) * getPercentPerTier();
        for(Map.Entry<PlayerItemLocation, KillEvent.ItemInfo> entry : new ArrayList<>(killEvent.getVulnerableItems().entrySet())) {
            PlayerItemLocation itemLocation = entry.getKey();
            KillEvent.ItemInfo itemInfo = entry.getValue();
            if(!(itemInfo.pitItem instanceof YummyBread) && !(itemInfo.pitItem instanceof VeryYummyBread)) continue;
            killEvent.removeVulnerableItem(itemLocation);

            ItemStack modifiedStack = itemInfo.itemStack.clone();
            int newAmount = (int) Math.floor(modifiedStack.getAmount() * percentSaved / 100.0);
            modifiedStack.setAmount(newAmount);
            killEvent.getDeadInventoryWrapper().putItem(itemLocation, modifiedStack);
        }
        killEvent.getDeadPlayer().updateInventory();
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.BREAD)
                .getItemStack();
    }

    @Override
    public String getCurrentEffect(int tier) {
        return "&7保留 &e" + (tier * getPercentPerTier()) + "% &7面包";
    }

    @Override
    public String getEffectPerTier() {
        return "&7在死亡时，保留 &e+" + getPercentPerTier() + "% &7面包";
    }

    @Override
    public String getSummary() {
        return "&e面包商人&7 是一个 &e声望&7 升级,它允许你在死亡时保留部分背包中的面包";
    }

    @Override
    public List<Integer> getTierCosts() {
        return Arrays.asList(10, 20, 30, 40, 50);
    }

    public static int getPercentPerTier() {
        return 10;
    }
}
