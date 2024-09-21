package me.wisdom.thepit.inventories;

import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.objects.PitPerk;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class PerkGUI extends AGUI {

    public PerkPanel perkPanel;
    public SelectPerkPanel selectPerkPanel;
    public MegastreakPanel megastreakPanel;
    public KillstreakPanel killstreakPanel;

    public int killstreakSlot = 0;

    public PerkGUI(Player player) {
        super(player);

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        for(PitPlayer.MegastreakLimit cooldown : pitPlayer.getAllCooldowns()) cooldown.attemptReset(pitPlayer);

        perkPanel = new PerkPanel(this);
        setHomePanel(perkPanel);
        selectPerkPanel = new SelectPerkPanel(this);
        megastreakPanel = new MegastreakPanel(this);
        killstreakPanel = new KillstreakPanel(this);
    }

    public int getSlot(int perkNum) {

        return perkNum * 2 + 8;
    }

    public int getPerkNum(int slot) {

        return (slot - 8) / 2;
    }

    public PitPerk getActivePerk(int perkNum) {

        return getActivePerks().get(perkNum - 1);
    }

    public List<PitPerk> getActivePerks() {

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        return pitPlayer.pitPerks;
    }

    public void setPerk(PitPerk pitPerk, int perkNum) {
        if(NonManager.getNon(player) != null) return;
        getActivePerks().set(perkNum - 1, pitPerk);
    }

    public boolean isActive(PitPerk pitPerk) {

        for(PitPerk activePerk : getActivePerks()) {

            if(activePerk == pitPerk) return true;
        }

        return false;
    }
}
