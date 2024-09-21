package me.wisdom.thepit.inventories;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.darkzone.DarkzoneBalancing;
import me.wisdom.thepit.enums.AChatColor;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.settings.SettingsGUI;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatColorPanel extends AGUIPanel {
    PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
    public static Map<Player, AChatColor> playerChatColors = new HashMap<>();

    public SettingsGUI settingsGUI;

    public ChatColorPanel(AGUI gui) {
        super(gui);
        settingsGUI = (SettingsGUI) gui;

        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 8);
    }

    @Override
    public String getName() {
        return "Chat Colors";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void onClick(InventoryClickEvent event) {

        int slot = event.getSlot();
        if(event.getClickedInventory().getHolder() == this) {

            if(Misc.isAirOrNull(getInventory().getItem(slot))) return;

            if(slot == 31) {
                openPanel(settingsGUI.getHomePanel());
            }

            for(AChatColor chatColor : AChatColor.values()) {
                if(getInventory().getItem(slot).getType().equals(chatColor.material) &&
                        getInventory().getItem(slot).getDurability() == chatColor.data) {

                    if(playerChatColors.containsKey(player) && playerChatColors.get(player).equals(chatColor)) {
                        Sounds.ERROR.play(player);
                        AOutput.error(player, "&cThat chat color is already equipped");
                    } else {
                        playerChatColors.put(player, chatColor);
                        pitPlayer.chatColor = chatColor;
                        Sounds.SUCCESS.play(player);
                        openPanel(settingsGUI.chatColorPanel);
                    }
                }
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

        getInventory().setItem(31, back);

        int i = 10;

        for(AChatColor chatColor : AChatColor.values()) {
            ItemStack color = new ItemStack(chatColor.material, 1, (short) chatColor.data);
            ItemMeta colormeta = color.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.GRAY + "Color: " + chatColor.chatColor + chatColor.refName);
            lore.add(ChatColor.GRAY + "Code: " + ChatColor.WHITE + "&" + chatColor.code);
            lore.add("");
            if(playerChatColors.get(player) == chatColor) {
                lore.add(ChatColor.GREEN + "Already selected!");
                colormeta.setDisplayName(ChatColor.GREEN + chatColor.refName + " Chat Color");
                colormeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 0, false);
                colormeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } else {
                lore.add(ChatColor.YELLOW + "Click to select!");
                colormeta.setDisplayName(ChatColor.YELLOW + chatColor.refName + " Chat Color");
            }
            colormeta.setLore(lore);
            color.setItemMeta(colormeta);

            getInventory().setItem(i, color);

            i++;
            if(i == 18 || i == 26) {
                i++;
            }
            if(i == 17) i = i + 2;
        }

    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
