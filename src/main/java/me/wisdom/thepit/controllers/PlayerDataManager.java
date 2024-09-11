package me.wisdom.thepit.controllers;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.PitQuitEvent;
import me.wisdom.thepit.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDataManager implements Listener {

    static {
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if(count++ % (60 * 5) == 0) {
                    for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        PitPlayer pitPlayer = PitPlayer.getPitPlayer(onlinePlayer);
                        pitPlayer.save(true, false);
                    }
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 20 * 15L, 20);
    }

    @EventHandler
    public void onQuit(PitQuitEvent event) {
        Player player = event.getPlayer();
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                StorageManager.quitInitiate(player);
                pitPlayer.save(true, true);
            }
        }.runTaskLater(Thepit.INSTANCE, 1L);


        new BukkitRunnable() {
            @Override
            public void run() {
                StorageManager.quitCleanup(player);
                removePitPlayer(pitPlayer);
            }
        }.runTaskLater(Thepit.INSTANCE, 30L);
    }

    public static void removePitPlayer(PitPlayer pitPlayer) {
        PitPlayer.pitPlayers.remove(pitPlayer);
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if(event.getPlugin() != Thepit.INSTANCE) return;

        for(Player player : Bukkit.getOnlinePlayers()) {
            PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
            pitPlayer.save(true, true);
        }
    }
}
