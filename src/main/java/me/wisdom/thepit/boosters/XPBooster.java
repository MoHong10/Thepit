package me.wisdom.thepit.boosters;

import me.wisdom.thepit.controllers.LevelManager;
import me.wisdom.thepit.controllers.objects.Booster;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class XPBooster extends Booster {
    public static XPBooster INSTANCE;

    public XPBooster() {
        super("XP 增益器", "xp", 9, ChatColor.AQUA);
        INSTANCE = this;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onKill(KillEvent killEvent) {
        if(!isActive()) return;
        killEvent.xpMultipliers.add(1 + (getXPIncrease() / 100.0));
        killEvent.maxXPMultipliers.add(1 + (getMaxXPIncrease() / 100.0));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKillMonitor(KillEvent killEvent) {
        if(!isActive() || activatorUUID == null || killEvent.getKiller() == null || killEvent.getKiller().getUniqueId().equals(activatorUUID)) return;
        queueShare(killEvent.getFinalXp());
    }

    @Override
    public void share(Player player, int amount) {
        LevelManager.addXP(player, (long) (amount * (getXPShare() / 100.0)));
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        DecimalFormat decimalFormat = new DecimalFormat("0.#");
        return new AItemStackBuilder(Material.INK_SACK, 1, 12)
                .setLore(new PitLoreBuilder(
                        "&7服务器上的所有玩家获得 &b+" + getXPIncrease() + "% XP &7和 &b+" + getMaxXPIncrease() + "% 最大 XP"
                ).addLongLine(
                        "&7如果你激活此增益器，你将获得由所有在线玩家获得 &b" + decimalFormat.format(getXPShare()) +
                                "% &的 &bxp"
                )).getItemStack();
    }

    public static int getXPIncrease() {
        return 50;
    }

    public static int getMaxXPIncrease() {
        return 35;
    }

    public static double getXPShare() {
        return 3;
    }
}
