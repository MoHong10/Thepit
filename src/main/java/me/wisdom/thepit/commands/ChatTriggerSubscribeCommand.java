package me.wisdom.thepit.commands;

import me.wisdom.thepit.controllers.ChatTriggerManager;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatTriggerSubscribeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (!player.isOp() && !String.join(" ", args).equals("不要运行此命令 (this is the chattrigger handshake)")) return false;

        if (ChatTriggerManager.isSubscribed(player)) {
            AOutput.send(player, ChatTriggerManager.PREFIX + "你已经启用了此功能");
            return false;
        }

        ChatTriggerManager.subscribePlayer(player);
        AOutput.send(player, ChatTriggerManager.PREFIX + "已订阅以接收大量数据");
        return false;
    }
}
