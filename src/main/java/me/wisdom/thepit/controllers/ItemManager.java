package me.wisdom.thepit.controllers;

import de.myzelyam.api.vanish.VanishAPI;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.auction.AuctionDisplays;
import me.wisdom.thepit.controllers.objects.FakeItem;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.PitEntityType;
import me.wisdom.thepit.events.PitJoinEvent;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.items.misc.SoulPickup;
import me.wisdom.thepit.items.misc.VeryYummyBread;
import me.wisdom.thepit.items.misc.YummyBread;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ItemManager implements Listener {
    public static Map<UUID, List<ItemStack>> updatedItems = new HashMap<>();
    public static List<FakeItem> fakeItems = new ArrayList<>();
    public static Map<Item, Player> soulPickupMap = new HashMap<>();
    public static Map<Player, Long> soulNotificationCooldownMap = new HashMap<>();

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(FakeItem fakeItem : new ArrayList<>(fakeItems)) {
                    if(fakeItem.hasBeenRemoved()) {
                        fakeItem.remove();
                        continue;
                    }

                    for(Entity entity : fakeItem.getLocation().getWorld().getNearbyEntities(fakeItem.getLocation(), 1, 1, 1)) {
                        if(!Misc.isEntity(entity, PitEntityType.REAL_PLAYER)) continue;
                        Player player = (Player) entity;
                        if(!fakeItem.getViewers().contains(player) || VanishAPI.isInvisible(player) ||
                                !Misc.hasSpaceForItem(player, fakeItem.getItemStack())) continue;

                        fakeItem.pickup(player);
                        break;
                    }
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0, 3);
    }

    @EventHandler
    public void onJoin(PitJoinEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!updatedItems.containsKey(player.getUniqueId()) || !player.isOnline()) return;
                List<ItemStack> itemList = updatedItems.remove(player.getUniqueId());
                for(ItemStack itemStack : itemList) {
                    String itemName = itemStack.getItemMeta().getDisplayName();
                    String haveHas = itemName.endsWith("s") ? "已更新" : "已更新";
                    AOutput.send(player, "&a&l物品已更新!&7 你的 " + itemName + "&7 " + haveHas + " 已被更新");
                }
            }
        }.runTaskLater(Thepit.INSTANCE, 10L);
    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent event) {
        if(event.getAction() != InventoryAction.DROP_ALL_CURSOR && event.getAction() != InventoryAction.DROP_ALL_SLOT &&
                event.getAction() != InventoryAction.DROP_ONE_CURSOR && event.getAction() != InventoryAction.DROP_ONE_SLOT)
            return;

        ItemStack itemStack = !Misc.isAirOrNull(event.getCursor()) ? event.getCursor() : event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        PitItem pitItem = ItemFactory.getItem(itemStack);
        if(pitItem == null) return;
        if(!pitItem.hasDropConfirm && !ItemFactory.isTutorialItem(itemStack)) return;
        if(pitItem.isMystic && !MysticFactory.isImportant(itemStack) && !ItemFactory.isTutorialItem(itemStack)) return;

        event.setCancelled(true);
        player.updateInventory();
        AOutput.error(player, "此物品只能在你的背包关闭时掉落");
        Sounds.WARNING_LOUD.play(player);
    }

    @EventHandler
    public static void onUnload(ChunkUnloadEvent event) {
        PitItem pitItem = ItemFactory.getItem(SoulPickup.class);
        for(Entity entity : new ArrayList<>(Arrays.asList(event.getChunk().getEntities()))) {
            if(!(entity instanceof Item)) continue;
            Item item = (Item) entity;
            ItemStack itemStack = item.getItemStack();
            if(!pitItem.isThisItem(itemStack)) continue;
            item.remove();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public static void onPickup(PlayerPickupItemEvent event) {
        if(event.isCancelled()) return;
        Item droppedItem = event.getItem();
        ItemStack itemStack = droppedItem.getItemStack();
        Player player = event.getPlayer();
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        SoulPickup pitItem = ItemFactory.getItem(SoulPickup.class);
        if(pitItem.isThisItem(itemStack)) {
            int souls = pitItem.getSouls(itemStack);
            event.setCancelled(true);

            if(soulPickupMap.containsKey(droppedItem)) {
                Player designatedPlayer = soulPickupMap.get(droppedItem);
                if(designatedPlayer != player) {
                    long lastNotifyTick = soulNotificationCooldownMap.getOrDefault(player, 0L);
                    if(lastNotifyTick + 40 > Thepit.currentTick) return;
                    AOutput.error(player, "&c&l错误!&7 你不能拾取这个灵魂");
                    soulNotificationCooldownMap.put(player, Thepit.currentTick);
                    return;
                }
            }

            soulPickupMap.remove(droppedItem);
            droppedItem.remove();
            pitPlayer.giveSouls(souls);

            Sounds.SOUL_PICKUP.play(player);
            AOutput.send(player, "&5&l收获!&7 你收获了 &f" + souls + " 个灵魂" + (souls == 1 ? "" : "s"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public static void onItemDrop(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();
        Player player = event.getPlayer();
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(Misc.isAirOrNull(itemStack)) return;

        if(ShutdownManager.isShuttingDown) {
            event.setCancelled(true);
            AOutput.send(player, "&c&l错误!&7 服务器正在关闭时你不能丢弃物品");
            return;
        }

        if(ItemFactory.isTutorialItem(itemStack)) {
            event.setCancelled(true);
            AOutput.error(player, "&c你不能丢弃教程物品!");
            Sounds.NO.play(player);
            return;
        }

        if(player.getWorld() == MapManager.getDarkzone()) {
            Location darkAuction = AuctionDisplays.pedestalLocations[0];
            double distance = darkAuction.distance(event.getPlayer().getLocation());

            if(distance < 50) {
                event.setCancelled(true);
                AOutput.error(player, "&c你不能在这个区域丢弃物品!");
                Sounds.WARNING_LOUD.play(player);
                return;
            }
        }

        PitItem pitItem = ItemFactory.getItem(itemStack);
        if(pitItem != null && pitPlayer.isOnMega()) {
            if(pitItem instanceof YummyBread || pitItem instanceof VeryYummyBread) {
                event.setCancelled(true);
                AOutput.error(player, "&c&l错误!&7 在超级连击期间你不能丢弃面包");
                return;
            }
        }

        if(!MysticFactory.isImportant(itemStack)) return;
        if(pitItem == null || !pitItem.hasDropConfirm) {
            if(itemStack.getType() != Material.ENDER_CHEST && itemStack.getType() != Material.TRIPWIRE_HOOK) return;
        }

        if(pitPlayer.confirmedDrop == null || !pitPlayer.confirmedDrop.equals(itemStack)) {
            event.setCancelled(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(pitPlayer.confirmedDrop != null && pitPlayer.confirmedDrop.equals(itemStack))
                        pitPlayer.confirmedDrop = null;
                }
            }.runTaskLater(Thepit.INSTANCE, 60L);
            pitPlayer.confirmedDrop = itemStack;
            AOutput.error(player, "&e&l警告!&7 你即将丢弃一个物品。请再次点击丢弃按钮以确认丢弃物品。");
            Sounds.WARNING_LOUD.play(player);
        } else {
            pitPlayer.confirmedDrop = null;
        }
    }
}
