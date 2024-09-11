package me.wisdom.thepit.perks;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPerk;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Gladiator extends PitPerk {
    public static Gladiator INSTANCE;
    public static Map<UUID, Integer> nearbyPlayerMap = new HashMap<>();

    public Gladiator() {
        super("Gladiator", "gladiator");
        INSTANCE = this;
    }

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(!INSTANCE.hasPerk(player)) continue;

                    nearbyPlayerMap.putIfAbsent(player.getUniqueId(), 0);
                    List<Entity> players = player.getNearbyEntities(12, 12, 12);
                    players.removeIf(entity -> !(entity instanceof Player));
                    int nearbyPlayers = Math.min(players.size(), 10);
                    if(nearbyPlayers < 3) nearbyPlayers = 0;
                    nearbyPlayerMap.put(player.getUniqueId(), nearbyPlayers);
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 9L, 40L);
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!hasPerk(attackEvent.getDefender())) return;
        attackEvent.multipliers.add(Misc.getReductionMultiplier(getReduction(attackEvent.getDefenderPlayer())));
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.BONE)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, Player player) {
        loreBuilder.addLongLine("&7每个附近的玩家减少 &9-3% &7伤害");
        loreBuilder.addLongLine("&7有效范围为 12 格，至少 3 名玩家，最多 10 名玩家");
    }

    @Override
    public String getSummary() {
        return "&aGladiator &7根据你周围的玩家数量给你 &9-3% 的伤害减少";
    }

    public static int getNearbyPlayers(Player player) {
        return nearbyPlayerMap.getOrDefault(player.getUniqueId(), 0);
    }

    public static int getReduction(Player player) {
        return 3 * getNearbyPlayers(player);
    }
}
