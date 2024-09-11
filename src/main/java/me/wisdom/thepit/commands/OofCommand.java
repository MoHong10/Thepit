package me.wisdom.thepit.commands;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.CombatManager;
import me.wisdom.thepit.controllers.DamageManager;
import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.SpawnManager;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OofCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (player.getWorld() == MapManager.getDarkzone() && !player.isOp() && !Thepit.isDev()) {
            AOutput.send(player, "&c&l错误!&7 你不能在黑暗区域使用此命令!");
            return false;
        }

        if (SpawnManager.isInSpawn(player) && !player.isOp()) {
            AOutput.send(player, "&c&l错误!&7 你不能在出生点使用 /oof!");
            Sounds.ERROR.play(player);
            return false;
        }

        if (!CombatManager.taggedPlayers.containsKey(player.getUniqueId())) {
            DamageManager.killPlayer(player);
            return false;
        }

        DamageManager.killPlayer(player);
        return false;
    }
}

