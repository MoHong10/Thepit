package me.wisdom.thepit.commands;

import de.myzelyam.api.vanish.VanishAPI;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Lang;
import me.wisdom.thepit.upgrades.TheWay;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

public class PayCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (args.length < 2) {
                    AOutput.error(player, "&c&l错误!&7 用法: /pay <玩家> <金额>");
                    return;
                }

                PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
                int levelRequired = 100 - TheWay.INSTANCE.getLevelReduction(pitPlayer.player);
                if (pitPlayer.level < levelRequired && !player.isOp()) {
                    AOutput.error(player, "&c&l错误!&7 你不能交易直到达到等级 " + levelRequired);
                    return;
                }

                Player target = null;
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (!onlinePlayer.getName().equalsIgnoreCase(args[0])) continue;
                    if (VanishAPI.isInvisible(onlinePlayer)) continue;
                    target = onlinePlayer;
                    break;
                }
                if (target == null) {
                    Lang.COULD_NOT_FIND_PLAYER_WITH_NAME.send(player);
                    return;
                } else if (target == player) {
                    AOutput.error(player, "&c&l错误!&7 你不能支付给自己");
                    return;
                }

                PitPlayer pitTarget = PitPlayer.getPitPlayer(target);
                int levelRequiredTarget = 100 - TheWay.INSTANCE.getLevelReduction(pitTarget.player);
                if (pitTarget.level < levelRequiredTarget && !player.isOp()) {
                    AOutput.error(player, "&c&l错误!&7 那个玩家的等级未达到 " + levelRequiredTarget);
                    return;
                }

                int amount;
                try {
                    amount = Integer.parseInt(args[1]);
                    if (amount <= 0) throw new IllegalArgumentException();
                } catch (Exception ignored) {
                    AOutput.error(player, "无效的金额");
                    return;
                }

                if (amount > pitPlayer.gold) {
                    AOutput.error(player, "你没有足够的钱");
                    return;
                }

                pitPlayer.gold -= amount;
                pitTarget.gold += amount;
                DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0");
                AOutput.send(player, "&6&l交易!&7 你已向 &6" + target.getName() + " &7发送了 $" + decimalFormat.format(amount));
                AOutput.send(target, "&6&l交易!&7 你已从 &7" + player.getName() + " 收到 $" + decimalFormat.format(amount));
            }
        }.runTask(Thepit.INSTANCE);
        return false;
    }
}
