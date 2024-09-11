package me.wisdom.thepit.commands;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class BalanceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (!player.isOp() || args.length < 1) {
            PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
            AOutput.send(player, "&6&l金币！&7 你拥有 " + Formatter.formatGoldFull(pitPlayer.gold));
            AOutput.send(player, "&f&l灵魂！&7 你拥有 " + Formatter.formatSouls(pitPlayer.taintedSouls));
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            AOutput.error(player, "&c&l错误！&7 找不到该玩家");
            return false;
        }

        PitPlayer pitTarget = PitPlayer.getPitPlayer(target);
        AOutput.send(player, "&6&l金币！&7 " + Misc.getDisplayName(target) + " &7拥有 " + Formatter.formatGoldFull(pitTarget.gold));
        AOutput.send(player, "&f&l灵魂！&7 " + Misc.getDisplayName(target) + " &7拥有 " + Formatter.formatSouls(pitTarget.taintedSouls));
        return false;
    }
}
