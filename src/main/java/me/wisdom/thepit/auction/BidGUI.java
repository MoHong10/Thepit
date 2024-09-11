package me.wisdom.thepit.auction;

import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.entity.Player;

public class BidGUI extends AGUI {

    public BidPanel bidPanel;

    public BidGUI(Player player, int slot) {
        super(player);

        bidPanel = new BidPanel(this, slot);
        setHomePanel(bidPanel);

    }

}
