package me.wisdom.thepit.inventories;

import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.entity.Player;

public class VileGUI extends AGUI {

    public VilePanel vilePanel;

    public VileGUI(Player player) {
        super(player);

        vilePanel = new VilePanel(this);
        setHomePanel(vilePanel);
    }

}
