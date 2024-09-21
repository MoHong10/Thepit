package me.wisdom.thepit.events;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.storage.StorageProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;

public class PitJoinEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final PlayerJoinEvent event;
    private final Player player;
    private final PitPlayer pitPlayer;
    private final StorageProfile profile;


    public PitJoinEvent(PlayerJoinEvent event, Player player, PitPlayer pitPlayer, StorageProfile profile) {
        this.event = event;
        this.player = player;
        this.pitPlayer = pitPlayer;
        this.profile = profile;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PlayerJoinEvent getEvent() {
        return event;
    }

    public Player getPlayer() {
        return player;
    }

    public PitPlayer getPitPlayer() {
        return pitPlayer;
    }

    public StorageProfile getProfile() {
        return profile;
    }
}
