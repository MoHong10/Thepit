package me.wisdom.thepit.commands;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.HopperManager;
import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.SpawnManager;
import me.wisdom.thepit.controllers.objects.Hopper;
import me.wisdom.thepit.events.PlayerSpawnCommandEvent;
import me.wisdom.thepit.helmetabilities.PhoenixAbility;
import me.wisdom.thepit.perks.Streaker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        PlayerSpawnCommandEvent event = new PlayerSpawnCommandEvent(player);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled() && !player.isOp()) return false;

        SpawnManager.lastLocationMap.remove(player);
        Location teleportLoc = Thepit.status.isOverworld() ? MapManager.currentMap.getSpawn() : MapManager.getDarkzoneSpawn();
        player.teleport(teleportLoc);
        PhoenixAbility.alreadyActivatedList.remove(player.getUniqueId());
        for(Hopper hopper : HopperManager.hopperList) {
            if(player != hopper.target) continue;
            hopper.remove();
        }

        Streaker.xpReward.remove(player);
        Streaker.playerTimes.remove(player);

        return false;
    }
}
