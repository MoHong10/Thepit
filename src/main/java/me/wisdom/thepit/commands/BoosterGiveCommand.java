package me.wisdom.thepit.commands;

import me.wisdom.thepit.controllers.objects.Booster;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BoosterGiveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) return false;
        String playerString = args[0];
        Player player = null;
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(!onlinePlayer.getName().equalsIgnoreCase(playerString)) continue;
            player = onlinePlayer;
            break;
        }
        if(player == null) return false;
        Booster.setBooster(player, args[1], Booster.getBoosterAmount(player, args[1]) + 1);

        Booster booster = Booster.getBooster(args[1]);
        assert booster != null;
        AOutput.send(player, "&6&l增益器！&7 收到 &f1 " + booster.color + booster.name + "&7。");

        return false;
    }
}
