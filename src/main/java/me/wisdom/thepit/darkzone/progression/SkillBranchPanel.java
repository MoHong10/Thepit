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

public class SkillBranchPanel extends AGUIPanel {
    public ProgressionGUI progressionGUI;
    public PitPlayer pitPlayer;
    public SkillBranch skillBranch;

    public SkillBranchPanel(AGUI gui, SkillBranch skillBranch) {
        super(gui, true);
        this.progressionGUI = (ProgressionGUI) gui;
        this.pitPlayer = progressionGUI.pitPlayer;
        this.skillBranch = skillBranch;

        buildInventory();

        inventoryBuilder.setSlots(Material.STAINED_GLASS_PANE, 15, 37, 38, 39, 40, 41, 42, 43);
        getInventory().setItem(36, ProgressionGUI.backItem);
        getInventory().setItem(44, HelpItemStacks.getProgressionStack());

        setInventory();
    }

    @Override
    public String getName() {
        String name = ChatColor.translateAlternateColorCodes('&', skillBranch.getDisplayName());
        name = ChatColor.getLastColors(name) + "&l" + "Skill: " + ChatColor.stripColor(name);
        return ChatColor.translateAlternateColorCodes('&', name);
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory().getHolder() != this) return;
        int slot = event.getSlot();

        if(slot == 9 || slot == 17 || slot == 4 || slot == 22) {
            SkillBranch.MajorProgressionUnlock unlock;
            if(slot == 9) unlock = skillBranch.firstUnlock;
            else if(slot == 17) unlock = skillBranch.lastUnlock;
            else if(slot == 4) unlock = skillBranch.firstPathUnlock;
            else unlock = skillBranch.secondPathUnlock;

            UnlockState state = ProgressionManager.getUnlockState(pitPlayer, unlock);
            if(state == UnlockState.LOCKED) {
                Sounds.NO.play(player);
            } else if(state == UnlockState.NEXT_TO_UNLOCK) {
                int cost = ProgressionManager.getInitialSoulCost(unlock);
                if(pitPlayer.taintedSouls < cost) {
                    Lang.NOT_ENOUGH_SOULS.send(player);
                    return;
                }
                ProgressionManager.unlock(this, pitPlayer, unlock, cost);
                setInventory();
            }
        } else if((slot >= 1 && slot <= 7) || (slot >= 19 && slot <= 25)) {
            SkillBranch.Path unlock = slot / 18 == 0 ? skillBranch.firstPath : skillBranch.secondPath;
            int level = slot % 18;
            if(slot % 18 > 3) level--;

            UnlockState state = ProgressionManager.getUnlockState(pitPlayer, unlock, level);
            if(state == UnlockState.LOCKED) {
                Sounds.NO.play(player);
            } else if(state == UnlockState.NEXT_TO_UNLOCK) {
                int cost = ProgressionManager.getInitialSoulCost(unlock, level);
                if(pitPlayer.taintedSouls < cost) {
                    Lang.NOT_ENOUGH_SOULS.send(player);
                    return;
                }
                ProgressionManager.unlockNext(this, pitPlayer, unlock, cost);
                setInventory();
            }
        } else if(slot == 36) {
            openPreviousGUI();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {}

    @Override
    public void onClose(InventoryCloseEvent event) {}

    public void setInventory() {
        getInventory().setItem(9, skillBranch.firstUnlock.getDisplayStack(pitPlayer));
        getInventory().setItem(17, skillBranch.lastUnlock.getDisplayStack(pitPlayer));
        getInventory().setItem(4, skillBranch.firstPathUnlock.getDisplayStack(pitPlayer));
        getInventory().setItem(22, skillBranch.secondPathUnlock.getDisplayStack(pitPlayer));
        getInventory().setItem(43, progressionGUI.createSoulsDisplay());
        for(int i = 0; i < 6; i++) {
            int firstPathSlot = i + 1;
            int secondPathSlot = i + 19;
            if(i >= 3) {
                firstPathSlot++;
                secondPathSlot++;
            }
            getInventory().setItem(firstPathSlot, skillBranch.firstPath.getDisplayStack(pitPlayer, i + 1));
            getInventory().setItem(secondPathSlot, skillBranch.secondPath.getDisplayStack(pitPlayer, i + 1));
        }
        updateInventory();
    }
}
