package me.wisdom.thepit.npcs;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitNPC;
import me.wisdom.thepit.inventories.TaintedShopGUI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class TaintedShopNPC extends PitNPC {

    public TaintedShopNPC(List<World> worlds) {
        super(worlds);
    }

    @Override
    public Location getRawLocation() {
        return new Location(MapManager.getDarkzone(), 201, 91, -84.7, 180, 0);
    }

    @Override
    public void createNPC(Location location) {
        spawnPlayerNPC("", "merchant", location, false);
    }

    @Override
    public void onClick(Player player) {
        TaintedShopGUI taintedShopGUI = new TaintedShopGUI(player);
        taintedShopGUI.open();
    }
}