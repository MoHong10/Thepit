package me.wisdom.thepit.controllers;

import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.myzelyam.api.vanish.VanishAPI;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.PitEntityType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.events.PitQuitEvent;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnManager implements Listener {
    public static Map<Player, Location> lastLocationMap = new HashMap<>();

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(World world : Bukkit.getWorlds()) {
                    List<Entity> toRemove = new ArrayList<>();
                    for(Entity entity : world.getEntities()) {
                        if(!(entity instanceof Arrow)) continue;
                        if(!SpawnManager.isInSpawn(entity.getLocation())) continue;
                        toRemove.add(entity);
                    }
                    toRemove.forEach(Entity::remove);
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 20);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        // 如果玩家在 NonManager 或 HopperManager 中存在，则直接返回
        if(NonManager.getNon(player) != null || HopperManager.getHopper(player) != null) return;

        Location location = player.getLocation();
        boolean isInSpawn = isInSpawn(location);

        // 如果玩家在出生点区域
        if(isInSpawn) {
            // 如果玩家是 OP 或者玩家处于隐形状态，则直接返回
            if(player.isOp() || VanishAPI.isInvisible(player)) return;

            // 如果 lastLocationMap 中不包含玩家的位置信息，则直接返回
            if(!lastLocationMap.containsKey(player)) return;

            PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

            // 如果玩家在 Mega 状态下
            if(pitPlayer.isOnMega()) {
                Location lastLocation = lastLocationMap.get(player);
                player.teleport(lastLocation); // 传送玩家回到上一个位置
                player.setVelocity(new Vector()); // 将玩家的速度设置为零
                AOutput.error(event.getPlayer(), "&c&c&l错误!&7 你不能在 连杀 状态下进入出生点!");
            }
            // 如果玩家正在战斗中
            else if(CombatManager.isInCombat(player)) {
                Location lastLocation = lastLocationMap.get(player);
                player.teleport(lastLocation); // 传送玩家回到上一个位置
                player.setVelocity(new Vector()); // 将玩家的速度设置为零
                AOutput.error(event.getPlayer(), "&c&c&l错误!&7 你不能在战斗中进入出生点!");
            }
            // 如果以上条件都不满足
            else {
                lastLocationMap.remove(player); // 从 lastLocationMap 中移除玩家的信息
            }
        }
        // 如果玩家不在出生点区域
        else {
            lastLocationMap.put(player, location); // 将玩家当前位置保存到 lastLocationMap 中
        }
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(killEvent.isDeadPlayer()) lastLocationMap.remove(killEvent.getDeadPlayer());
    }

    @EventHandler
    public void onQuit(PitQuitEvent event) {
        lastLocationMap.remove(event.getPlayer());
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getProjectile() instanceof Arrow)) return;

        Player player = (Player) event.getEntity();

        if(isInSpawn(player)) {
            event.setCancelled(true);
            Sounds.NO.play(player);
        }
    }

    @EventHandler
    public void onAttack(AttackEvent.Pre attackEvent) {
        if(!isInSpawn(attackEvent.getAttackerPlayer()) && !isInSpawn(attackEvent.getDefender().getLocation())) return;
        attackEvent.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(!isInSpawn(event.getItemDrop().getLocation())) return;
        ItemStack itemStack = event.getItemDrop().getItemStack();
        if(itemStack.getType() == Material.ENDER_CHEST || itemStack.getType() == Material.TRIPWIRE_HOOK) return;

        PitItem pitItem = ItemFactory.getItem(itemStack);
        if(pitItem == null || !pitItem.destroyIfDroppedInSpawn) return;
        if(MysticFactory.isImportant(itemStack)) return;

        event.getItemDrop().remove();
        Sounds.NO.play(event.getPlayer());
        AOutput.send(event.getPlayer(), "&c&l物品删除!&7 掉落在出生点区域!");
    }

    public static boolean isInSpawn(Player player) {
        if(player == null) return false;
        return isInSpawn(player, player.getLocation());
    }

    public static boolean isInSpawn(Location location) {
        return isInSpawn(null, location);
    }

    private static boolean isInSpawn(Player player, Location location) {
        if(player != null && !player.isOp() && Misc.isEntity(player, PitEntityType.REAL_PLAYER) &&
                CombatManager.isInCombat(player)) return false;

        RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
        RegionManager regions = container.get(location.getWorld());
        assert regions != null;
        ApplicableRegionSet set = regions.getApplicableRegions((BukkitUtil.toVector(location)));

        for(ProtectedRegion region : set) if(region.getId().contains("spawn") || region.getId().contains("auction")) return true;
        return false;
    }

    public static void clearSpawnStreaks() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(isInSpawn(player) && !lastLocationMap.containsKey(player)) {
                PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
                pitPlayer.endKillstreak();
            }
        }
    }

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                clearSpawnStreaks();
            }
        }.runTaskTimer(Thepit.INSTANCE, 20L, 20L);
    }
}
