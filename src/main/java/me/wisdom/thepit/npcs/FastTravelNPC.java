package me.wisdom.thepit.npcs;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitNPC;
import me.wisdom.thepit.darkzone.FastTravelGUI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class FastTravelNPC extends PitNPC {

    public FastTravelNPC(List<World> worlds) {
        super(worlds);
    }

    @Override
    public Location getRawLocation() {
        return new Location(MapManager.getDarkzone(), 182.5, 91, -88.5, 170, 0);
    }

    @Override
    public void createNPC(Location location) {
        spawnPlayerNPC("&f&l快速移动", "Mailman", location, false);
    }

    @Override
    public void onClick(Player player) {
        FastTravelGUI gui = new FastTravelGUI(player);
        gui.open();
    }
}
