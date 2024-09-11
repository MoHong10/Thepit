package me.wisdom.thepit.inventories;

import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.entity.Player;

public class KeeperGUI extends AGUI {

    public KeeperPanel keeperPanel;

    public KeeperGUI(Player player) {
        super(player);

        keeperPanel = new KeeperPanel(this);
        setHomePanel(keeperPanel);
    }
}
