package me.wisdom.thepit.darkzone.altar;

import me.wisdom.thepit.darkzone.altar.pedestals.AltarPanel;
import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.entity.Player;

public class AltarGUI extends AGUI {

    public AltarPanel altarPanel;

    public AltarGUI(Player player) {
        super(player);

        altarPanel = new AltarPanel(this);
        setHomePanel(altarPanel);
    }
}
