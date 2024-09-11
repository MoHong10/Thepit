package me.wisdom.thepit.controllers.objects;

import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.gui.AGUIPanel;
import org.bukkit.entity.Player;

public abstract class UnlockableRenownUpgrade extends RenownUpgrade {

    public UnlockableRenownUpgrade(String name, String refName, int prestigeReq) {
        super(name, refName, prestigeReq);
    }

    public UnlockableRenownUpgrade(String name, String refName, int prestigeReq, Class<? extends AGUIPanel> subPanel) {
        super(name, refName, prestigeReq, subPanel);
    }

    public abstract String getEffect();
    public abstract int getUnlockCost();

    public void addBaseDescription(PitLoreBuilder loreBuilder, Player player) {
        loreBuilder.addLongLine(getEffect());
    }
}
