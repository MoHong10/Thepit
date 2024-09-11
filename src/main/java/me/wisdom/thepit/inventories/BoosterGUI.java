package me.wisdom.thepit.inventories;

import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.entity.Player;

public class BoosterGUI extends AGUI {

    public BoosterPanel boosterPanel;

    public BoosterGUI(Player player) {
        super(player);

        boosterPanel = new BoosterPanel(this);
        setHomePanel(boosterPanel);

    }

}
