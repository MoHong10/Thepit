package me.wisdom.thepit.market;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class YourListingsPanel extends AGUIPanel {

    public static List<YourListingsPanel> panels = new ArrayList<>();

    public Map<Integer, MarketListing> listings = new HashMap<>();
    public Map<Integer, MarketListing> soulClaims = new HashMap<>();
    public Map<Integer, MarketListing> itemClaims = new HashMap<>();

    public BukkitTask updateTask;
    public YourListingsPanel(AGUI gui) {
        super(gui);

        for(int i = 0; i < 54; i++) {
            if(i < 9 || i > 45) {
                getInventory().setItem(i, new AItemStackBuilder(Material.STAINED_GLASS_PANE,1, 15).setName(" ").getItemStack());
                continue;
            }

            if(i % 9 != 0 && i % 9 != 4 && i % 9 != 8) continue;
            getInventory().setItem(i, new AItemStackBuilder(Material.STAINED_GLASS_PANE,1, 15).setName(" ").getItemStack());

        }

        AItemStackBuilder back = new AItemStackBuilder(Material.BARRIER).setName("&返回");
        getInventory().setItem(49, back.getItemStack());

        placeClaimables();
        placeListings();
    }

    public void placeListings() {
        MarketManager.ListingLimit rank = MarketManager.ListingLimit.getRank(player);
        int itemsPlaced = 0;
        List<MarketListing> listings = MarketManager.getActiveListings(player.getUniqueId());
        int listingsPlaced = 0;

        for(int i = 14; i < 44; i++) {
            if(i % 9 < 5 || i % 9 > 7) continue;

            if(listings.size() > listingsPlaced) {
                MarketListing listing = listings.get(listingsPlaced);

                ItemStack item = listing.getItemStack();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                if(isCancelable(listing)) lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight-Click to Cancel"));
                meta.setLore(lore);
                item.setItemMeta(meta);

                getInventory().setItem(i, item);
                this.listings.put(i, listing);

                listingsPlaced++;
                itemsPlaced++;
                continue;
            }

            if(itemsPlaced >= rank.limit) {
                MarketManager.ListingLimit required = MarketManager.ListingLimit.getMinimumRequiredRank(itemsPlaced + 1);
                getInventory().setItem(i, new AItemStackBuilder(Material.STAINED_GLASS_PANE,1, 14)
                        .setName("&cLocked Slot")
                        .setLore(new ALoreBuilder(
                                "&7Required Rank: " + required.rankName,
                                "",
                                "&ePurchase at &f&nstore.pitsim.net"
                        )).getItemStack());

                itemsPlaced++;
                continue;
            }

            getInventory().setItem(i, new AItemStackBuilder(Material.STAINED_GLASS_PANE,1, 5)
                    .setName("&aUnused Slot")
                    .setLore(new ALoreBuilder(
                            "&7Create a listing to occupy",
                            "&7this slot."
                    )).getItemStack());

            itemsPlaced++;
        }
    }

    public void placeClaimables() {
        soulClaims.clear();
        itemClaims.clear();

        for(int i = 10; i < 40; i++) {
            if(i % 9 < 1 || i % 9 > 3) continue;

            getInventory().setItem(i, new ItemStack(Material.AIR));
        }

        List<MarketListing> soulListings = new ArrayList<>();

        for(MarketListing listing : MarketManager.listings) {

            if(listing.hasEnded && listing.ownerUUID.equals(player.getUniqueId()) && listing.claimableSouls > 0) {
                soulListings.add(listing);
            }

            List<UUID> bidders = new ArrayList<>(listing.bidMap.keySet());

            if(listing.buyer == null) continue;
            if(!listing.hasEnded) continue;

            if(bidders.contains(player.getUniqueId()) && !listing.buyer.equals(player.getUniqueId())) soulListings.add(listing);
        }

        List<MarketListing> itemListings = new ArrayList<>();
        for(MarketListing listing : new ArrayList<>(MarketManager.listings)) {
            if(!listing.hasEnded || listing.itemClaimed) continue;

            if(listing.startingBid != -1) {
                if(listing.ownerUUID.equals(player.getUniqueId()) && listing.buyer == null) itemListings.add(listing);
                else if(listing.buyer != null && listing.buyer.equals(player.getUniqueId())) itemListings.add(listing);
            } else if(listing.binPrice != -1) {
                if(listing.ownerUUID.equals(player.getUniqueId())) itemListings.add(listing);
            }
        }

        List<MarketListing> combined = new ArrayList<>(soulListings);
        boolean firstIterationComplete = false;

        int listIndex = 0;
        for(int i = 10; i < 40; i++) {
            if(i % 9 < 1 || i % 9 > 3) continue;

            getInventory().setItem(i, new ItemStack(Material.AIR));

            if(listIndex == combined.size()) {
                if(!firstIterationComplete) {
                    combined = itemListings;
                    firstIterationComplete = true;
                    listIndex = 0;
                } else break;

                if(combined.isEmpty()) break;
            }

            MarketListing listing = combined.get(listIndex);
            if(!firstIterationComplete) {
                AItemStackBuilder soulBuilder = new AItemStackBuilder(Material.INK_SACK, 1, 7)
                        .setName("&fClaimable Souls");

                ALoreBuilder loreBuilder;
                if(listing.ownerUUID.equals(player.getUniqueId())) {
                    loreBuilder = new ALoreBuilder(
                            "&7Sold: " + listing.itemData.getItemMeta().getDisplayName() + (listing.stackBIN ? " &8x" + listing.getItemsSold() : ""),
                            "&7Sold For: &f&m" + (listing.stackBIN ? listing.binPrice + " Souls&8 (Per Item)" : listing.claimableSouls + " Souls"),
                            "&7After Tax: &f" + (listing.stackBIN ? (listing.getTaxedSouls() / listing.getItemsSold()) +
                                    " Souls &8(Per Item)" : listing.getTaxedSouls() + " Souls"),
                            listing.stackBIN ? "&7Total Souls: &f" + listing.getTaxedSouls() + " Souls" : "&7Sold to: &f" +
                                    (listing.buyerDisplayName),
                            "",
                            "&eClick to claim Souls!"
                    );
                } else {
                    int returnedSouls = listing.bidMap.get(player.getUniqueId());
                    soulBuilder.getItemStack().setAmount(returnedSouls);
                    loreBuilder = new ALoreBuilder(
                            "&7Item: " + listing.itemData.getItemMeta().getDisplayName(),
                            "&7Winner: " + listing.buyerDisplayName,
                            "&7Your Bid: " + Formatter.formatSouls(returnedSouls),
                            "",
                            "&eClick to claim Souls!"
                    );
                }
                soulBuilder.setLore(loreBuilder.getLore());
                getInventory().setItem(i, soulBuilder.getItemStack());
                soulClaims.put(i, listing);
            }

            if(firstIterationComplete) {
                AItemStackBuilder itemBuilder = new AItemStackBuilder(listing.itemData.clone())
                        .setName(listing.itemData.getItemMeta().getDisplayName() + (listing.stackBIN ? " &8x" + (listing.itemData.getAmount()) : ""));

                ALoreBuilder loreBuilder = new ALoreBuilder();
                loreBuilder.addLore(listing.itemData.getItemMeta().getLore());
                loreBuilder.addLore("&8&m------------------------");
                if(listing.ownerUUID.equals(player.getUniqueId())) loreBuilder.addLore("&7" + (listing.stackBIN ? "These items" : "This item") + " did not sell");
                else loreBuilder.addLore("&7You won this item!");
                loreBuilder.addLore("&8&m------------------------", "", "&eClick to claim item!");

                itemBuilder.setLore(loreBuilder.getLore());
                getInventory().setItem(i, itemBuilder.getItemStack());
                itemClaims.put(i, listing);
            }
            listIndex++;
        }
    }

    @Override
    public String getName() {
        return "   Your Claims    Your Listings";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory().getHolder() != this) return;
        int slot = event.getSlot();

        if(slot == 49) {
            openPanel(((MarketGUI) gui).selectionPanel);
        }

        if(listings.containsKey(slot)) {
            MarketListing listing = listings.get(slot);
            if(listing.hasEnded) {
                Sounds.NO.play(player);
                return;
            }

            if(event.isRightClick() && isCancelable(listing)) {
                openPanel(new ConfirmDeletionPanel(gui, listing));
                return;
            }

            ((MarketGUI) gui).listingInspectPanel = new ListingInspectPanel(gui, listing, false);
            openPanel(((MarketGUI) gui).listingInspectPanel);
        }

        if(soulClaims.containsKey(slot)) {
            MarketListing listing = soulClaims.get(slot);

            Runnable success = new BukkitRunnable() {
                @Override
                public void run() {
                    int claimedSouls = listing.getTaxedSouls();
                    AOutput.send(player, "&a&lMARKET!&7 Claimed &f" + claimedSouls + " Souls");
                    PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
                    pitPlayer.giveSouls(claimedSouls, false);
                    if(listing.ownerUUID.equals(player.getUniqueId())) pitPlayer.stats.listingsSold++;
                    Sounds.RENOWN_SHOP_PURCHASE.play(player);
                    placeClaimables();
                }
            };

            String failMessage = "&cThere was an error while attempting to claim your souls. Please contact a staff member about this issue.";
            new MarketAsyncTask(MarketAsyncTask.MarketTask.CLAIM_SOULS, listing.marketUUID, player, 0, success, MarketAsyncTask.getDefaultFail(player, failMessage));
        }

        if(itemClaims.containsKey(slot)) {
            MarketListing listing = itemClaims.get(slot);

            Runnable success = new BukkitRunnable() {
                @Override
                public void run() {
                    AOutput.send(player, "&a&lMARKET!&7 Claimed " + listing.itemData.getItemMeta().getDisplayName() + (listing.stackBIN ? " &8x" + (listing.itemData.getAmount()) : ""));
                    AUtil.giveItemSafely(player, listing.itemData, true);
                    Sounds.RENOWN_SHOP_PURCHASE.play(player);
                    if(listing.ownerUUID != player.getUniqueId()) PitPlayer.getPitPlayer(player).stats.listingsClaimed++;
                    placeClaimables();
                }
            };

            String failMessage = "&cThere was an error while attempting to claim your item(s). Please contact a staff member about this issue.";
            new MarketAsyncTask(MarketAsyncTask.MarketTask.CLAIM_ITEM, listing.marketUUID, player, 0, success, MarketAsyncTask.getDefaultFail(player, failMessage));
        }
    }

    public boolean isCancelable(MarketListing listing) {
        if(listing.hasEnded()) return false;
        if(listing.startingBid != -1) return listing.bidMap.isEmpty();
        else return true;
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        panels.add(this);
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                placeListings();
            }
        }.runTaskTimer(Thepit.INSTANCE, 20, 20);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        panels.remove(this);
        if(updateTask != null) updateTask.cancel();
    }
}
