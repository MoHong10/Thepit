package me.wisdom.thepit.controllers;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.PitJoinEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class ResourcePackManager implements Listener {

    @EventHandler
    public void onJoin(PitJoinEvent event) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(event.getPlayer());
        if(pitPlayer.promptPack) {
            event.getPlayer().setResourcePack("https://cdn.discordapp.com/attachments/803483152630677524/1035038648552394782/Nebula_PitEdit.zip");

//			event.getPlayer().setResourcePack("https://cdn.discordapp.com/attachments/803483152630677524/903075400442314772/PitSim.zip");
        } else {
            TextComponent nonClick = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&c&l我们建议您使用我们的资源包，以获得更好的\n&c&l游戏体验。要这样做，请点击 "));
            TextComponent click = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6&l这里。"));
            click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/resource"));
            nonClick.addExtra(click);
            event.getPlayer().spigot().sendMessage(nonClick);
        }
    }

    @EventHandler
    public void onPrompt(PlayerResourcePackStatusEvent event) {
        if(event.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED) {
            PitPlayer pitPlayer = PitPlayer.getPitPlayer(event.getPlayer());
            pitPlayer.promptPack = true;
        }
        if(event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
            PitPlayer pitPlayer = PitPlayer.getPitPlayer(event.getPlayer());
            pitPlayer.promptPack = false;
        }
    }
}
