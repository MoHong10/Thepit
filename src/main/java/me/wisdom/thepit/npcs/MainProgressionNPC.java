package me.wisdom.thepit.npcs;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitNPC;
import me.wisdom.thepit.darkzone.progression.ProgressionGUI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class MainProgressionNPC extends PitNPC {

    public MainProgressionNPC(List<World> worlds) {
        super(worlds);
    }

    @Override
    public Location getRawLocation() {
        return new Location(MapManager.getDarkzone(), 188.5, 91, -84.7, 180F, 0);
    }

    @Override
    public void createNPC(Location location) {
        spawnPlayerNPC("", "debrided", location, false);
    }

    @Override
    public void onClick(Player player) {
        ProgressionGUI progressionGUI = new ProgressionGUI(player);
        progressionGUI.open();
    }
}
