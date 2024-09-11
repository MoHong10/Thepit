package me.wisdom.thepit.inventories;

import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.entity.Player;

public class GemGUI extends AGUI {
    public TotallyLegitGemPanel totallyLegitGemPanel;

    public GemGUI(Player player) {
        super(player);

        totallyLegitGemPanel = new TotallyLegitGemPanel(this);
        setHomePanel(totallyLegitGemPanel);
    }
}
