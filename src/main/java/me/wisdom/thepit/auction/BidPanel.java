package me.wisdom.thepit.auction;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ItemType;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.misc.packets.SignPrompt;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
public class BidPanel extends AGUIPanel {
    PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
    public BidGUI bidGUI;
    public int slot;
    public BukkitTask runnable;

    public BidPanel(AGUI gui, int slot) {
        super(gui);
        bidGUI = (BidGUI) gui;
        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 8);
        this.slot = slot;
    }

    @Override
    public String getName() {
        return "出价";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        if(event.getClickedInventory().getHolder() == this) {
            if(slot != 16) return;

            AuctionItem auctionItem = AuctionManager.auctionItems[this.slot];

            if(auctionItem.getHighestBidder() != null && auctionItem.getHighestBidder().equals(player.getUniqueId())) {
                Sounds.NO.play(player);
                AOutput.error(player, "&c&l错误!&7 你已经是出价最高的了!");
            } else if(pitPlayer.taintedSouls < minBid(auctionItem) - auctionItem.getBid(player.getUniqueId())) {
                Sounds.NO.play(player);
                AOutput.error(player, "&c&l错误!&7 你的灵魂不足!");
            } else {
                if(event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {
                    SignPrompt.promptPlayer(player, "", "^^^^^^", "出价", "最少: " + minBid(auctionItem), input -> {
                        int bid;
                        try {
                            bid = Integer.parseInt(input.replaceAll("\"", ""));
                            if(bid < minBid(auctionItem)) throw new Exception();
                        } catch(Exception ignored) {
                            AOutput.error(player, "&c&l错误!&7 无效");
                            return;
                        }

                        if(pitPlayer.taintedSouls < bid - auctionItem.getBid(player.getUniqueId())) {
                            Sounds.NO.play(player);
                            AOutput.error(player, "&c&l错误!&7 你的灵魂不足!");
                            return;
                        }

                        bid(auctionItem, bid);
                    });
                } else {
                    bid(auctionItem, minBid(auctionItem));
                }
            }
        }
    }

    public void bid(AuctionItem auctionItem, int bid) {
        auctionItem.addBid(player, bid);
        player.closeInventory();
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                AuctionItem auctionItem = AuctionManager.auctionItems[slot];
                ItemStack auctionStack = auctionItem.item.item.clone();
                ItemStack item = MysticFactory.isFresh(auctionStack) ? ItemType.getJewelItem(auctionItem.item.id, auctionItem.itemData) : auctionItem.item.item.clone();

                AItemStackBuilder itemBuilder = new AItemStackBuilder(item);
                itemBuilder.setName(auctionItem.item.itemName);
                if(auctionItem.itemData == 0) itemBuilder.setLore(auctionItem.item.item.getItemMeta().getLore());

                getInventory().setItem(13, itemBuilder.getItemStack());

                ALoreBuilder loreBuilder = new ALoreBuilder();
                if(auctionItem.bidMap.size() == 0) loreBuilder.addLore("&7尚未有人出价!"); else loreBuilder.addLore("");
                loreBuilder.addLore();

                for(Map.Entry<UUID, Integer> entry : auctionItem.bidMap.entrySet()) {
                    loreBuilder.addLore("&6" + Bukkit.getOfflinePlayer(entry.getKey()).getName() + "&f " +
                            entry.getValue() + " 灵魂");
                }

                AItemStackBuilder bidsBuilder = new AItemStackBuilder(Material.MAP)
                        .setName("&e当前出价")
                        .setLore(loreBuilder);
                getInventory().setItem(10, bidsBuilder.getItemStack());

                loreBuilder = new ALoreBuilder("");
                if(auctionItem.getHighestBidder() != null)
                    loreBuilder.addLore("&7最高出价: &f" + auctionItem.getHighestBid() + " 灵魂");
                else
                    loreBuilder.addLore("&7开始出价: &f" + auctionItem.getHighestBid() + " 灵魂");
                loreBuilder.addLore(
                        "&7你的出价: &f" + auctionItem.getBid(player.getUniqueId()) + " 灵魂",
                        "",
                        "&7你的灵魂: &f" + pitPlayer.taintedSouls,
                        ""
                );
                if(AuctionManager.haveAuctionsEnded()) {
                    loreBuilder.addLore("&e即将结束");
                } else loreBuilder.addLore("&e" + AuctionManager.getRemainingTime() + " 剩余");
                loreBuilder.addLore("");
                if(auctionItem.getHighestBidder() != null && auctionItem.getHighestBidder().equals(player.getUniqueId())) {
                    loreBuilder.addLore("&a你已经有了最高出价");
                } else if(pitPlayer.taintedSouls < minBid(auctionItem) - auctionItem.getBid(player.getUniqueId())) {
                    loreBuilder.addLore("&c你的灵魂不足!");
                } else {
                    loreBuilder.addLore("&e左键出价 &f" + (minBid(auctionItem)) + " 灵魂" + "&e!");
                    loreBuilder.addLore("&e右键自定义出价金额!");
                }
                AItemStackBuilder placeBidBuilder = new AItemStackBuilder(Material.GHAST_TEAR)
                        .setName("&e出价")
                        .setLore(loreBuilder);

                getInventory().setItem(16, placeBidBuilder.getItemStack());

            }
        }.runTaskTimer(Thepit.INSTANCE, 0, 10);
    }

    public static int minBid(AuctionItem item) {
        int currentBid = item.getHighestBid();

        if(item.getHighestBidder() == null) return currentBid;

        int bid = (int) Math.ceil(currentBid + (currentBid * 0.1));
        return Math.max(1, bid);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        runnable.cancel();
    }

}
