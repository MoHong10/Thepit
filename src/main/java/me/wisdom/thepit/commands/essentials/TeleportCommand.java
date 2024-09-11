package me.wisdom.thepit.commands.essentials;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.misc.Lang;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {
    public static TeleportCommand INSTANCE;

    public TeleportCommand() {
        INSTANCE = this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (!player.hasPermission("pitsim.teleport") && !Thepit.isDev()) {
            Lang.NO_PERMISSION.send(player);
            return false;
        }

        Player target;
        TPLocation tpLocation;

        if (args.length < 1) {
            AOutput.error(player, "&c&l错误!&7 用法: /" + label + " [目标] <位置> 或 /" + label + " [目标] <x> <y> <z>");
            return false;
        }

        if (args.length == 1) {
            Player locationPlayer = Bukkit.getPlayer(args[0]);
            if (locationPlayer == null) {
                AOutput.error(player, "&c&l错误!&7 找不到名字为: " + args[0] + " 的玩家");
                return false;
            }

            target = player;
            tpLocation = new TPLocation(locationPlayer);
        } else if (args.length == 2 && player.hasPermission("pitsim.teleport")) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                AOutput.error(player, "&c&l错误!&7 找不到名字为: " + args[0] + " 的玩家");
                return false;
            }

            Player locationPlayer = Bukkit.getPlayer(args[1]);
            if (locationPlayer == null) {
                AOutput.error(player, "&c&l错误!&7 找不到名字为: " + args[1] + " 的玩家");
                return false;
            }
            tpLocation = new TPLocation(locationPlayer);
        } else if (args.length == 3) {
            target = player;
            try {
                tpLocation = new TPLocation(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
            } catch (Exception ignored) {
                AOutput.error(player, "&c&l错误!&7 坐标无效");
                return false;
            }
        } else if (args.length == 4 && player.hasPermission("pitsim.teleport")) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                AOutput.error(player, "&c&l错误!&7 找不到名字为: " + args[0] + " 的玩家");
                return false;
            }

            try {
                tpLocation = new TPLocation(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
            } catch (Exception ignored) {
                AOutput.error(player, "&c&l错误!&7 坐标无效");
                return false;
            }
        } else {
            AOutput.error(player, "&c&l错误!&7 用法: /" + label + " [目标] <位置> 或 /" + label + " [目标] <x> <y> <z>");
            return false;
        }

        if (target == tpLocation.locationPlayer) {
            if (target == player) {
                AOutput.error(player, "&c&l错误!&7 你不能传送到自己");
                return false;
            }
            AOutput.error(player, "&c&l错误!&7 你不能将玩家传送到自己");
            return false;
        }

        tpLocation.teleport(target);
        if (target != player) tpLocation.sendThirdPartyTeleportMessage(target, player);
        return false;
    }

    public static class TPLocation {
        private Player locationPlayer;
        private double x;
        private double y;
        private double z;

        public TPLocation(Player locationPlayer) {
            this.locationPlayer = locationPlayer;
        }

        public TPLocation(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void teleport(Player target) {
            if (locationPlayer != null) {
                target.teleport(locationPlayer);
                AOutput.send(target, "&9&lTP!&7 你已被传送到 " + Misc.getDisplayName(locationPlayer));
            } else {
                target.teleport(new Location(target.getWorld(), x, y, z));
                AOutput.send(target, "&9&lTP!&7 你已被传送到 &9" + x + "&7, &9" + y + "&7, &9" + z);
            }
        }

        public void sendThirdPartyTeleportMessage(Player target, Player player) {
            if (locationPlayer != null) {
                AOutput.send(player, "&9&lTP!&7 " + Misc.getDisplayName(target) + " &7已被传送到 " + Misc.getDisplayName(locationPlayer));
            } else {
                AOutput.send(player, "&9&lTP!&7 " + Misc.getDisplayName(target) + " &7已被传送到 &9" + x + "&7, &9" + y + "&7, &9" + z);
            }
        }
    }
}
