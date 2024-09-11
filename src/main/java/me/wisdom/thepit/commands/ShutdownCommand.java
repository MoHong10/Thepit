package me.wisdom.thepit.commands;

import me.wisdom.thepit.controllers.ShutdownManager;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShutdownCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!sender.isOp()) return false;

        if(args.length < 1) {
            AOutput.send(sender, "&c用法: /ps shutdown <分钟数>");
            return false;
        }

        int minutes;
        try {
            minutes = Integer.parseInt(args[0]);
        } catch(Exception e) {
            AOutput.send(sender, "&c无效的参数。用法: /ps shutdown <分钟数>");
            return false;
        }

        if(minutes < 1) {
            AOutput.send(sender, "&c无效的参数。用法: /ps shutdown <分钟数>");
            return false;
        }

        if(ShutdownManager.isShuttingDown) {
            AOutput.send(sender, "&c服务器已经在关机中！");
            return false;
        }

        if(minutes != 0) {
            ShutdownManager.initiateShutdown(minutes);
            AOutput.send(sender, "&a关机已启动！");
        }
        return false;
    }
}

