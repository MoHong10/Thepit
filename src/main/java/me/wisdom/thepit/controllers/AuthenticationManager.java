package me.wisdom.thepit.controllers;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PluginMessage;
import me.wisdom.thepit.events.MessageEvent;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class AuthenticationManager implements Listener {

    @EventHandler
    public void onMessage(MessageEvent event) {
        PluginMessage message = event.getMessage();
        List<String> strings = message.getStrings();
        if(strings.isEmpty()) return;

        if(strings.get(0).equals("AUTH_SUCCESS")) {
            UUID playerUUID = UUID.fromString(strings.get(1));
            Player player = Bukkit.getPlayer(playerUUID);
            if(player == null) return;

            new BukkitRunnable() {
                @Override
                public void run() {
                    ConsoleCommandSender console = Thepit.INSTANCE.getServer().getConsoleSender();
                    Bukkit.dispatchCommand(console, "cc give p basic 1 " + player.getName());
                    AOutput.send(player, "&9&l链接!&7 谢谢!");
                }
            }.runTask(Thepit.INSTANCE);
        }
    }
}
