package me.wisdom.thepit.inventories;

import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.controllers.objects.RenownUpgrade;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIManager;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.gui.APagedGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class RenownShopPanel extends APagedGUIPanel {
    public PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
    public RenownShopGUI renownShopGUI;

    public RenownShopPanel(AGUI gui) {
        super(gui);
        renownShopGUI = (RenownShopGUI) gui;
        addBackButton(addTaggedItem(getRows() * 9 - 5, AGUIManager::getBackItemStack, event -> new PrestigeGUI(player).open()));
        buildInventory();
    }

    @Override
    public String getName() {
        return "&eRenown Shop";
    }

    @Override
    public void addItems() {
        for(RenownUpgrade upgrade : UpgradeManager.upgrades) addItem(() -> upgrade.getDisplayStack(player), event -> {
            boolean hasUpgrade = UpgradeManager.hasUpgrade(player, upgrade);
            boolean isTiered = upgrade.isTiered();
            boolean isMaxed = UpgradeManager.isMaxed(player, upgrade);
            int renownCost = isMaxed ? -1 : UpgradeManager.getNextCost(player, upgrade);
            AGUIPanel subPanel = renownShopGUI.getSubPanel(upgrade);

            if(upgrade.prestigeReq > pitPlayer.prestige) {
                AOutput.error(player, "&c&lERROR!&7 You need to have prestige &e" +
                        AUtil.toRoman(upgrade.prestigeReq) + " &7to acquire this!");
                Sounds.NO.play(player);
                return;
            }

            if(hasUpgrade && subPanel != null) {
                openPanel(renownShopGUI.getSubPanel(upgrade));
                return;
            }

            if(isMaxed) {
                if(isTiered) {
                    AOutput.error(player, "&a&lMAXED!&7 You already unlocked the last upgrade!");
                } else {
                    AOutput.error(player, "&a&lMAXED!&7 You already unlocked this upgrade!");
                }
                Sounds.NO.play(player);
                return;
            }

            if(renownCost > pitPlayer.renown) {
                AOutput.error(player, "&c&lERROR!&7 You do not have enough renown!");
                Sounds.NO.play(player);
                return;
            }

            RenownShopGUI.purchaseConfirmations.put(player, upgrade);
            openPanel(renownShopGUI.renownShopConfirmPanel);
        });
    }

    @Override
    public void setInventory() {
        super.setInventory();
        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 7, false);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        setInventory();
    }

    @Override
    public void onClose(InventoryCloseEvent event) {}
}
