package me.wisdom.thepit.inventories;

import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.entity.Player;

public class GodGUI extends AGUI {
    public GodPanel godPanel;

    public GodGUI(Player player) {
        super(player);

        godPanel = new GodPanel(this);
        setHomePanel(godPanel);
    }
}
