package me.wisdom.thepit.market;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.enums.MarketCategory;
import me.wisdom.thepit.misc.HeadLib;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.misc.packets.SignPrompt;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class MarketPanel extends AGUIPanel {

    public SortQuery.PrimarySortType sortType = SortQuery.PrimarySortType.PRICE_HIGH;
    public SortQuery.ListingFilter listingFilter = SortQuery.ListingFilter.ALL;
    public MarketCategory marketCategory = MarketCategory.ALL;
    public SortQuery sortQuery;
    public String searchParameter = "";
    public int page = 0;
    public int maxPages;
    public List<Integer> glassSlots = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 10, 17, 19, 26, 28, 35, 37, 44, 46, 47, 48, 49, 50	, 52, 53);
    public Map<Integer, UUID> listingSlots = new HashMap<>();
    public BukkitTask runnable;


    public MarketPanel(AGUI gui) {
        super(gui);
        this.sortQuery = new SortQuery(sortType, SortQuery.ListingFilter.ALL, MarketCategory.ALL, "");

        for(Integer glassSlot : glassSlots) {
            getInventory().setItem(glassSlot, new AItemStackBuilder(Material.STAINED_GLASS_PANE,1, 15).setName(" ").getItemStack());
        }

        createInventory();
    }

    public void createInventory() {
        calculateListings();
        calculateArrows();

        AItemStackBuilder backBuilder = new AItemStackBuilder(Material.BARRIER)
                .setName("&c返回主菜单");
        getInventory().setItem(45, backBuilder.getItemStack());

        updateSortItemStack();
        updateListingFilterItemStack();
        updateSearchItemStack();
        updateAuctionCategoryItemStack();
    }

    public void calculateArrows() {
        int currentPages = page + 1;

        AItemStackBuilder upBuilder = new AItemStackBuilder(HeadLib.getCustomHead(HeadLib.getLeftArrowHead()))
                .setName(page > 0 ? "&a上一页 &7(" + currentPages + "/" + maxPages + ")" : "&c上一页 &7(" + currentPages + "/" + maxPages + ")");
        getInventory().setItem(48, upBuilder.getItemStack());

        AItemStackBuilder downBuilder = new AItemStackBuilder(HeadLib.getCustomHead(HeadLib.getRightArrowHead()))
                .setName(page < maxPages - 1 ? "&a下一页 &7(" + currentPages + "/" + maxPages + ")" : "&c下一页 &7(" + currentPages + "/" + maxPages + ")");
        getInventory().setItem(51, downBuilder.getItemStack());
    }

    public void calculateListings() {
        MarketListing[] listings = sortQuery.getListings();
        maxPages = (int) Math.max(1, Math.ceil(listings.length / 24D));

        int itemIndex = page * 24;

        for(int i = 9; i < 45; i++) {
            if(i % 9 <= 1 || i % 9 == 8) continue;
            if(listings.length < itemIndex + 1) {
                getInventory().setItem(i, null);
                listingSlots.remove(i);
                continue;
            }
            getInventory().setItem(i, listings[itemIndex].getItemStack());
            listingSlots.put(i, listings[itemIndex].marketUUID);
            itemIndex++;
        }

        calculateArrows();
    }

    public void updateSortItemStack() {
        ALoreBuilder loreBuilder = new ALoreBuilder("");
        for(SortQuery.PrimarySortType value : SortQuery.PrimarySortType.values()) {
            String toAdd = (value == sortType ? "&7\u27a4 " : "") +  value.color + (value == sortType ? "&l" : "") + value.displayName;
            loreBuilder.addLore(toAdd);
        }
        loreBuilder.addLore("", "&e点击以切换");

        AItemStackBuilder sortBuilder = new AItemStackBuilder(sortType.material)
                .setName("&e更改排序类型")
                .setLore(loreBuilder.getLore());

        getInventory().setItem(0, sortBuilder.getItemStack());
    }

    public void updateListingFilterItemStack() {
        ALoreBuilder loreBuilder = new ALoreBuilder("");
        for(SortQuery.ListingFilter value : SortQuery.ListingFilter.values()) {
            String toAdd = (value == listingFilter ? "&7\u27a4 " : "") +  value.color + (value == listingFilter ? "&l" : "") + value.displayName;
            loreBuilder.addLore(toAdd);
        }
        loreBuilder.addLore("", "&e点击以切换");

        AItemStackBuilder sortBuilder = new AItemStackBuilder(listingFilter.material)
                .setName("&e更改列表类型")
                .setLore(loreBuilder.getLore());

        getInventory().setItem(9, sortBuilder.getItemStack());
    }

    public void updateAuctionCategoryItemStack() {
        ALoreBuilder loreBuilder = new ALoreBuilder("");
        for(MarketCategory value : MarketCategory.values()) {
            String toAdd = (value == marketCategory ? "&7\u27a4 " : "") + value.color + (value == marketCategory ? "&l" : "") + value.displayName;
            loreBuilder.addLore(toAdd);
        }
        loreBuilder.addLore("", "&e点击以切换");

        AItemStackBuilder sortBuilder = new AItemStackBuilder(marketCategory.displayMaterial)
                .setName("&e更改物品种类")
                .setLore(loreBuilder.getLore());

        getInventory().setItem(18, sortBuilder.getItemStack());
    }

    public void updateSearchItemStack() {
        AItemStackBuilder searchBuilder = new AItemStackBuilder(Material.SIGN)
                .setName("&e搜索列表")
                .setLore(new ALoreBuilder(
                        "",
                        "&7搜索参数: ",
                        !searchParameter.isEmpty() ? "&f&o\"" + searchParameter + "\"": "&c无",
                        "",
                        "&e点击更改",
                        "&e右键清除"
                ));

        if(!searchParameter.isEmpty()) Misc.addEnchantGlint(searchBuilder.getItemStack());
        getInventory().setItem(27, searchBuilder.getItemStack());
    }

    @Override
    public String getName() {
        return "市场列表";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory().getHolder() != this) return;

        if(listingSlots.containsKey(event.getSlot())) {
            UUID uuid = listingSlots.get(event.getSlot());
            MarketListing listing = MarketManager.getListing(uuid);
            if(listing == null) return;
            ((MarketGUI) gui).listingInspectPanel = new ListingInspectPanel(gui, listing, true);
            openPanel(((MarketGUI) gui).listingInspectPanel);
        }

        if(event.getSlot() == 0) {
            sortType = sortType.getNext();
            sortQuery = new SortQuery(sortType, listingFilter, marketCategory, searchParameter);
            Sounds.HELMET_TICK.play(player);
            updateSortItemStack();
            page = 0;
            calculateListings();
        }

        if(event.getSlot() == 9) {
            listingFilter = listingFilter.getNext();
            sortQuery = new SortQuery(sortType, listingFilter, marketCategory, searchParameter);
            Sounds.HELMET_TICK.play(player);
            updateListingFilterItemStack();
            page = 0;
            calculateListings();
        }

        if(event.getSlot() == 18) {
            marketCategory = marketCategory.getNext();
            sortQuery = new SortQuery(sortType, listingFilter, marketCategory, searchParameter);
            Sounds.HELMET_TICK.play(player);
            updateAuctionCategoryItemStack();
            page = 0;
            calculateListings();
        }

        if(event.getSlot() == 27) {
            if(event.isLeftClick()) {
                SignPrompt.promptPlayer(player, "", "^^^^^^", "输入搜索", "提示", input -> {
                openPanel(this);
                    searchParameter = input.replaceAll("\"", "");
                    sortQuery = new SortQuery(sortType, listingFilter, marketCategory, searchParameter);
                    updateSearchItemStack();
                    page = 0;
                    calculateListings();

                    if(runnable != null) runnable.cancel();
                    runnable = new BukkitRunnable() {
                        @Override
                        public void run() {
                            calculateListings();
                        }
                    }.runTaskTimer(Thepit.INSTANCE, 20, 20);

                });
            } else if(event.isRightClick()) {
                searchParameter = "";
                sortQuery = new SortQuery(sortType, listingFilter, marketCategory, searchParameter);
                Sounds.HELMET_TICK.play(player);
                updateSearchItemStack();
                page = 0;
                calculateListings();
            }
        }

        if(event.getSlot() == 48) {
            if(page > 0) {
                page--;
                calculateListings();
                calculateArrows();
                Sounds.HELMET_TICK.play(player);
            } else Sounds.NO.play(player);
        }

        if(event.getSlot() == 51) {
            if(page + 1 < maxPages) {
                page++;
                calculateListings();
                calculateArrows();
                Sounds.HELMET_TICK.play(player);
            } else Sounds.NO.play(player);
        }

        if(event.getSlot() == 45) {
            openPanel(((MarketGUI) gui).selectionPanel);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                calculateListings();
            }
        }.runTaskTimer(Thepit.INSTANCE, 20, 20);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        runnable.cancel();

    }
}
