package me.wisdom.thepit.controllers;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitNPC;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.*;

public class NPCManager implements Listener {
    public static List<PitNPC> pitNPCs = new ArrayList<>();

    public static void onDisable() {
        for(PitNPC pitNPC : pitNPCs) pitNPC.remove();
    }

    public static void registerNPC(PitNPC pitNPC) {
        pitNPCs.add(pitNPC);
        Bukkit.getPluginManager().registerEvents(pitNPC, Thepit.INSTANCE);
    }
}
