package me.wisdom.thepit.market;

import me.wisdom.thepit.Thepit;
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
import org.bukkit.scheduler.BukkitRunnable;

public class ConfirmDeletionPanel extends AGUIPanel {
    public MarketListing listing;

    public ConfirmDeletionPanel(AGUI gui, MarketListing listing) {
        super(gui);

        this.listing = listing;

        // 创建确认删除按钮
        AItemStackBuilder confirmBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 5)
                .setName("&a&l确认删除")
                .setLore(new ALoreBuilder(
                        "&7此列表将被移除。",
                        "&7剩余的物品将通过 &f\"您的领取\" &7菜单返回给您。",
                        "",
                        "&e点击以确认删除!"
                ));
        getInventory().setItem(11, confirmBuilder.getItemStack());

        // 创建取消按钮
        AItemStackBuilder cancelBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 14)
                .setName("&c&l取消")
                .setLore(new ALoreBuilder(
                        "&7返回到上一个菜单。",
                        "",
                        "&e点击以取消删除!"
                ));
        getInventory().setItem(15, cancelBuilder.getItemStack());
    }

    @Override
    public String getName() {
        return "确认删除？";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        MarketAsyncTask.MarketTask task = MarketAsyncTask.MarketTask.REMOVE_LISTING;

        BukkitRunnable delete = new BukkitRunnable() {
            @Override
            public void run() {
                AOutput.send(player, "&a&l市场！&7 列表成功删除！");
                Sounds.SUCCESS.play(player);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        YourListingsPanel panel = ((MarketGUI) gui).yourListingsPanel;
                        openPanel(panel);
                        panel.placeClaimables();
                        panel.placeListings();
                    }
                }.runTask(Thepit.INSTANCE);
            }
        };

        if (event.getSlot() == 11) {
            String failMessage = "&c尝试删除您的列表时发生错误。请联系工作人员处理此问题。";
            new MarketAsyncTask(task, listing.marketUUID, player, 0, delete, MarketAsyncTask.getDefaultFail(player, failMessage));
        }

        if (event.getSlot() == 15) openPreviousGUI();
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
