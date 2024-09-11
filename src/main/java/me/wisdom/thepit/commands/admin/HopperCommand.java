package me.wisdom.thepit.commands.admin;

import me.wisdom.thepit.controllers.HopperManager;
import me.wisdom.thepit.controllers.objects.Hopper;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HopperCommand extends ACommand implements CommandExecutor {
    public HopperCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] argsArray) {
        execute(sender, command, alias, new ArrayList<>(Arrays.asList(argsArray)));
        return false;
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!sender.isOp()) return;

        String concatHoppers = "";
        for(Hopper.Type type : Hopper.Type.values())
            concatHoppers += concatHoppers.isEmpty() ? type.refName : ", " + type.refName;
        if(args.size() < 2) {
            AOutput.send(sender, "使用方法: /hopper <" + concatHoppers + "> <目标>");
            return;
        }

        Hopper.Type type = Hopper.Type.getType(args.get(0));
        if(type == null) {
            AOutput.error(sender, "无效的hopper类型");
            return;
        }

        Player target = null;
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(!onlinePlayer.getName().equalsIgnoreCase(args.get(1))) continue;
            target = onlinePlayer;
            break;
        }
        if(target == null) {
            AOutput.error(sender, "找不到目标玩家");
            return;
        }

        HopperManager.callHopper("PayForTruce", type, target);
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}