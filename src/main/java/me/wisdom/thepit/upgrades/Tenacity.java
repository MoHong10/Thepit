package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.controllers.objects.TieredRenownUpgrade;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Tenacity extends TieredRenownUpgrade {
    public static Tenacity INSTANCE;

    public Tenacity() {
        super("Tenacity", "TENACITY", 1);
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!killEvent.isKillerPlayer() || !killEvent.isDeadPlayer()) return;
        if(!UpgradeManager.hasUpgrade(killEvent.getKillerPlayer(), this)) return;

        int tier = UpgradeManager.getTier(killEvent.getKillerPlayer(), this);
        if(tier == 0) return;

        PitPlayer pitKiller = killEvent.getKillerPitPlayer();
        pitKiller.heal(tier);
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.MAGMA_CREAM)
                .getItemStack();
    }

    @Override
    public String getCurrentEffect(int tier) {
        return "&7击杀时恢复 &c" + Misc.getHearts(tier) + " 心";
    }

    @Override
    public String getEffectPerTier() {
        return getCurrentEffect(1);
    }

    @Override
    public String getSummary() {
        return "坚韧是一个 &e声望&7 升级，当你击杀机器人或玩家时会 &c恢复生命&7，非常适合连续击杀";
    }

    @Override
    public List<Integer> getTierCosts() {
        return Arrays.asList(10, 50);
    }
}
