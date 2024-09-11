package me.wisdom.thepit.inventories.trash;

import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.entity.Player;

public class TrashGUI extends AGUI {
    public TrashPanel trashPanel;

    public TrashGUI(Player player) {
        super(player);

        trashPanel = new TrashPanel(this);
        setHomePanel(trashPanel);
    }
}
