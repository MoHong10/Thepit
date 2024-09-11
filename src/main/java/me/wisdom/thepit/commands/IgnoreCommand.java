package me.wisdom.thepit.commands;

import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Lang;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IgnoreCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        if (PlayerManager.isStaff(player.getUniqueId())) {
            AOutput.error(player, "&c&l错误！&7 员工不能忽略玩家");
            return false;
        }

        if (!player.hasPermission("group.legendary")) {
            AOutput.error(player, "&c&l错误！&7 您需要拥有 &eLegendary &7等级才能执行此操作");
            return false;
        }

        if (args.length < 1) {
            AOutput.send(player, "&8&m--------------------&8<&c&l忽略&8>&m--------------------");
            AOutput.send(player, "&c * &7/" + label + " add <ign> &7(将玩家添加到忽略列表)");
            AOutput.send(player, "&c * &7/" + label + " remove <ign> &7(从忽略列表中移除玩家)");
            AOutput.send(player, "&c * &7/" + label + " list &7(查看您的忽略列表)");
            AOutput.send(player, "&8&m--------------------&8<&c&l忽略&8>&m--------------------");
            return false;
        }

        String command = args[0].toLowerCase();
        if (command.equals("add")) {
            if (args.length < 2) {
                AOutput.error(player, "&c&l错误！&7 用法：/" + label + " add <ign>");
                return false;
            }

            OfflinePlayer target;
            try {
                UUID uuid = UUID.fromString(args[1]);
                target = Bukkit.getOfflinePlayer(uuid);
            } catch (Exception ignored) {
                target = Bukkit.getOfflinePlayer(args[1]);
            }
            if (target == null) {
                Lang.COULD_NOT_FIND_PLAYER_WITH_NAME.send(player);
                return false;
            }

            if (pitPlayer.uuidIgnoreList.contains(target.getUniqueId().toString())) {
                AOutput.error(player, "&c&l错误！&7 该玩家已经在您的忽略列表中");
                return false;
            }

            if (target == player) {
                AOutput.error(player, "&c&l错误！&7 您不能忽略自己");
                return false;
            }

            if (PlayerManager.isStaff(target.getUniqueId())) {
                AOutput.error(player, "&c&l错误！&7 您不能忽略工作人员");
                return false;
            }

            pitPlayer.uuidIgnoreList.add(target.getUniqueId().toString());
            AOutput.send(player, "&c&l忽略！&7 您已将 " + target.getName() + " 添加到您的忽略列表");
        } else if (command.equals("remove")) {
            if (args.length < 2) {
                AOutput.error(player, "&c&l错误！&7 用法：/" + label + " remove <ign>");
                return false;
            }

            OfflinePlayer target;
            try {
                UUID uuid = UUID.fromString(args[1]);
                target = Bukkit.getOfflinePlayer(uuid);
            } catch (Exception ignored) {
                target = Bukkit.getOfflinePlayer(args[1]);
            }
            if (target == null) {
                Lang.COULD_NOT_FIND_PLAYER_WITH_NAME.send(player);
                return false;
            }

            if (!pitPlayer.uuidIgnoreList.contains(target.getUniqueId().toString())) {
                AOutput.error(player, "&c&l错误！&7 该玩家不在您的忽略列表中");
                return false;
            }

            pitPlayer.uuidIgnoreList.remove(target.getUniqueId().toString());
            AOutput.send(player, "&c&l忽略！&7 您已将 " + target.getName() + " 从您的忽略列表中移除");
        } else if (command.equals("list")) {

            if (pitPlayer.uuidIgnoreList.isEmpty()) {
                AOutput.send(player, "&c&l忽略！&7 您的忽略列表为空");
                return false;
            }

            AOutput.send(player, "&c&l忽略！&7 您已忽略以下玩家：");
            for (String playerUUIDString : pitPlayer.uuidIgnoreList) {
                OfflinePlayer ignoredPlayer = Bukkit.getOfflinePlayer(UUID.fromString(playerUUIDString));
                AOutput.send(player, "&c * &7" + ignoredPlayer.getName());
            }
        } else {
            AOutput.send(player, "&8&m--------------------&8<&c&l忽略&8>&m--------------------");
            AOutput.send(player, "&c * &7/" + label + " add <ign> &7(将玩家添加到忽略列表)");
            AOutput.send(player, "&c * &7/" + label + " remove <ign> &7(从忽略列表中移除玩家)");
            AOutput.send(player, "&c * &7/" + label + " list &7(查看您的忽略列表)");
            AOutput.send(player, "&8&m--------------------&8<&c&l忽略&8>&m--------------------");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return new ArrayList<>();
        Player player = (Player) sender;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        if (PlayerManager.isStaff(player.getUniqueId())) return new ArrayList<>();

        if (args.length < 2) return Misc.getTabComplete(args[0], "add", "remove", "list");
        String command = args[0].toLowerCase();
        if (command.equals("add")) {
            List<String> players = new ArrayList<>();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer == player || PlayerManager.isStaff(onlinePlayer.getUniqueId())) continue;
                players.add(onlinePlayer.getName());
            }
            return Misc.getTabComplete(args[1], players);
        }
        if (command.equals("remove")) {
            List<String> ignoredPlayers = new ArrayList<>();
            for (String playerUUIDString : pitPlayer.uuidIgnoreList) {
                OfflinePlayer ignoredPlayer = Bukkit.getOfflinePlayer(UUID.fromString(playerUUIDString));
                ignoredPlayers.add(ignoredPlayer.getName());
            }
            return Misc.getTabComplete(args[1], ignoredPlayers);
        }
        return new ArrayList<>();
    }
}
