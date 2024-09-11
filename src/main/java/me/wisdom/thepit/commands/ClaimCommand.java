package me.wisdom.thepit.commands;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.DiscordManager;
import me.wisdom.thepit.controllers.objects.PluginMessage;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ClaimCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        long lastClaim = DiscordManager.getLastBoostRewardClaim(player.getUniqueId());
        if (lastClaim == -1) {
            AOutput.send(player, "&c&l错误！&7 你没有绑定的 Discord 账户。运行 /link 来绑定一个");
            return false;
        }

        new PluginMessage()
                .writeString("BOOSTER_CLAIM")
                .writeString(Thepit.serverName)
                .writeString(player.getUniqueId().toString())
                .send();
        return false;
    }

    public static void callback(Player player, boolean isBooster) {
        if (!isBooster) {
            AOutput.error(player, "&c&l错误！&7 你并未为服务器提供增益");
            return;
        }

        long lastClaim = DiscordManager.getLastBoostRewardClaim(player.getUniqueId());
        if (lastClaim == -1) {
            AOutput.send(player, "&c&l错误！&7 你没有绑定的 Discord 账户。运行 /link 来绑定一个");
            return;
        }

        long nextClaimTime = lastClaim + 1000L * 60 * 60 * 24 * 30;
        long currentTime = System.currentTimeMillis();
        if (nextClaimTime > currentTime) {
            AOutput.send(player, "&c&l错误！&7 你需要再等待 " +
                    Formatter.formatDurationFull(nextClaimTime - currentTime, true) + " 才能再次领取");
            return;
        }

        DiscordManager.setLastBoostRewardClaim(player.getUniqueId(), currentTime);

        new BukkitRunnable() {
            @Override
            public void run() {
                ConsoleCommandSender console = Thepit.INSTANCE.getServer().getConsoleSender();
                Bukkit.dispatchCommand(console, "cc give p basic 1 " + player.getName());
                AOutput.send(player, "&d&l哦！&7 感谢你为服务器提供增益！");
            }
        }.runTask(Thepit.INSTANCE);
    }
}
