package me.wisdom.thepit.controllers.objects;

import me.wisdom.thepit.Thepit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerToPlayerCooldown {
    private final long cooldownTicks;
    private final Map<UUID, Map<UUID, Long>> cooldownMap = new HashMap<>();

    public PlayerToPlayerCooldown(long cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }

    public boolean isOnCooldown(Player mainPlayer, Player otherPlayer) {
        cooldownMap.putIfAbsent(mainPlayer.getUniqueId(), new HashMap<>());
        Map<UUID, Long> playerCooldownMap = cooldownMap.get(mainPlayer.getUniqueId());
        Long cooldown = playerCooldownMap.getOrDefault(otherPlayer.getUniqueId(), 0L);
        if(cooldown + cooldownTicks >= Thepit.currentTick) return true;
        playerCooldownMap.put(otherPlayer.getUniqueId(), Thepit.currentTick);
        return false;
    }
}
