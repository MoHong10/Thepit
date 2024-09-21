package me.wisdom.thepit.inventories;

import me.wisdom.thepit.controllers.LevelManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PrestigeConfirmPanel extends AGUIPanel {
    public PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
    public PrestigeGUI prestigeGUI;

    public PrestigeConfirmPanel(AGUI gui) {
        super(gui);
        prestigeGUI = (PrestigeGUI) gui;
    }

    @Override
    public String getName() {
        return "&bAre you sure?";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        if(event.getClickedInventory().getHolder() == this) {
            if(slot == 11) {
                LevelManager.incrementPrestige(player);
                player.closeInventory();
            }
            if(slot == 15) {
                openPreviousGUI();
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

        ItemStack confirm = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
        ItemMeta confirmMeta = confirm.getItemMeta();
        List<String> confirmLore = new ArrayList<>();
        confirmMeta.setDisplayName(ChatColor.GREEN + "ARE YOU SURE?");
        confirmLore.add(ChatColor.translateAlternateColorCodes('&', "&7New prestige: &e" + AUtil.toRoman(pitPlayer.prestige + 1)));
        confirmLore.add("");
        confirmLore.add(ChatColor.translateAlternateColorCodes('&', "&c&lRESETTING LEVEL!"));
        confirmLore.add(ChatColor.translateAlternateColorCodes('&', "&c&lRESETTING LEVEL!"));
        confirmLore.add(ChatColor.translateAlternateColorCodes('&', "&c&lRESETTING UPGRADES!"));
        confirmLore.add("");
        confirmLore.add(ChatColor.YELLOW + "Click to prestige!");
        confirmMeta.setLore(confirmLore);
        confirm.setItemMeta(confirmMeta);

        getInventory().setItem(11, confirm);

        ItemStack cancel = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
        ItemMeta cancelMeta = confirm.getItemMeta();
        List<String> cancelLore = new ArrayList<>();
        cancelMeta.setDisplayName(ChatColor.RED + "CANCEL");
        cancelLore.add(ChatColor.GRAY + "Back to Prestige & Renown");
        cancelMeta.setLore(cancelLore);
        cancel.setItemMeta(cancelMeta);

        getInventory().setItem(15, cancel);

    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
