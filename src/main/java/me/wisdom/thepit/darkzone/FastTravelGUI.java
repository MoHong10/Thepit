package me.wisdom.thepit.darkzone;

import me.wisdom.thepit.darkzone.abilities.FastTravelPanel;
import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.entity.Player;

public class FastTravelGUI extends AGUI {

    public FastTravelPanel fastTravelPanel;
    public FastTravelGUI(Player player) {
        super(player);

        fastTravelPanel = new FastTravelPanel(this);

        setHomePanel(fastTravelPanel);
    }
}
