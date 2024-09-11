package me.wisdom.thepit.battlepass.inventories;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.entity.Player;

public class PassGUI extends AGUI {
    public PitPlayer pitPlayer;

    public PassPanel passPanel;
    public QuestPanel questPanel;

    public PassGUI(Player player) {
        super(player);
        this.pitPlayer = PitPlayer.getPitPlayer(player);

        passPanel = new PassPanel(this);
        questPanel = new QuestPanel(this);
        setHomePanel(passPanel);
    }
}