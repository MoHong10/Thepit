package me.wisdom.thepit.controllers;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.events.PitQuitEvent;
import me.wisdom.thepit.events.PlayerSpawnCommandEvent;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CombatManager implements Listener {
    public static HashMap<UUID, Integer> taggedPlayers = new HashMap<>();

    public static int getCombatTicks() {
        return Thepit.status.isOverworld() ? 20 * 20 : 20 * 5;
    }

    public static boolean isInCombat(Player player) {
        return taggedPlayers.containsKey(player.getUniqueId());
    }

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<UUID> toRemove = new ArrayList<>();
                for(Map.Entry<UUID, Integer> entry : taggedPlayers.entrySet()) {
                    int time = entry.getValue();
                    time = time - 1;

                    if(time > 0) taggedPlayers.put(entry.getKey(), time);
                    else toRemove.add(entry.getKey());
                }

                for(UUID uuid : toRemove) {
                    taggedPlayers.remove(uuid);
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        if(player.getUniqueId().equals(uuid)) {
                            PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
                            pitPlayer.lastHitUUID = null;
                        }
                    }
                }

            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 1L);
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(attackEvent.hasAttacker()) taggedPlayers.put(attackEvent.getAttacker().getUniqueId(), getCombatTicks());
        taggedPlayers.put(attackEvent.getDefender().getUniqueId(), getCombatTicks());

        if(attackEvent.isDefenderRealPlayer() && attackEvent.hasAttacker() &&
                attackEvent.getDefenderPitPlayer() != attackEvent.getAttacker()) {
            attackEvent.getDefenderPitPlayer().lastHitUUID = attackEvent.getAttacker().getUniqueId();
        }
    }

    @EventHandler
    public static void onLeave(PitQuitEvent event) {
        Player player = event.getPlayer();
        event.getPlayer().closeInventory();
        if(NonManager.getNon(event.getPlayer()) != null) return;

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(isInCombat(player) || pitPlayer.isOnMega()) DamageManager.killPlayer(player);
        if(player.getWorld() == MapManager.getDarkzone() && !SpawnManager.isInSpawn(player)) DamageManager.killPlayer(player);
    }

    @EventHandler
    public static void onKill(KillEvent event) {
        taggedPlayers.remove(event.getDead().getUniqueId());
        if(event.isDeadPlayer()) event.getDeadPitPlayer().lastHitUUID = null;
    }

    @EventHandler
    public void onSpawn(PlayerSpawnCommandEvent event) {
        Player player = event.getPlayer();
        if(taggedPlayers.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            AOutput.error(player, "&c&l错误!&7 你在战斗中不能使用这个!");
            return;
        }

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(pitPlayer.isOnMega()) {
            event.setCancelled(true);
            AOutput.send(player, "&c&l错误!&7 你不能在进行超级连击时生成物品");
            return;
        }
    }
}
