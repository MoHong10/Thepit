package me.wisdom.thepit.inventories;

import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.items.TemporaryItem;
import me.wisdom.thepit.items.misc.ChunkOfVile;
import me.wisdom.thepit.logging.LogManager;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class VilePanel extends AGUIPanel {

    public Map<Integer, Integer> slots = new HashMap<>();
    public VileGUI vileGUI;

    public VilePanel(AGUI gui) {
        super(gui);
        vileGUI = (VileGUI) gui;

        int nextSlot = 0;
        for(int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if(Misc.isAirOrNull(itemStack)) continue;
            itemStack = itemStack.clone();
            if(!MysticFactory.hasLives(itemStack)) continue;

            PitItem pitItem = ItemFactory.getItem(itemStack);
            assert pitItem != null;
            TemporaryItem temporaryItem = pitItem.getAsTemporaryItem();

            if(temporaryItem.isAtMaxLives(itemStack) || temporaryItem.getLives(itemStack) == 0) continue;

            ItemMeta itemMeta = itemStack.getItemMeta();
            ALoreBuilder loreBuilder = new ALoreBuilder(itemMeta.getLore()).addLore(
                    "",
                    "&eClick to repair!"
            );
            new AItemStackBuilder(itemStack).setLore(loreBuilder);

            getInventory().setItem(nextSlot, itemStack);
            slots.put(nextSlot++, i);
        }
    }

    @Override
    public String getName() {
        return "Choose an item to repair";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void onClick(InventoryClickEvent event) {

        if(Misc.isAirOrNull(event.getCurrentItem())) {
            player.closeInventory();
            return;
        }

        ItemStack vileStack = player.getItemInHand();
        PitItem pitVile = ItemFactory.getItem(vileStack);
        if(!(pitVile instanceof ChunkOfVile)) {
            player.closeInventory();
            return;
        }

        int slot = event.getSlot();
        if(event.getClickedInventory().getHolder() == this) {

            if(!slots.containsKey(slot)) return;
            int invSlot = slots.get(slot);

            ItemStack itemStack = player.getInventory().getItem(invSlot);

            PitItem pitRepair = ItemFactory.getItem(itemStack);
            assert pitRepair != null;
            TemporaryItem temporaryItem = pitRepair.getAsTemporaryItem();

            temporaryItem.addLives(itemStack, 1);
            player.getInventory().setItem(invSlot, itemStack);
            player.closeInventory();

            TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&5WITHERCRAFT!&7 Repaired "));
            message.addExtra(Misc.createItemHover(itemStack));
            message.addExtra(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7!")));
            player.spigot().sendMessage(message);

            LogManager.onItemRepair(player, itemStack);

            Sounds.WITHERCRAFT_1.play(player);
            Sounds.WITHERCRAFT_2.play(player);

            if(vileStack.getAmount() == 1) {
                player.setItemInHand(new ItemStack(Material.AIR));
            } else {
                vileStack.setAmount(vileStack.getAmount() - 1);
                player.setItemInHand(vileStack);
            }
        }
        updateInventory();
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }
}
