package me.wisdom.thepit.commands;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EcoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender.isOp())) return false;

        if (args.length < 3) {
            AOutput.error(sender, "&7用法: /eco <give|take|set> <玩家> <金额>");
            return false;
        }

        boolean affectEveryone = false;
        String subCommand = args[0];
        if (subCommand.equalsIgnoreCase("give")) {
            if (args[1].equals("*")) affectEveryone = true;

            Player target = Bukkit.getPlayer(args[1]);
            if (!affectEveryone && target == null) {
                AOutput.error(sender, "&c&l错误！&7 找不到该玩家");
                return false;
            }

            double amount;
            try {
                amount = Double.parseDouble(args[2]);
                if (amount <= 0) throw new Exception();
            } catch (Exception ignored) {
                AOutput.error(sender, "&c&l错误！&7 无效的金额");
                return false;
            }

            if (affectEveryone) {
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                if (sender instanceof Player) {
                    players.remove((Player) sender);
                    players.add((Player) sender);
                }
                for (Player onlinePlayer : players) giveGold(sender, onlinePlayer, amount);
                return false;
            }

            giveGold(sender, target, amount);
        } else if (subCommand.equalsIgnoreCase("take")) {
            if (args[1].equals("*")) affectEveryone = true;

            Player target = Bukkit.getPlayer(args[1]);
            if (!affectEveryone && target == null) {
                AOutput.error(sender, "&c&l错误！&7 找不到该玩家");
                return false;
            }

            double amount;
            try {
                amount = Double.parseDouble(args[2]);
                if (amount <= 0) throw new Exception();
            } catch (Exception ignored) {
                AOutput.error(sender, "&c&l错误！&7 无效的金额");
                return false;
            }

            if (affectEveryone) {
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                if (sender instanceof Player) {
                    players.remove((Player) sender);
                    players.add((Player) sender);
                }
                for (Player onlinePlayer : players) takeGold(sender, onlinePlayer, amount);
                return false;
            }

            takeGold(sender, target, amount);
        } else if (subCommand.equalsIgnoreCase("set")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                AOutput.error(sender, "&c&l错误！&7 找不到该玩家");
                return false;
            }

            double amount;
            try {
                amount = Double.parseDouble(args[2]);
                if (amount < 0) throw new Exception();
            } catch (Exception ignored) {
                AOutput.error(sender, "&c&l错误！&7 无效的金额");
                return false;
            }

            PitPlayer pitTarget = PitPlayer.getPitPlayer(target);
            pitTarget.gold = amount;
            AOutput.send(sender, "&6&l经济！&7 已将 " + Misc.getDisplayName(target) + "&7 的余额设置为 " + Formatter.formatGoldFull(amount));
            AOutput.send(target, "&6&l金币！&7 你的余额已设置为 " + Formatter.formatGoldFull(amount));
        } else {
            AOutput.error(sender, "&7用法: /eco <give|take|set> <玩家> <金额>");
        }

        return false;
    }

    public static void giveGold(CommandSender giver, Player target, double amount) {
        PitPlayer pitTarget = PitPlayer.getPitPlayer(target);
        pitTarget.gold += amount;
        if (giver != target) AOutput.send(giver, "&6&l经济！&7 已给予 " + Formatter.formatGoldFull(amount) + " &7给 " + Misc.getDisplayName(target));
        AOutput.send(pitTarget.player, "&6&l金币！&7 你收到了 " + Formatter.formatGoldFull(amount));
    }

    public static void takeGold(CommandSender giver, Player target, double amount) {
        PitPlayer pitTarget = PitPlayer.getPitPlayer(target);

        if (amount > pitTarget.gold) {
            AOutput.error(giver, "&c&l错误！&7 该玩家只有 " + Formatter.formatGoldFull(pitTarget.gold));
            return;
        }

        pitTarget.gold -= amount;
        if (giver != target) AOutput.send(giver, "&6&l经济！&7 从 " + Misc.getDisplayName(target) + " 处扣除了 " + Formatter.formatGoldFull(amount));
        AOutput.send(target, "&6&l金币！&7 从你那里扣除了 " + Formatter.formatGoldFull(amount));
    }
}
