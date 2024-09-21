package me.wisdom.thepit.inventories;

import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.settings.SettingsGUI;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ChatOptionsPanel extends AGUIPanel {
    PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

    public SettingsGUI settingsGUI;

    public ChatOptionsPanel(AGUI gui) {
        super(gui);
        settingsGUI = (SettingsGUI) gui;

        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 8);
    }

    @Override
    public String getName() {
        return "Chat Options";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(NonManager.getNon(player) != null) return;
        int slot = event.getSlot();
        if(event.getClickedInventory().getHolder() == this) {

            if(slot == 22) openPanel(settingsGUI.getHomePanel());

            if(!player.hasPermission("pitsim.chat")) return;

            if(slot == 10) {
                if(pitPlayer.bountiesDisabled) {
                    pitPlayer.bountiesDisabled = false;
                } else {
                    pitPlayer.bountiesDisabled = true;
                }
                Sounds.SUCCESS.play(player);
                openPanel(settingsGUI.chatOptionsPanel);
            } else if(slot == 12) {
                if(pitPlayer.streaksDisabled) {
                    pitPlayer.streaksDisabled = false;
                } else {
                    pitPlayer.streaksDisabled = true;
                }
                Sounds.SUCCESS.play(player);
                openPanel(settingsGUI.chatOptionsPanel);
            } else if(slot == 14) {
                if(pitPlayer.killFeedDisabled) {
                    pitPlayer.killFeedDisabled = false;
                } else {
                    pitPlayer.killFeedDisabled = true;
                }
                Sounds.SUCCESS.play(player);
                openPanel(settingsGUI.chatOptionsPanel);
            } else if(slot == 16) {
                if(pitPlayer.playerChatDisabled) {
                    pitPlayer.playerChatDisabled = false;
                } else {
                    pitPlayer.playerChatDisabled = true;
                }
                Sounds.SUCCESS.play(player);
                openPanel(settingsGUI.chatOptionsPanel);
            }

        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backmeta = back.getItemMeta();
        backmeta.setDisplayName(ChatColor.GREEN + "Go Back");
        List<String> backlore = new ArrayList<>();
        backlore.add(ChatColor.GRAY + "To Donator Perks");
        backmeta.setLore(backlore);
        back.setItemMeta(backmeta);

        ItemStack bounty = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta bountymeta = bounty.getItemMeta();
        List<String> bountylore = new ArrayList<>();
        bountylore.add(ChatColor.GRAY + "Toggles bounty announcements");
        bountylore.add(ChatColor.GRAY + "and claims");
        bountylore.add("");
        if(pitPlayer.bountiesDisabled) bountylore.add(ChatColor.GRAY + "Enabled: " + ChatColor.RED + "OFF");
        else bountylore.add(ChatColor.GRAY + "Enabled: " + ChatColor.GREEN + "ON");
        bountylore.add(ChatColor.YELLOW + "Click to toggle!");
        bountymeta.setDisplayName(ChatColor.YELLOW + "Chat Option: Bounties");
        bountymeta.setLore(bountylore);
        bounty.setItemMeta(bountymeta);

        ItemStack streak = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemMeta streakmeta = streak.getItemMeta();
        List<String> streaklore = new ArrayList<>();
        streaklore.add(ChatColor.GRAY + "Toggles streak broadcasts");
        streaklore.add("");
        if(pitPlayer.streaksDisabled) streaklore.add(ChatColor.GRAY + "Enabled: " + ChatColor.RED + "OFF");
        else streaklore.add(ChatColor.GRAY + "Enabled: " + ChatColor.GREEN + "ON");
        streaklore.add(ChatColor.YELLOW + "Click to toggle!");
        streakmeta.setDisplayName(ChatColor.YELLOW + "Chat Option: Streaks");
        streakmeta.setLore(streaklore);
        streak.setItemMeta(streakmeta);

        ItemStack kill = new ItemStack(Material.IRON_SWORD);
        ItemMeta killmeta = kill.getItemMeta();
        List<String> killlore = new ArrayList<>();
        killlore.add(ChatColor.GRAY + "Toggles kill, assist, and");
        killlore.add(ChatColor.GRAY + "death messages");
        killlore.add("");
        killmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if(pitPlayer.killFeedDisabled) killlore.add(ChatColor.GRAY + "Enabled: " + ChatColor.RED + "OFF");
        else killlore.add(ChatColor.GRAY + "Enabled: " + ChatColor.GREEN + "ON");
        killlore.add(ChatColor.YELLOW + "Click to toggle!");
        killmeta.setDisplayName(ChatColor.YELLOW + "Chat Option: Kill Feed");
        killmeta.setLore(killlore);
        kill.setItemMeta(killmeta);

        ItemStack chat = new ItemStack(Material.PAPER);
        ItemMeta chatmeta = chat.getItemMeta();
        List<String> chatlore = new ArrayList<>();
        chatlore.add(ChatColor.GRAY + "Toggles chat messages sent from");
        chatlore.add(ChatColor.GRAY + "players and /show messages");
        chatlore.add("");
        if(pitPlayer.playerChatDisabled) chatlore.add(ChatColor.GRAY + "Enabled: " + ChatColor.RED + "OFF");
        else chatlore.add(ChatColor.GRAY + "Enabled: " + ChatColor.GREEN + "ON");
        chatlore.add(ChatColor.YELLOW + "Click to toggle!");
        chatmeta.setDisplayName(ChatColor.YELLOW + "Chat Option: Player Chat");
        chatmeta.setLore(chatlore);
        chat.setItemMeta(chatmeta);

        getInventory().setItem(10, bounty);
        getInventory().setItem(12, streak);
        getInventory().setItem(14, kill);
        getInventory().setItem(16, chat);
        getInventory().setItem(22, back);
        updateInventory();
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
