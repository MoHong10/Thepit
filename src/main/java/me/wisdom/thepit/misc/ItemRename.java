package me.wisdom.thepit.misc;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.events.PitQuitEvent;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class ItemRename implements Listener {
    public static Map<Player, ItemStack> renamePlayers = new HashMap<>();

    public static void renameItem(Player player, ItemStack item) {

        renamePlayers.remove(player);
        renamePlayers.put(player, item);
        AOutput.send(player, "&a&l请输入您希望为所持物品设置的名称");
        AOutput.send(player, "&7&o（您可以使用 & 符号来包含颜色代码）");

        new BukkitRunnable() {
            @Override
            public void run() {
                renamePlayers.remove(player);
            }
        }.runTaskLater(Thepit.INSTANCE, 1200L);
    }

    @EventHandler
    public void onLeave(PitQuitEvent event) {
        renamePlayers.remove(event.getPlayer());
    }
}
