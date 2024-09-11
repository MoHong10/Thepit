package me.wisdom.thepit.commands;

import me.wisdom.thepit.controllers.BoosterManager;
import me.wisdom.thepit.controllers.ProxyMessaging;
import me.wisdom.thepit.controllers.objects.Booster;
import me.wisdom.thepit.inventories.BoosterGUI;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BoosterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (player.isOp() && args.length != 0) {

            String concatenatedBoosters = "";
            for (Booster booster : BoosterManager.boosterList)
                concatenatedBoosters += concatenatedBoosters.isEmpty() ? booster.refName : ", " + booster.refName;
            if (args.length < 2) {
                AOutput.send(player, "用法: /booster <" + concatenatedBoosters + "> <分钟数|clear>");
                return false;
            }

            Booster booster = BoosterManager.getBooster(args[0]);
            if (booster == null) {
                AOutput.error(player, "该增益器不存在");
                return false;
            }

            int minutes;
            try {
                minutes = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
                if (!args[1].equalsIgnoreCase("clear")) {
                    AOutput.error(player, "无效的时间");
                    return false;
                }

                booster.disable();
                return false;
            }

            AOutput.send(player, "&7已为增益器增加了 &b" + minutes + " &7分钟。增益器将再活跃 &b" + (booster.minutes + minutes) + " &7分钟" +
                    (booster.minutes == 1 ? "" : "s"));

            ProxyMessaging.sendBoosterUse(booster, player, minutes, false);

            return false;
        } else {
            BoosterGUI boosterGUI = new BoosterGUI(player);
            boosterGUI.open();
            return false;
        }
    }
}

