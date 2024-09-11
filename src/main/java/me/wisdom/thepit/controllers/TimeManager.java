package me.wisdom.thepit.controllers;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.PitCalendarEvent;
import me.wisdom.thepit.events.PitJoinEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class TimeManager implements Listener {
    private static final Map<PitCalendarEvent, Boolean> activeEventsMap = new HashMap<>();

    static {
        for(PitCalendarEvent calendarEvent : PitCalendarEvent.values())
            activeEventsMap.put(calendarEvent, calendarEvent.isCurrentlyActive());
    }

    public static boolean isEventActive(PitCalendarEvent calendarEvent) {
        return activeEventsMap.get(calendarEvent);
    }

    @EventHandler
    public void onJoin(PitJoinEvent event) {
        if(!isEventActive(PitCalendarEvent.HALLOWEEN)) return;
        Player player = event.getPlayer();
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(pitPlayer.prestige < 5) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                Misc.sendTitle(player, "&5&l快乐的 &6&l万圣节!", 40);
                Misc.sendSubTitle(player, "&72x &fsouls&7 来自 &5黑暗地带&7!", 40);
                AOutput.send(player, "&5&l快乐的 &6&l万圣节!&7 2x &5灵魂&7 来自黑暗地带!");
            }
        }.runTaskLater(Thepit.INSTANCE, 1L);
    }

    //	TODO: No longer does anything, needs to be re-added to code
    public static double getHalloweenSoulMultiplier() {
        return isEventActive(PitCalendarEvent.HALLOWEEN) ? 2 : 1;
    }
}
