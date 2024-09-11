package me.wisdom.thepit.inventories.help;

import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.entity.Player;

public class KitGUI extends AGUI {
    public KitPanel kitPanel;

    public KitGUI(Player player) {
        super(player);

        this.kitPanel = new KitPanel(this);
//		setHomePanel(kitPanel);
    }
}
