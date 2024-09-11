package me.wisdom.thepit.commands.essentials;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Lang;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NicknameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        Player target;
        String nickname;

        if (player.isOp() && args.length >= 2) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                Lang.COULD_NOT_FIND_PLAYER_WITH_NAME.send(player);
                return false;
            }
            if (!target.hasPermission("pitsim.nick")) {
                AOutput.error(player, "&c&l错误!&7 他们需要拥有 &4Eternal &7才能设置昵称");
                return false;
            }
            nickname = args[1];
        } else {
            if (!player.hasPermission("pitsim.nick")) {
                AOutput.error(player, "&c&l错误!&7 你需要拥有 &4Eternal &7才能设置昵称");
                return false;
            }
            if (args.length < 1) {
                if (player.isOp()) {
                    AOutput.error(player, "&c&l错误!&7 用法: /" + label + " [玩家] <昵称|重置>");
                } else {
                    AOutput.error(player, "&c&l错误!&7 用法: /" + label + " <昵称|重置>");
                }
                return false;
            }
            target = player;
            nickname = args[0];
        }

        PitPlayer pitTarget = PitPlayer.getPitPlayer(target);
        if (nickname.equalsIgnoreCase("off") || nickname.equalsIgnoreCase("reset")) {
            pitTarget.nickname = null;
            AOutput.send(target, "&4&l昵称!&7 已关闭你的昵称");
            if (target != player) AOutput.send(player, "&4&l昵称!&7 已关闭 " + Misc.getDisplayName(target) + " 的昵称");
            return false;
        }

        if (!player.isOp() && !nickname.matches("^\\w+$")) {
            AOutput.error(player, "&c&l错误!&7 昵称只能包含普通字符");
            return false;
        }
        if (nickname.length() > 16) {
            AOutput.error(player, "&c&l错误!&7 昵称长度不能超过16个字符");
            return false;
        }
        OfflinePlayer nickTest = Bukkit.getOfflinePlayer(nickname);
//        TODO: 检查代理服务器而不是spigot服务器
        if (!player.isOp() && nickTest.hasPlayedBefore()) {
            AOutput.error(player, "&c&l错误!&7 已有玩家使用过这个昵称");
            return false;
        }

        pitTarget.nickname = nickname;
        AOutput.send(player, "&4&l昵称!&7 你的昵称已设置为 " + Misc.getDisplayName(player));
        if (target != player) AOutput.send(player, "&4&l昵称!&7 设置 " + Misc.getDisplayName(target) + " 的昵称");
        return false;
    }
}
