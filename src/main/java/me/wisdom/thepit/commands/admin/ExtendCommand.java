package me.wisdom.thepit.commands.admin;

import me.wisdom.thepit.controllers.ShutdownManager;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ExtendCommand extends ACommand {
    public ExtendCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!sender.isOp()) return;

        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if(args.size() != 1) {
            AOutput.send(player, "&c无效的参数! /extend <分钟>");
            return;
        }

        int minutes = 0;

        try {
            minutes = Integer.parseInt(args.get(0));
        } catch(Exception e) {
            AOutput.send(player, "&c无效的参数! /extend <分钟>");
            return;
        }

        if(minutes < 1) {
            AOutput.send(player, "&c无效的参数! /extend <分钟>");
            return;
        }

        if(ShutdownManager.minutes == 0 && ShutdownManager.seconds < 10) {
            AOutput.send(player, "&c现在已经太晚，无法延长关机时间!");
            return;
        }

        ShutdownManager.minutes += minutes;

        AOutput.send(player, "&a关机时间延长了 " + minutes + " 分钟!");
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}
