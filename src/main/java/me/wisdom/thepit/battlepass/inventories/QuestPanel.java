package me.wisdom.thepit.battlepass.inventories;

import me.wisdom.thepit.battlepass.PassManager;
import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class QuestPanel extends AGUIPanel {
    public PassGUI passGUI;

    public static ItemStack backItem;

    public int dailyQuestPage = 1;
    public List<Integer> dailyQuestSlots = Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30, 37, 38, 39);

    public int weeklyQuestPage = 1;
    public List<Integer> weeklyQuestSlots = Arrays.asList(14, 15, 16, 23, 24, 25, 32, 33, 34, 41, 42, 43);

    static {
        backItem = new AItemStackBuilder(Material.BARRIER)
                .setName("&c返回")
                .setLore(new ALoreBuilder(
                        "&7点击返回上一页"
                ))
                .getItemStack();
    }

    public QuestPanel(AGUI gui) {
        super(gui);
        passGUI = (PassGUI) gui;

        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 7)
                .setSlots(Material.STAINED_GLASS_PANE, 7, 13, 22, 31, 40);

        getInventory().setItem(4, PassPanel.purchaseItem);
        getInventory().setItem(49, backItem);
    }

    public void setDailyQuestPage(int page) {
        ItemStack dailyQuestItem = new AItemStackBuilder(Material.PAPER)
                .setName("&e&l每日任务")
                .setLore(new ALoreBuilder(
                        "&7活跃的每日任务",
                        "",
                        "&7页数: &3" + dailyQuestPage + "&7/&3" + getDailyPages()
                ))
                .getItemStack();
        getInventory().setItem(2, dailyQuestItem);

        List<PassQuest> fullList = PassManager.getDailyQuests();
        fullList.removeIf(passQuest -> !passQuest.canProgressQuest(passGUI.pitPlayer));
        List<PassQuest> displayList = new ArrayList<>();
        for(int i = 0; i < dailyQuestSlots.size() + 1; i++) {
            int index = i + (page - 1) * dailyQuestSlots.size();
            if(index >= fullList.size()) break;
            displayList.add(fullList.get(index));
        }

        for(int i = 0; i < dailyQuestSlots.size(); i++) {
            int slot = dailyQuestSlots.get(i);
            if(i < displayList.size()) {
                PassQuest toDisplay = displayList.get(i);

                ItemStack itemStack = toDisplay.getDisplayStack(passGUI.pitPlayer, toDisplay.getDailyState(),
                        PassManager.getProgression(passGUI.pitPlayer, toDisplay));
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                itemStack.setItemMeta(itemMeta);
                getInventory().setItem(slot, itemStack);
            } else {
                ItemStack itemStack = new ItemStack(Material.AIR);
                getInventory().setItem(slot, itemStack);
            }
        }
    }

    public void setWeeklyQuestPage(int page) {
        ItemStack weeklyQuestItem = new AItemStackBuilder(Material.BOOK)
                .setName("&e&l每周任务")
                .setLore(new ALoreBuilder(
                        "&7活跃的每周任务",
                        "",
                        "&7页数: &3" + weeklyQuestPage + "&7/&3" + getWeeklyPages()
                ))
                .getItemStack();
        getInventory().setItem(6, weeklyQuestItem);

        List<PassQuest> fullList = PassManager.getWeeklyQuests();
        fullList.removeIf(passQuest -> !passQuest.canProgressQuest(passGUI.pitPlayer));
        List<PassQuest> displayList = new ArrayList<>();
        for(int i = 0; i < weeklyQuestSlots.size() + 1; i++) {
            int index = i + (page - 1) * weeklyQuestSlots.size();
            if(index >= fullList.size()) break;
            displayList.add(fullList.get(index));
        }

        for(int i = 0; i < weeklyQuestSlots.size(); i++) {
            int slot = weeklyQuestSlots.get(i);
            if(i < displayList.size()) {
                PassQuest toDisplay = displayList.get(i);

                ItemStack itemStack = toDisplay.getDisplayStack(passGUI.pitPlayer, PassManager.currentPass.weeklyQuests.get(toDisplay),
                        PassManager.getProgression(passGUI.pitPlayer, toDisplay));
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                itemStack.setItemMeta(itemMeta);
                getInventory().setItem(slot, itemStack);
            } else {
                ItemStack itemStack = new ItemStack(Material.AIR);
                getInventory().setItem(slot, itemStack);
            }
        }
    }

    public int getDailyPages() {
        List<PassQuest> dailyQuests = PassManager.getDailyQuests();
        dailyQuests.removeIf(passQuest -> !passQuest.canProgressQuest(passGUI.pitPlayer));
        return (dailyQuests.size() - 1) / dailyQuestSlots.size() + 1;
    }

    public int getWeeklyPages() {
        List<PassQuest> weeklyQuests = PassManager.getWeeklyQuests();
        weeklyQuests.removeIf(passQuest -> !passQuest.canProgressQuest(passGUI.pitPlayer));
        return (weeklyQuests.size() - 1) / weeklyQuestSlots.size() + 1;
    }

    @Override
    public String getName() {
        return "" + ChatColor.GOLD + ChatColor.BOLD + "天坑" + ChatColor.YELLOW + ChatColor.BOLD + "乱斗 " + ChatColor.DARK_AQUA + ChatColor.BOLD + "任务";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory().getHolder() != this) return;
        int slot = event.getSlot();

        if(slot == 4) {
            player.closeInventory();
            AOutput.send(player, "&e&l高级通行证!&7 请前往商店购买 &6&l天坑&e&l乱斗 &3&l通行证&7");
        } else if(slot == 2) {
            if(dailyQuestPage < getDailyPages()) {
                dailyQuestPage++;
            } else {
                dailyQuestPage = 1;
            }
            setWeeklyQuestPage(weeklyQuestPage);
        } else if(slot == 6) {
            if(weeklyQuestPage < getWeeklyPages()) {
                weeklyQuestPage++;
            } else {
                weeklyQuestPage = 1;
            }
            setWeeklyQuestPage(weeklyQuestPage);
        } else if(slot == 49) {
            openPreviousGUI();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        setDailyQuestPage(dailyQuestPage);
        setWeeklyQuestPage(weeklyQuestPage);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
