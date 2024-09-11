package me.wisdom.thepit.inventories;

import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.entity.Player;

public class TaintedShopGUI extends AGUI {

    ShopHomePanel homePanel;
    ShredPanel shredPanel;
    TaintedShopPanel shopPanel;

    public TaintedShopGUI(Player player) {
        super(player);

        homePanel = new ShopHomePanel(this);
        shredPanel = new ShredPanel(this);
        shopPanel = new TaintedShopPanel(this);

        setHomePanel(homePanel);
    }
}
