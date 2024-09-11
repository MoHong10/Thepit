package me.wisdom.thepit.npcs;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitNPC;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class ReyerticNPC extends PitNPC {

    public ReyerticNPC(List<World> worlds) {
        super(worlds);
    }

    @Override
    public Location getRawLocation() {
        return null;
    }

    @Override
    public Location getFinalLocation(World world) {
        return MapManager.currentMap.getReyNPCSpawn();
    }

    @Override
    public void createNPC(Location location) {
        spawnPlayerNPC("&9Reyertic", "Reyertic", location, true);
    }

    @Override
    public void onClick(Player player) {}
}
