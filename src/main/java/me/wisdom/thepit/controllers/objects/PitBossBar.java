package me.wisdom.thepit.controllers.objects;

import me.wisdom.thepit.Thepit;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PitBossBar {
    public BossBar bossBar;
    public List<Player> players = new ArrayList<>();

    public PitBossBar(String text, float progress) {
        this.bossBar = BossBar.bossBar(Component.text(text), progress, BossBar.Color.PINK, BossBar.Overlay.PROGRESS);
    }

    public void updateProgress(float progress) {
        this.bossBar.progress(progress);
    }

    public void updateText(String text) {
        this.bossBar.name(Component.text(text));
    }

    public void updatePlayers(List<Player> players) {
        if(players.equals(this.players)) return;

        for(Player player : this.players) {
            if(!players.contains(player)) removePlayer(player);
        }

        this.players = players;
        sendToPlayers();
    }

    public void sendToPlayers() {
        for(Player player : players) {
            Audience audience = Thepit.adventure.player(player);
            audience.showBossBar(bossBar);
        }
    }

    public void remove() {
        for(Player player : players) {
            Audience audience = Thepit.adventure.player(player);
            audience.hideBossBar(bossBar);
        }
    }

    public void removePlayer(Player player) {
        Audience audience = Thepit.adventure.player(player);
        audience.hideBossBar(bossBar);
    }
}
