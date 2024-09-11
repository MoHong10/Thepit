package me.wisdom.thepit.commands.admin;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Lang;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StreakCommand extends ACommand {
    public StreakCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if(args.size() < 1) {
            AOutput.error(sender, "用法: /streak [玩家] <数量>");
            return;
        }

        Player target = player;
        int amount;

        if(args.size() == 1) {
            try {
                amount = Integer.parseInt(args.get(0));
            } catch(Exception ignored) {
                AOutput.error(player, "无效的数字");
                return;
            }
        } else {
            target = Bukkit.getPlayer(args.get(0));
            if(target == null) {
                Lang.COULD_NOT_FIND_PLAYER_WITH_NAME.send(player);
                return;
            }

            try {
                amount = Integer.parseInt(args.get(1));
            } catch(Exception ignored) {
                AOutput.error(player, "无效的数字");
                return;
            }
        }

        PitPlayer pitTarget = PitPlayer.getPitPlayer(target);
        for(int i = 0; i < amount; i++) pitTarget.incrementKills();
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}
