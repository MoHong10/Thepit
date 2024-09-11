package me.wisdom.thepit.commands.essentials;

import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("pitsim.broadcast")) return false;

        if(args.length < 1) {
            AOutput.error(sender, "&c&l错误!&7 使用: /" + label + " <message>");
            return false;
        }

        Misc.broadcast(String.join(" ", args));
        return false;
    }
}
