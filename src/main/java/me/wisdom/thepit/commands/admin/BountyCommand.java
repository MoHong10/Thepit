package me.wisdom.thepit.commands.admin;

import me.wisdom.thepit.controllers.ChatTriggerManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BountyCommand extends ACommand {
    public BountyCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;
        if(!player.hasPermission("pitsim.bounty")) return;

        if(args.size() < 2) {
            AOutput.error(player, "用法: /bounty <玩家> <金额>");
            return;
        }

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(onlinePlayer.getDisplayName().equalsIgnoreCase(args.get(0))) {
                PitPlayer pitPlayer = PitPlayer.getPitPlayer(onlinePlayer);

                try {
                    pitPlayer.bounty += Integer.parseInt(args.get(1));
                    ChatTriggerManager.sendBountyInfo(pitPlayer);
                } catch(Exception ignored) {
                    AOutput.error(player, "请输入一个有效的数字");
                    return;
                }
                Sounds.BOUNTY.play(onlinePlayer);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a成功!"));
            }
        }
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}
