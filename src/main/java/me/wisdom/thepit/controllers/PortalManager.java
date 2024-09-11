package me.wisdom.thepit.controllers;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.brewing.PotionManager;
import me.wisdom.thepit.controllers.objects.Hopper;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PortalManager implements Listener {

    @EventHandler
    public void onPortal(EntityPortalEvent event) {
        if(event.getEntity() instanceof Player) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        if(event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return;
        event.setCancelled(true);

        Player player = event.getPlayer();
        attemptServerSwitch(player);
    }

    public static void attemptServerSwitch(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if (pitPlayer.prestige < 5 && !player.isOp()) {
            player.setVelocity(new Vector(3, 1, 0));
            AOutput.error(player, "&5&lDARKZONE &7你必须至少达到声望 &eV &7才能进入！");
            Sounds.NO.play(player);
            return;
        }

        if (pitPlayer.isOnMega() && !player.isOp()) {
            player.setVelocity(new Vector(3, 1, 0));
            AOutput.error(player, "&5&lDARKZONE &7你不能在超级连击期间进入黑暗区域！");
            Sounds.NO.play(player);
            return;
        }

        if (CombatManager.isInCombat(player) && !player.isOp()) {
            player.setVelocity(new Vector(3, 1, 0));
            AOutput.error(player, "&5&lDARKZONE &7你不能在战斗中进入黑暗区域！");
            Sounds.NO.play(player);
            return;
        }

        if(HopperManager.isHopper(player)) return;

        boolean hasHopper = false;
        for(Hopper hopper : HopperManager.hopperList) {
            if(hopper.target != player) continue;
            hasHopper = true;
            break;
        }
        if(hasHopper && !player.isOp()) {
            player.setVelocity(new Vector(3, 1, 0));
            AOutput.error(player, "&c&lYOU WISH!&7 Kill that hopper first :P");
            Sounds.NO.play(player);
            return;
        }

        if(Thepit.getStatus() == Thepit.ServerStatus.STANDALONE) {
            Location playerLoc = player.getLocation();
            PotionManager.bossBars.remove(player);

            Location teleportLoc;
            if(player.getWorld() != Bukkit.getWorld("darkzone")) {
                teleportLoc = playerLoc.clone().add(235, 40, -97);
                teleportLoc.setWorld(Bukkit.getWorld("darkzone"));
                teleportLoc.setX(173);
                teleportLoc.setY(92);
                teleportLoc.setZ(-94);
            } else {
                World destination = MapManager.currentMap.world;
                teleportLoc = MapManager.currentMap.getStandAlonePortalRespawn();
                teleportLoc.setWorld(destination);
                teleportLoc.setY(72);
            }

            if(teleportLoc.getYaw() > 0 || teleportLoc.getYaw() < -180) teleportLoc.setYaw(-teleportLoc.getYaw());
            teleportLoc.add(3, 0, 0);

            player.teleport(teleportLoc);
            player.setVelocity(new Vector(1.5, 1, 0).multiply(0.25));

            PitPlayer.getPitPlayer(player).updateMaxHealth();
            player.setHealth(player.getMaxHealth());

            if(player.getWorld() == Bukkit.getWorld("darkzone")) {
                Misc.sendTitle(player, "&d&k||&5&l黑暗区&d&k||", 40);
                Misc.sendSubTitle(player, "", 40);
                AOutput.send(player, "&7你已经被传送到 &d&k||&5&l黑暗区&d&k||&7。");
            } else {
                Misc.sendTitle(player, "&a&l主世界", 40);
                Misc.sendSubTitle(player, "", 40);
                AOutput.send(player, "&7你已经被传送到 &a&l主世界&7。");

                MusicManager.stopPlaying(player);
            }
            return;
        }

        LobbySwitchManager.setSwitchingPlayer(player);

        if(Thepit.getStatus().isDarkzone()) {
            ProxyMessaging.switchPlayer(player, 0);
        } else {
            ProxyMessaging.darkzoneSwitchPlayer(player, 0);
        }
    }

    @EventHandler
    public static void onTp(PlayerTeleportEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!MapManager.inDarkzone(event.getPlayer())) MusicManager.stopPlaying(event.getPlayer());
            }
        }.runTaskLater(Thepit.INSTANCE, 10);
    }
}
