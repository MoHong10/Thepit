package me.wisdom.thepit.npcs;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitNPC;
import me.wisdom.thepit.inventories.stats.StatGUI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class StatsNPC extends PitNPC {

    public StatsNPC(List<World> worlds) {
        super(worlds);
    }

    @Override
    public Location getRawLocation() {
        return null;
    }

    @Override
    public Location getFinalLocation(World world) {
        return MapManager.currentMap.getStatsNPCSpawn();
    }

    @Override
    public void createNPC(Location location) {
        spawnPlayerNPC("&e&lLB 和统计数据", "Whyplay", location, false);}

    @Override
    public void onClick(Player player) {
        StatGUI statGUI = new StatGUI(player);
        statGUI.open();
    }
}
