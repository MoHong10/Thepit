package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.TieredRenownUpgrade;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class RenownXPBoost extends TieredRenownUpgrade {
    public static RenownXPBoost INSTANCE;

    public RenownXPBoost() {
        super("Renown XP Boost", "XP_BOOST", 2);
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!killEvent.isKillerPlayer()) return;
        if(!UpgradeManager.hasUpgrade(killEvent.getKillerPlayer(), this)) return;

        int tier = UpgradeManager.getTier(killEvent.getKillerPlayer(), this);
        if(tier == 0) return;

        killEvent.xpReward += 5 * tier;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.EXP_BOTTLE)
                .getItemStack();
    }

    @Override
    public String getCurrentEffect(int tier) {
        return "&b+" + (tier * 5) + " XP";
    }

    @Override
    public String getEffectPerTier() {
        return "&7从击杀中获得 &b+5 XP";
    }

    @Override
    public String getSummary() {
        return "&e声望 &bXP 提升&7 是一个 &e声望&7 升级，每提升一层在击杀玩家/机器人时额外获得 &b+5 XP";
    }

    @Override
    public List<Integer> getTierCosts() {
        return Arrays.asList(10, 12, 14, 16, 18, 20, 22, 24, 26, 28);
    }
}
