package me.wisdom.thepit.controllers;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.KitItem;
import me.wisdom.thepit.events.PitJoinEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FirstJoinManager implements Listener {

    @EventHandler
    public void onJoin(PitJoinEvent event) {
        Player player = event.getPlayer();
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(pitPlayer.prestige > 0 || hasItems(player)) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));

                player.getInventory().setLeggings(KitManager.getItem(KitItem.SWEATY_GHEART));

                player.getInventory().setItem(0, KitManager.getItem(KitItem.EXE_SWEATY));
                player.getInventory().setItem(1, KitManager.getItem(KitItem.MLB_DRAIN));
                player.updateInventory();
            }
        }.runTaskLater(Thepit.INSTANCE, 5);
    }

    public boolean hasItems(Player player) {
        boolean hasItems = false;
        for(ItemStack item : player.getInventory().getContents()) {
            if(item != null) {
                hasItems = true;
                break;
            }
        }
        return hasItems;
    }
}
