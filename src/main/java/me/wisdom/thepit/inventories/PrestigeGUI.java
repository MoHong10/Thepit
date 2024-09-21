package me.wisdom.thepit.inventories;

import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.entity.Player;

public class PrestigeGUI extends AGUI {

    public PrestigePanel prestigePanel;
    public PrestigeConfirmPanel prestigeConfirmPanel;

    public PrestigeGUI(Player player) {
        super(player);

        prestigePanel = new PrestigePanel(this);
        prestigeConfirmPanel = new PrestigeConfirmPanel(this);
        setHomePanel(prestigePanel);
    }

}
