package me.wisdom.thepit.npcs;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitNPC;
import me.wisdom.thepit.darkzone.FastTravelGUI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class AuctioneerNPC extends PitNPC {

    public AuctioneerNPC(List<World> worlds) {
        super(worlds);
    }

    @Override
    public Location getRawLocation() {
        return new Location(MapManager.getDarkzone(), 247.5, 91, 8.5, 145, 0);
    }

    @Override
    public void createNPC(Location location) {
        spawnPlayerNPC("&8&l可疑人物", "Itz_Aethan", location, true);
    }

    @Override
    public void onClick(Player player) {
        FastTravelGUI fastTravelGUI = new FastTravelGUI(player);
        fastTravelGUI.open();
    }
}
