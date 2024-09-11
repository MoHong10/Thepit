package me.wisdom.thepit.npcs;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitNPC;
import me.wisdom.thepit.market.MarketGUI;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerMarketNPC extends PitNPC {

    public PlayerMarketNPC(List<World> worlds) {
        super(worlds);
    }

    @Override
    public Location getRawLocation() {
        return new Location(MapManager.getDarkzone(), 204, 91, -84.7, 180, 0);
    }

    @Override
    public void createNPC(Location location) {
        spawnPlayerNPC("", "Banker", location, false);
    }

    @Override
    public void onClick(Player player) {

        if(!Thepit.MARKET_ENABLED) {
            AOutput.error(player, "&a&l市场！&c 玩家市场当前已禁用!");
            Sounds.NO.play(player);
            return;
        }

        MarketGUI marketGUI = new MarketGUI(player);
        marketGUI.open();
    }
}
