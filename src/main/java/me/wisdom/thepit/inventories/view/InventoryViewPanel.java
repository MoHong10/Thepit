package me.wisdom.thepit.inventories.view;

import me.wisdom.thepit.storage.StorageProfile;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryViewPanel extends AGUIPanel {
    public ViewGUI viewGUI;



    public InventoryViewPanel(AGUI gui) {
        super(gui, true);
        viewGUI = (ViewGUI) gui;
        buildInventory();

        StorageProfile target = viewGUI.target;
        ItemStack[] inventory = target.getInventory();
        for(int i = 0; i < 36; i++) {
            ItemStack itemStack = inventory[i];
            getInventory().setItem(i, itemStack);
        }
    }

    @Override
    public String getName() {
        return (viewGUI.name + "'s Inventory");
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void onClick(InventoryClickEvent event) {}

    @Override
    public void onOpen(InventoryOpenEvent event) {
        viewGUI.playerClosed = true;
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }
}
