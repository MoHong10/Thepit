package me.wisdom.thepit.market;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ConfirmPurchasePanel extends AGUIPanel {
    public MarketListing listing;
    public int price;
    public boolean bin;
    public int amount;

    public ConfirmPurchasePanel(AGUI gui, MarketListing listing, int price, boolean bin, int amount) {
        super(gui);

        this.listing = listing;
        this.price = price;
        this.bin = bin;
        this.amount = amount;

        AItemStackBuilder confirmBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 5)
                .setName("&a&l确认 " + (bin ? "立即购买" : "出价"))
                .setLore(new ALoreBuilder(
                        "&7购买: " + listing.itemData.getItemMeta().getDisplayName() + (bin ? " &8x" + amount : ""),
                        "&7价格: &f" + price + " 灵魂", "",
                        "&e点击确认购买!"
                ));
        getInventory().setItem(11, confirmBuilder.getItemStack());

        int soulsToTake = price - listing.bidMap.getOrDefault(player.getUniqueId(), 0);

        AItemStackBuilder cancelBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 14)
                .setName("&c&l取消")
                .setLore(new ALoreBuilder(
                        "&7购买: " + listing.itemData.getItemMeta().getDisplayName() + (bin ? " &8x" + amount : ""),
                        "&7价格: &f" + soulsToTake + " 灵魂", "",
                        "&e点击取消购买!"
                ));
        getInventory().setItem(15, cancelBuilder.getItemStack());
    }

    @Override
    public String getName() {
        return "确认购买？";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        MarketAsyncTask.MarketTask task = bin ? MarketAsyncTask.MarketTask.BIN_ITEM : MarketAsyncTask.MarketTask.PLACE_BID;

        String failMessage = "&c处理交易时发生错误。请稍后再试。";

        BukkitRunnable bid = new BukkitRunnable() {
            @Override
            public void run() {
                AOutput.send(player, "&a&l市场!&7 出价成功!");
                Sounds.SUCCESS.play(player);
                int soulsToTake = price - listing.bidMap.getOrDefault(player.getUniqueId(), 0);

                PitPlayer.getPitPlayer(player).taintedSouls -= soulsToTake;
                player.closeInventory();
            }
        };

        BukkitRunnable buy = new BukkitRunnable() {
            @Override
            public void run() {
                AOutput.send(player, "&a&l市场!&7 购买了 " + listing.itemData.getItemMeta().getDisplayName() + (amount > 1 ? " &8x" + amount : ""));
                PitPlayer.getPitPlayer(player).taintedSouls -= price;

                ItemStack item = listing.itemData.clone();
                item.setAmount(amount);

                AUtil.giveItemSafely(player, item, true);
                Sounds.SUCCESS.play(player);
                player.closeInventory();
            }
        };


        if(event.getSlot() == 11) {
            new MarketAsyncTask(task, listing.marketUUID, player, (bin ? amount : price), bin ? buy : bid, MarketAsyncTask.getDefaultFail(player, failMessage));
        }

        if(event.getSlot() == 15) openPreviousGUI();
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
