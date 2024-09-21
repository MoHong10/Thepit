package me.wisdom.thepit.darkzone.progression;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.UnlockState;
import me.wisdom.thepit.misc.Lang;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.tutorial.HelpItemStacks;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MainProgressionPanel extends AGUIPanel {
    public ProgressionGUI progressionGUI;
    public PitPlayer pitPlayer;

    public static final List<MainProgressionUnlock> tutorialUnlocks = new ArrayList<>();

    static {
        tutorialUnlocks.add(ProgressionManager.getMainProgressionUnlock(2, 3));
        tutorialUnlocks.add(ProgressionManager.getMainProgressionUnlock(2, 2));
    }

    public MainProgressionPanel(AGUI gui) {
        super(gui);
        this.progressionGUI = (ProgressionGUI) gui;
        this.pitPlayer = progressionGUI.pitPlayer;

        inventoryBuilder.setSlots(Material.STAINED_GLASS_PANE, 15, 45, 46, 47, 48, 49, 50, 51, 52);
        getInventory().setItem(53, HelpItemStacks.getMainProgressionStack());

        setInventory();
    }

    @Override
    public String getName() {
        return "" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Skill Tree";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory().getHolder() != this) return;
        int slot = event.getSlot();

        for(MainProgressionUnlock unlock : ProgressionManager.mainProgressionUnlocks) {
            if(slot != unlock.getSlot()) continue;
            UnlockState state = ProgressionManager.getUnlockState(pitPlayer, unlock);
            if(state == UnlockState.LOCKED) {
                Sounds.NO.play(player);
            } else if(state == UnlockState.NEXT_TO_UNLOCK) {
                int cost = ProgressionManager.getUnlockCost(pitPlayer, unlock);
                if(tutorialUnlocks.contains(unlock)) cost = 0;
                if(pitPlayer.taintedSouls < cost) {
                    Lang.NOT_ENOUGH_SOULS.send(player);
                    return;
                }
                ProgressionManager.unlock(this, pitPlayer, unlock, cost);
                setInventory();
            } else if(state == UnlockState.UNLOCKED) {
                if(unlock instanceof MainProgressionMajorUnlock) {
                    MainProgressionMajorUnlock majorUnlock = (MainProgressionMajorUnlock) unlock;
                    for(SkillBranchPanel skillBranchPanel : progressionGUI.skillBranchPanels) {
                        if(skillBranchPanel.skillBranch != majorUnlock.skillBranch) continue;
                        openPanel(skillBranchPanel);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {}

    @Override
    public void onClose(InventoryCloseEvent event) {}

    public void setInventory() {
        getInventory().setItem(52, progressionGUI.createSoulsDisplay());
        for(MainProgressionUnlock unlock : ProgressionManager.mainProgressionUnlocks) {
            UnlockState state = ProgressionManager.getUnlockState(pitPlayer, unlock);
            getInventory().setItem(unlock.getSlot(), unlock.getDisplayStack(pitPlayer, state));
        }
        updateInventory();
    }
}
