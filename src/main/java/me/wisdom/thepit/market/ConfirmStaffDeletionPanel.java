package me.wisdom.thepit.market;

import me.wisdom.thepit.Thepit;
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
import org.bukkit.scheduler.BukkitRunnable;

public class ConfirmStaffDeletionPanel extends AGUIPanel {
    public MarketListing listing;

    public ConfirmStaffDeletionPanel(AGUI gui, MarketListing listing) {
        super(gui);

        this.listing = listing;

        // 确认删除按钮的构建
        AItemStackBuilder confirmBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 5)
                .setName("&a&l确认删除")
                .setLore(new ALoreBuilder(
                        "&7此列表将被删除。",
                        "&7剩余的物品将通过 &f\"你的索赔\" &7菜单返回给你。",
                        "",
                        "&e点击以确认删除！"
                ));
        getInventory().setItem(11, confirmBuilder.getItemStack());

        // 取消按钮的构建
        AItemStackBuilder cancelBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 14)
                .setName("&c&l取消")
                .setLore(new ALoreBuilder(
                        "&7返回到上一菜单。",
                        "",
                        "&e点击以取消删除！"
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
        MarketAsyncTask.MarketTask task = MarketAsyncTask.MarketTask.STAFF_REMOVE;

        BukkitRunnable delete = new BukkitRunnable() {
            @Override
            public void run() {
                AOutput.send(player, "&a&l市场！&7 列表已成功删除！");
                Sounds.SUCCESS.play(player);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (listing.startingBid != -1) {
                            YourListingsPanel panel = ((MarketGUI) gui).yourListingsPanel;
                            openPanel(panel);
                            panel.placeClaimables();
                            panel.placeListings();
                            return;
                        }

                        AUtil.giveItemSafely(player, listing.itemData);
                        player.closeInventory();
                    }
                }.runTask(Thepit.INSTANCE);
            }
        };

        if (event.getSlot() == 11) {
            String failMessage = "&c删除列表时发生错误。请检查控制台以获取可能的错误。";
            new MarketAsyncTask(task, listing.marketUUID, player, 0, delete, MarketAsyncTask.getDefaultFail(player, failMessage));
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
