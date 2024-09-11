package me.wisdom.thepit.market;

import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MarketSelectionPanel extends AGUIPanel {
    public MarketSelectionPanel(AGUI gui) {
        super(gui);

        AItemStackBuilder listingsBuilder = new AItemStackBuilder(Material.BOOK)
                .setName("&e查看市场列表")
                .setLore(new ALoreBuilder(
                        "&7查看玩家市场上",
                        "&7当前出售的所有物品。",
                        "",
                        "&e点击查看列表"
                ));
        getInventory().setItem(11, listingsBuilder.getItemStack());

        AItemStackBuilder createBuilder = new AItemStackBuilder(Material.GOLD_BARDING)
                .setName(canCreateListing() ? "&e创建新列表" : "&c创建新列表")
                .setLore(new ALoreBuilder(
                        "&7在玩家市场上创建一个新列表。",
                        "&7当前列表数: " + (canCreateListing() ? "&e" : "&c") + MarketManager.getActiveListings(player.getUniqueId()).size() + "&f/" + MarketManager.ListingLimit.getRank(player).limit,
                        "",
                        canCreateListing() ? "&e点击创建新列表" : "&c无法创建更多列表！"
                ));
        getInventory().setItem(15, createBuilder.getItemStack());

        AItemStackBuilder yourBuilder = new AItemStackBuilder(Material.GHAST_TEAR)
                .setName("&e你的列表")
                .setLore(new ALoreBuilder(
                        "&7查看你所有的",
                        "&7当前列表，并",
                        "&7领取任何已结束列表中的物品或灵魂。",
                        "",
                        "&e点击查看列表"
                ));
        getInventory().setItem(13, yourBuilder.getItemStack());

    }

    public boolean canCreateListing() {
        return MarketManager.getActiveListings(player.getUniqueId()).size() < MarketManager.ListingLimit.getRank(player).limit;
    }

    @Override
    public String getName() {
        return "玩家市场";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(!event.getClickedInventory().equals(getInventory())) return;
        int slot = event.getSlot();

        if(slot == 11) {
            openPanel(((MarketGUI) gui).marketPanel);
        }

        if(slot == 15) {
            if(!canCreateListing()) {
                AOutput.error(player, "&c通过从 &f&nstore.pitsim.net 获得的排名，获得更高的发布限额");
                Sounds.NO.play(player);
                return;
            }
            openPanel(((MarketGUI) gui).createListingPanel);
        }

        if(slot == 13) {
            openPanel(((MarketGUI) gui).yourListingsPanel);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
