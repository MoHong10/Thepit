package me.wisdom.thepit.commands.essentials;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.misc.Lang;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlyCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (!player.hasPermission("pitsim.flight") && !Thepit.isDev()) {
            Lang.NO_PERMISSION.send(player);
            return false;
        }

        Player target = player;
        if (args.length > 0) {
            target = Bukkit.getPlayer(args[0]);
        }

        if (target == null) {
            Lang.COULD_NOT_FIND_PLAYER_WITH_NAME.send(player);
            return false;
        }

        if (target.getAllowFlight()) {
            target.setAllowFlight(false);
            AOutput.send(target, "&f&l飞行!&7 你的飞行已经被 &c禁用");
            if (target != player) AOutput.send(player, "&f&l飞行!&7 你已为 " + Misc.getDisplayName(target) + " &c禁用&7了飞行");
        } else {
            target.setAllowFlight(true);
            AOutput.send(target, "&f&l飞行!&7 你的飞行已经被 &a启用");
            if (target != player) AOutput.send(player, "&f&l飞行!&7 你已为 " + Misc.getDisplayName(target) + " &a启用&7了飞行");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return new ArrayList<>();
        Player player = (Player) sender;
        if (!player.hasPermission("pitsim.flight")) return new ArrayList<>();

        List<String> players = new ArrayList<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer == player) continue;
            players.add(onlinePlayer.getName());
        }
        return Misc.getTabComplete(args[0], players);
    }
}
