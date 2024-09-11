package me.wisdom.thepit.inventories;

import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.entity.Player;

public class PotionMasterGUI extends AGUI {
    public PotionMasterPanel potionMasterPanel;

    public PotionMasterGUI(Player player) {
        super(player);
        potionMasterPanel = new PotionMasterPanel(this);

        setHomePanel(potionMasterPanel);
    }
}
