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

public class GoldBooster extends Booster {
    public static GoldBooster INSTANCE;

    public GoldBooster() {
        super("金币增益器", "gold", 11, ChatColor.GOLD);
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!isActive()) return;
        killEvent.goldMultipliers.add(1 + (getGoldIncrease() / 100.0));
        killEvent.goldCap += getGoldCapIncrease();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKillMonitor(KillEvent killEvent) {
        if(!isActive() || activatorUUID == null || killEvent.getKiller() == null || killEvent.getKiller().getUniqueId().equals(activatorUUID)) return;
        queueShare(killEvent.getFinalGold());
    }

    @Override
    public void share(Player player, int amount) {
        LevelManager.addGold(player, (int) (amount * (getGoldShare() / 100.0)));
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        DecimalFormat decimalFormat = new DecimalFormat("0.#");
        return new AItemStackBuilder(Material.INK_SACK, 1, 14)
                .setLore(new PitLoreBuilder(
                        "&7服务器上的所有玩家获得 &6+" + getGoldIncrease() + "% 金币 &7和 &6+" + getGoldCapIncrease() + " 最大金币"
                ).addLongLine(
                        "&7如果你激活这个增益器，你将获得所有在线玩家所赚取的 &6" + decimalFormat.format(getGoldShare()) + "% &7金币"
                )).getItemStack();
    }

    public static int getGoldIncrease() {
        return 50;
    }

    public static int getGoldCapIncrease() {
        return 500;
    }

    public static double getGoldShare() {
        return 3;
    }
}
