package me.wisdom.thepit.controllers;

import com.xxmicloxx.NoteBlockAPI.songplayer.EntitySongPlayer;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.enchants.overworld.Stereo;
import me.wisdom.thepit.misc.Misc;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StereoManager implements Listener {

    public static Map<Player, EntitySongPlayer> playerMusic = new HashMap<>();
    public static List<Player> toggledPlayers = new ArrayList<>();

    public static boolean hasStereo(Player player) {
        if(!Misc.isAirOrNull(player.getInventory().getLeggings())) {
            if(EnchantManager.getEnchantsOnItem(player.getInventory().getLeggings()).containsKey(Stereo.INSTANCE))
                return true;
        }
        if(!Misc.isAirOrNull(player.getInventory().getChestplate())) {
            return EnchantManager.getEnchantsOnItem(player.getInventory().getChestplate()).containsKey(Stereo.INSTANCE);
        }
        return false;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                boolean stereoOnChest = false;
                if(!Misc.isAirOrNull(event.getPlayer().getInventory().getChestplate())) {
                    if(!EnchantManager.getEnchantsOnItem(event.getPlayer().getInventory().getChestplate()).containsKey(Stereo.INSTANCE))
                        stereoOnChest = true;
                }

                if(stereoOnChest && !MapManager.inDarkzone(event.getPlayer())) {
                    EntitySongPlayer esp = StereoManager.playerMusic.get(event.getPlayer());
                    if(StereoManager.playerMusic.containsKey(event.getPlayer())) esp.destroy();
                    StereoManager.playerMusic.remove(event.getPlayer());
                }
            }
        }.runTaskLater(Thepit.INSTANCE, 5L);
    }
}
