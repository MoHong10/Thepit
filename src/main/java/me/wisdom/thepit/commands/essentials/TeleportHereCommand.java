package me.wisdom.thepit.commands.essentials;

import me.wisdom.thepit.misc.Lang;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportHereCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (!player.hasPermission("pitsim.teleport")) {
            Lang.NO_PERMISSION.send(player);
            return false;
        }

        if (args.length < 1) {
            AOutput.error(player, "&c&l错误!&7 用法: /" + label + " <目标>");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            AOutput.error(player, "&c&l错误!&7 找不到名为: " + args[0] + " 的玩家");
            return false;
        }

        if (target == player) {
            AOutput.error(player, "&c&l错误!&7 你不能传送到自己身上");
            return false;
        }

        TeleportCommand.TPLocation tpLocation = new TeleportCommand.TPLocation(player);
        tpLocation.teleport(target);
        tpLocation.sendThirdPartyTeleportMessage(target, player);
        return false;
    }
}
