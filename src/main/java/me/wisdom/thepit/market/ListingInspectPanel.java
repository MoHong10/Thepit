package me.wisdom.thepit.market;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.misc.packets.SignPrompt;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ListingInspectPanel extends AGUIPanel {
    public MarketListing listing;
    public int bid;
    public int purchasing;
    public BukkitTask runnable;
    public boolean marketPanel;

    public ListingInspectPanel(AGUI gui, MarketListing listing, boolean marketPanel) {
        super(gui);
        this.listing = listing;
        bid = listing.getMinimumBid();
        purchasing = 1;
        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 15);
        this.marketPanel = marketPanel;

        calculateItems();
    }

    public void calculateItems() {
        if (PitPlayer.getPitPlayer(player) == null) return;

        // 更新展示的物品
        ItemStack listingStack = listing.getItemStack();
        ItemMeta listingMeta = listingStack.getItemMeta();
        List<String> listingLore = listingMeta.getLore();
        listingLore.subList(listingLore.size() - 2, listingLore.size()).clear();
        listingMeta.setLore(listingLore);
        listingStack.setItemMeta(listingMeta);
        getInventory().setItem(13, listingStack);

        // 更新拍卖项
        AItemStackBuilder auctionBuilder = new AItemStackBuilder(listing.startingBid != -1 ? Material.GOLD_BARDING : Material.BARRIER)
                .setName(listing.startingBid != -1 ? "&e参与竞标" : "&c无法参与竞标");

        ALoreBuilder loreBuilder = new ALoreBuilder();
        if (listing.startingBid != -1) {
            int soulsToTake = bid - listing.bidMap.getOrDefault(player.getUniqueId(), 0);

            loreBuilder.addLore((listing.bidMap.isEmpty() ? "&7起拍价: &f" : "&7最低竞标价: &f") + listing.getMinimumBid() + " 灵魂");
            loreBuilder.addLore("");
            if (listing.bidMap.containsKey(player.getUniqueId()))
                loreBuilder.addLore("&7你的竞标: &f" + listing.bidMap.get(player.getUniqueId()) + " 灵魂");
            else loreBuilder.addLore("&7你的竞标: &c无");
            loreBuilder.addLore("&7你的灵魂: &f" + PitPlayer.getPitPlayer(player).taintedSouls + " 灵魂");
            loreBuilder.addLore("");
            if (listing.ownerUUID.equals(player.getUniqueId())) {
                loreBuilder.addLore("&c你不能对自己的物品进行竞标！");
            } else if (listing.getHighestBidder() != null && listing.getHighestBidder().equals(player.getUniqueId())) {
                loreBuilder.addLore("&a你是最高竞标者！");
            } else if (PitPlayer.getPitPlayer(player).taintedSouls < soulsToTake) {
                loreBuilder.addLore("&c灵魂不足！");
            } else {
                loreBuilder.addLore("&e左键点击竞标 &f" + bid + " 灵魂");
                loreBuilder.addLore("&e右键点击输入自定义金额");
            }
        } else {
            loreBuilder.addLore("&7该物品未进行拍卖！");
        }

        auctionBuilder.setLore(loreBuilder);
        getInventory().setItem(11, auctionBuilder.getItemStack());

        // 更新一口价项
        AItemStackBuilder binBuilder = new AItemStackBuilder(listing.binPrice != -1 ? Material.NAME_TAG : Material.BARRIER)
                .setName(listing.binPrice != -1 ? "&e立即购买！" : "&c无法购买");

        ALoreBuilder binLoreBuilder = new ALoreBuilder();
        if (listing.binPrice != -1) {

            binLoreBuilder.addLore("&7一口价: &f" + listing.binPrice + " 灵魂" + (listing.stackBIN ? " &8（每个物品）" : ""));
            binLoreBuilder.addLore("&7你的灵魂: &f" + PitPlayer.getPitPlayer(player).taintedSouls + " 灵魂");
            binLoreBuilder.addLore("");
            if (listing.stackBIN) {
                binLoreBuilder.addLore("&7库存: &a" + listing.itemData.getAmount() + (listing.itemData.getAmount() == 1 ? " 件" : " 件"));
                binLoreBuilder.addLore("&7购买数量: &e" + purchasing + (purchasing == 1 ? " 件" : " 件"));
                binLoreBuilder.addLore("&7总费用: &f" + (listing.binPrice * purchasing) + " 灵魂");
                binLoreBuilder.addLore("");
            }

            int cost = listing.stackBIN ? listing.binPrice * purchasing : listing.binPrice;

            if (listing.ownerUUID.equals(player.getUniqueId())) {
                binLoreBuilder.addLore("&c你不能购买自己的物品！");
            } else if (PitPlayer.getPitPlayer(player).taintedSouls < cost) {
                binLoreBuilder.addLore("&c灵魂不足！");
                if (purchasing != 1) binLoreBuilder.addLore("&e右键点击更改购买数量");
            } else {
                if (!listing.stackBIN) binLoreBuilder.addLore("&e点击购买 &f" + cost + " 灵魂");
                else {
                    binLoreBuilder.addLore("&e左键点击购买 &f" + purchasing + " 件物品 &efor &f" + cost + " 灵魂");
                    binLoreBuilder.addLore("&e右键点击更改购买数量");
                }
            }
        } else {
            binLoreBuilder.addLore("&7此物品未启用一口价！");
        }

        binBuilder.setLore(binLoreBuilder);
        getInventory().setItem(15, binBuilder.getItemStack());

        // 更新返回项
        AItemStackBuilder backBuilder = new AItemStackBuilder(Material.BARRIER)
                .setName("&c返回");
        getInventory().setItem(31, backBuilder.getItemStack());

        // 更新竞标记录项
        AItemStackBuilder bidsBuilder = new AItemStackBuilder(Material.MAP)
                .setName("&e最高竞标");
        ALoreBuilder bidsLoreBuilder = new ALoreBuilder();
        for (Map.Entry<UUID, Integer> entry : listing.sortBidMap()) {
            bidsLoreBuilder.addLore(listing.bidderDisplayNames.get(entry.getKey()) + "&7: &f" + entry.getValue() + " 灵魂");
        }

        bidsBuilder.setLore(bidsLoreBuilder);
        bidsBuilder.getItemStack().getItemMeta().addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if (listing.startingBid != -1) getInventory().setItem(22, bidsBuilder.getItemStack());

        // 更新查看玩家所有列表项
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setOwner(listing.ownerUUID.toString());
        head.setItemMeta(headMeta);

        new AItemStackBuilder(head)
                .setName("&e查看该玩家的所有列表");

        if (player.hasPermission("pitsim.admin")) getInventory().setItem(35, head);
    }

    @Override
    public String getName() {
        return "查看列表";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory().getHolder() != this) return;
        int slot = event.getSlot();

        if (slot == 35 && player.hasPermission("pitsim.admin")) {
            openPanel(new MarketAdminPanel(gui, listing.ownerUUID));
            return;
        }

        if (slot == 11) {
            if (listing.startingBid != -1) {
                int soulsToTake = bid - listing.bidMap.getOrDefault(player.getUniqueId(), 0);
                if (listing.ownerUUID.equals(player.getUniqueId()) || (listing.getHighestBidder() != null &&
                        listing.getHighestBidder().equals(player.getUniqueId())) || PitPlayer.getPitPlayer(player).taintedSouls < soulsToTake) {
                    Sounds.NO.play(player);
                } else {
                    if (event.isRightClick()) {
                        bidSign();
                        return;
                    }

                    ConfirmPurchasePanel panel = new ConfirmPurchasePanel(gui, listing, bid, false, 1);
                    openPanel(panel);
                }
            }

        } else if (slot == 15) {

            if (event.isRightClick() && listing.stackBIN) {
                binSign();
                return;
            }

            if (listing.binPrice != -1) {
                if (listing.ownerUUID.equals(player.getUniqueId()) || PitPlayer.getPitPlayer(player).taintedSouls < (listing.stackBIN ? listing.binPrice * purchasing : listing.binPrice)) {
                    Sounds.NO.play(player);
                } else {
                    ConfirmPurchasePanel panel = new ConfirmPurchasePanel(gui, listing, listing.stackBIN ? purchasing * listing.binPrice : listing.binPrice, true, purchasing);
                    openPanel(panel);
                }
            }

        } else if (slot == 31) {
            if (marketPanel) openPanel(((MarketGUI) gui).marketPanel);
            else openPanel(((MarketGUI) gui).yourListingsPanel);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                calculateItems();
            }
        }.runTaskTimer(Thepit.INSTANCE, 20, 20);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if (runnable != null) runnable.cancel();
    }

    public void binSign() {

        SignPrompt.promptPlayer(player, "", "^^^^^", "输入购买数量", "(最大 " + listing.itemData.getAmount() + ")", input -> {
            openPanel(this);
            int amount;
            try {
                amount = Integer.parseInt(input.replaceAll("\"", ""));
            } catch (Exception ignored) {
                Sounds.NO.play(player);
                AOutput.error(player, "&c&l错误!&7 无法解析数量!");
                return;
            }

            if (amount < 1 || amount > listing.itemData.getAmount()) {
                Sounds.NO.play(player);
                AOutput.error(player, "&c&l错误!&7 无效的数量!");
                return;
            }

            purchasing = amount;
            openPanel(this);
            calculateItems();
        });
    }

    public void bidSign() {

        SignPrompt.promptPlayer(player, "", "^^^^^", "输入出价金额", "(最少 " + listing.getMinimumBid() + ")", input -> {
            openPanel(this);
            int amount;
            try {
                amount = Integer.parseInt(input.replaceAll("\"", ""));
            } catch (Exception ignored) {
                Sounds.NO.play(player);
                AOutput.error(player, "&c&l错误!&7 无法解析金额!");
                return;
            }

            if (amount < listing.getMinimumBid()) {
                Sounds.NO.play(player);
                AOutput.error(player, "&c&l错误!&7 出价低于最低限额!");
                return;
            }

            if (amount > PitPlayer.getPitPlayer(player).taintedSouls) {
                Sounds.NO.play(player);
                AOutput.error(player, "&c&l错误!&7 你的灵魂数量不足!");
                return;
            }

            bid = amount;

            ConfirmPurchasePanel panel = new ConfirmPurchasePanel(gui, listing, bid, false, 1);
            openPanel(panel);
        });
    }
}
