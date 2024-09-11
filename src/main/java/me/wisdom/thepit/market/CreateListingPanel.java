package me.wisdom.thepit.market;

import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.enums.MysticType;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.misc.packets.SignPrompt;
import me.wisdom.thepit.storage.StorageProfile;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CreateListingPanel extends AGUIPanel {

    public ItemStack selectedItem = null;
    public boolean auctionEnabled = false;
    public boolean binEnabled = false;

    public int startingBid = 0;
    public int binPrice = 0;

    public boolean signClose = false;

    public long duration = 86400000;

    public CreateListingPanel(AGUI gui) {
        super(gui);

        for(int i = 0; i < 5 * 9; i++) {
            getInventory().setItem(i, new AItemStackBuilder(Material.STAINED_GLASS_PANE, 1, 15).setName(" ").getItemStack());
            calculateItems();
        }
    }

    @Override
    public String getName() {
        return "创建市场列表";
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public void onClick(InventoryClickEvent event) {

        int slot = event.getSlot();
        if (event.getClickedInventory().getHolder() == this) {
            if (slot == 13) {
                if (selectedItem == null) return;
                AUtil.giveItemSafely(player, selectedItem, true);
                selectedItem = null;
                player.updateInventory();
                Sounds.BOOSTER_REMIND.play(player);
                auctionEnabled = false;
                binEnabled = false;
                startingBid = 0;
                binPrice = 0;
                calculateItems();
            }

            if (slot == 11) {
                if (selectedItem == null) {
                    AOutput.error(player, "&c请先选择一个物品！");
                    Sounds.NO.play(player);
                    return;
                }

                if (selectedItem.getAmount() > 1) {
                    AOutput.error(player, "&c一次只能拍卖一个物品！");
                    Sounds.NO.play(player);
                    return;
                }

                auctionEnabled = !auctionEnabled;
                Sounds.HELMET_TICK.play(player);
                calculateItems();
            }

            if (slot == 15) {
                if (selectedItem == null) {
                    AOutput.error(player, "&c请先选择一个物品！");
                    Sounds.NO.play(player);
                    return;
                }

                binEnabled = !binEnabled;
                Sounds.HELMET_TICK.play(player);
                calculateItems();
            }

            if (slot == 20) {
                if (!auctionEnabled) {
                    Sounds.NO.play(player);
                    return;
                }

                signPromptAuction();
            }

            if (slot == 24) {
                if (!binEnabled) {
                    Sounds.NO.play(player);
                    return;
                }

                signPromptBin();
            }

            if (slot == 38) {
                openPanel(((MarketGUI) gui).selectionPanel);
            }

            if (slot == 40) {
                if (!isValidItem(selectedItem)) {
                    Sounds.NO.play(player);
                    return;
                }

                BukkitRunnable success = new BukkitRunnable() {
                    @Override
                    public void run() {
                        Sounds.SUCCESS.play(player);
                        AOutput.send(player, "&a&l市场！&7 列表已创建！");
                    }
                };

                ItemStack returnItem = selectedItem.clone();

                BukkitRunnable fail = new BukkitRunnable() {
                    @Override
                    public void run() {
                        AUtil.giveItemSafely(player, returnItem, true);
                        player.updateInventory();

                        Sounds.NO.play(player);
                        String failMessage = "&c创建列表时发生错误。请联系工作人员处理此问题。";
                        AOutput.error(player, failMessage);
                    }
                };

                MarketAsyncTask.CreateListingInfo info = new MarketAsyncTask.CreateListingInfo();
                info.setItem(StorageProfile.serialize(player, selectedItem, false));
                info.setStartingBid(startingBid == 0 ? -1 : startingBid);
                info.setBinPrice(binPrice == 0 ? -1 : binPrice);
                info.setStackBin(selectedItem.getAmount() > 1);
                info.setDuration(duration);

                new MarketAsyncTask(MarketAsyncTask.MarketTask.CREATE_LISTING, player, info, success, fail);

                selectedItem = null;
                player.closeInventory();
            }

            if(slot == 42) {
                signPromptDuration();
            }
        }

        if(event.getClickedInventory().equals(player.getInventory())) {
            ItemStack item = event.getCurrentItem();
            PitItem pitItem = ItemFactory.getItem(item);

            if(pitItem == null || pitItem.marketCategory == null || ItemFactory.isTutorialItem(item)) {
                Sounds.NO.play(player);
            } else {

                if(selectedItem != null) {
                    AUtil.giveItemSafely(player, selectedItem, true);
                    selectedItem = null;
                    player.updateInventory();
                }

                Misc.createMeta(item);

                auctionEnabled = false;
                binEnabled = false;
                startingBid = 0;
                binPrice = 0;
                selectedItem = item;
                player.getInventory().setItem(slot, new ItemStack(Material.AIR));
                player.updateInventory();
                Sounds.BOOSTER_REMIND.play(player);
                calculateItems();
            }
        }
    }

    public void calculateItems() {

        // 设置选定物品的显示
        AItemStackBuilder selectedItemBuilder = new AItemStackBuilder(Material.ITEM_FRAME)
                .setName("&e选择一个物品！")
                .setLore(new ALoreBuilder(
                        "&7点击你的库存中的物品",
                        "&7以选择它。"
                ));
        getInventory().setItem(13, selectedItem == null ? selectedItemBuilder.getItemStack() : selectedItem);

        // 设置拍卖相关按钮
        AItemStackBuilder auctionBuilder;
        if (auctionEnabled && isBidValid()) {
            auctionBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 13)
                    .setName("&e以拍卖出售物品")
                    .setLore(new ALoreBuilder(
                            "&7玩家将能够竞标",
                            "&7你的物品", "",
                            "&e点击以禁用拍卖"
                    ));
        } else if (auctionEnabled) {
            auctionBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 4)
                    .setName("&6以拍卖出售物品")
                    .setLore(new ALoreBuilder(
                            "&7你的起拍价为 &c无效！",
                            "&7请在下面纠正", "",
                            "&6点击以禁用拍卖"
                    ));
        } else if (selectedItem != null && selectedItem.getAmount() > 1) {
            auctionBuilder = new AItemStackBuilder(Material.BARRIER)
                    .setName("&c以拍卖出售物品")
                    .setLore(new ALoreBuilder(
                            "&7玩家将能够竞标",
                            "&7你的物品", "",
                            "&c无法拍卖多个物品！"
                    ));
        } else {
            auctionBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 14)
                    .setName("&e以拍卖出售物品")
                    .setLore(new ALoreBuilder(
                            "&7玩家将能够竞标",
                            "&7你的物品", "",
                            "&e点击以启用拍卖"
                    ));
        }
        getInventory().setItem(11, auctionBuilder.getItemStack());

        // 设置一口价相关按钮
        AItemStackBuilder binBuilder;
        if (binEnabled && isBinValid()) {
            binBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 13)
                    .setName("&e以一口价出售物品")
                    .setLore(new ALoreBuilder(
                            "&7玩家将能够立即购买",
                            "&7你的物品", "",
                            "&e点击以禁用一口价"
                    ));
        } else if (binEnabled) {
            binBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 4)
                    .setName("&6以一口价出售物品")
                    .setLore(new ALoreBuilder(
                            "&7你的BIN价格为 &c无效！",
                            "&7请在下面纠正", "",
                            "&6点击以禁用一口价"
                    ));
        } else {
            binBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 14)
                    .setName("&e以一口价出售物品")
                    .setLore(new ALoreBuilder(
                            "&7玩家将能够立即购买",
                            "&7你的物品", "",
                            "&e点击以启用一口价"
                    ));
        }
        getInventory().setItem(15, binBuilder.getItemStack());

        // 设置起拍价
        AItemStackBuilder auctionSoulsBuilder = new AItemStackBuilder(Material.GHAST_TEAR)
                .setName("&e设置起拍价")
                .setLore(new ALoreBuilder(
                        "&7起拍价: " + (isBidValid() ? "&f" + startingBid + " 灵魂" : "&c无效！"),
                        "",
                        "&7至少必须为 &f10 灵魂", "",
                        auctionEnabled ? "&e点击以更改！" : "&c请先启用拍卖！"
                ));
        getInventory().setItem(20, auctionSoulsBuilder.getItemStack());

        // 设置一口价
        AItemStackBuilder binSoulsBuilder = new AItemStackBuilder(Material.GHAST_TEAR)
                .setName("&e设置BIN价格")
                .setLore(new ALoreBuilder(
                        "&7BIN价格: " + (isBinValid() ? ("&f" + binPrice + " 灵魂" + (selectedItem.getAmount() > 1 ? " &8(每个物品)" : "")) : "&c无效！"),
                        "",
                        binEnabled && isBidValid() ? "&7至少必须为 &f" + (startingBid * 2) + " 灵魂" : "&7至少必须为 &f10 灵魂", "",
                        binEnabled ? "&e点击以更改！" : "&c请先启用一口价！"
                ));
        getInventory().setItem(24, binSoulsBuilder.getItemStack());

        // 设置列表持续时间
        AItemStackBuilder durationBuilder = new AItemStackBuilder(Material.WATCH)
                .setName("&e列表持续时间")
                .setLore(new ALoreBuilder(
                        "&7列表持续时间: &f" + Formatter.formatDurationFull(duration, false),
                        "",
                        "&7最大持续时间: &f" + getMaxDurationDays() + " 天",
                        "&e通过 &f&nstore.pitsim.net &e提升此时间",
                        "",
                        "&e点击以更改！"
                ));
        getInventory().setItem(42, durationBuilder.getItemStack());

        // 设置返回按钮
        AItemStackBuilder backBuilder = new AItemStackBuilder(Material.BARRIER)
                .setName("&c取消")
                .setLore(new ALoreBuilder(
                        "&7返回到上一个菜单"
                ));
        getInventory().setItem(38, backBuilder.getItemStack());

        // 设置确认按钮
        boolean invalid = (auctionEnabled && !isBidValid()) || (binEnabled && !isBinValid());

        AItemStackBuilder confirmBuilder;
        if (selectedItem == null || (!auctionEnabled && !binEnabled)) {
            confirmBuilder = new AItemStackBuilder(Material.REDSTONE_BLOCK)
                    .setName("&c确认列表")
                    .setLore(new ALoreBuilder(
                            "&7点击以确认你的列表", "",
                            "&c请先选择销售类型！"
                    ));
        } else if (invalid) {
            confirmBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 1)
                    .setName("&6确认列表")
                    .setLore(new ALoreBuilder(
                            "&7点击以确认你的列表", "",
                            "&6请修复无效的销售值！"
                    ));
        } else {
            confirmBuilder = new AItemStackBuilder(Material.EMERALD_BLOCK)
                    .setName("&a确认列表")
                    .setLore(new ALoreBuilder(
                            "&7点击以确认你的列表", "",
                            "&a点击以确认！"
                    ));
        }
        getInventory().setItem(40, confirmBuilder.getItemStack());
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if(selectedItem != null && !signClose) {
            Player player = (Player) event.getPlayer();
            AUtil.giveItemSafely(player, selectedItem, true);
            selectedItem = null;
            player.updateInventory();
        }

        signClose = false;
    }

    public boolean isBidValid() {
        return startingBid >= 10 && startingBid <= 1000000;
    }

    public boolean isBinValid() {
        return binPrice >= 10 && binPrice >= startingBid * 2 && binPrice <= 1000000;
    }

    public void signPromptAuction() {

        signClose = true;
        SignPrompt.promptPlayer(player, "", "^^^^^", "输入起拍价", "（至少10个灵魂）", input -> {
            openPanel(this);
            int amount;
            try {
                amount = Integer.parseInt(input.replaceAll("\"", ""));
            } catch(Exception ignored) {
                Sounds.NO.play(player);
                AOutput.error(player, "&c&l错误！&7 无法解析起拍价！");
                return;
            }

            startingBid = amount;
            calculateItems();
            if(!isBidValid()) {
                Sounds.NO.play(player);
                AOutput.error(player, "&c&l错误！&7 起拍价必须至少为10个灵魂！");
            }
        });
    }

    public void signPromptBin() {

        signClose = true;
        SignPrompt.promptPlayer(player, "", "^^^^^", "输入一口价", auctionEnabled ? "(起拍价的2倍)" : "(至少10个灵魂)", input -> {
            openPanel(this);
            int amount;
            try {
                amount = Integer.parseInt(input.replaceAll("\"", ""));
            } catch(Exception ignored) {
                Sounds.NO.play(player);
                AOutput.error(player, "&c&l错误！&7 无法解析一口价！");
                return;
            }

            binPrice = amount;
            calculateItems();
            if(!isBinValid()) {
                Sounds.NO.play(player);
                if(!auctionEnabled) AOutput.error(player, "&c&l错误！&7 一口价必须至少为10个灵魂！");
                else AOutput.error(player, "&c&l错误！&7 一口价必须至少为起拍价的2倍！");
            }
        });
    }

    public void signPromptDuration() {

        signClose = true;
        SignPrompt.promptPlayer(player, "", "^^^^^", "最大: " + getMaxDurationDays() + " 天", "示例: 2天 6小时 9分钟", input -> {
            openPanel(this);
            long amount;
            try {
                amount = Misc.parseDuration(input.replaceAll("\"", "")).toMillis();
            } catch(Exception ignored) {
                Sounds.NO.play(player);
                AOutput.error(player, "&c&l错误！&7 无法解析持续时间！");
                return;
            }

            if(!isDurationValid(amount)) {
                Sounds.NO.play(player);
                AOutput.error(player, "&c&l错误！&7 无效的持续时间！");
            } else {
                duration = amount;
                calculateItems();
            }
        });
    }

    public boolean isDurationValid(long duration) {
        return duration >= 60000 && duration <= getMaxDurationDays() * 86400000L;
    }

    public int getMaxDurationDays() {
        return player.hasPermission("group.unthinkable") ? 7 : 3;
    }

    public boolean isValidItem(ItemStack selectedItem) {
        MysticType type = MysticType.getMysticType(selectedItem);
        if(type != null && !type.isTainted() && !MysticFactory.isJewel(selectedItem, false)) return false;

        return !(selectedItem == null || (!auctionEnabled && !binEnabled) || (auctionEnabled && !isBidValid()) ||
                (binEnabled && !isBinValid()) || EnchantManager.isIllegalItem(selectedItem));
    }
}
