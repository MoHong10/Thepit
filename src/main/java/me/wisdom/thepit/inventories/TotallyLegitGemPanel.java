package me.wisdom.thepit.inventories;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.items.misc.TotallyLegitGem;
import me.wisdom.thepit.logging.LogManager;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TotallyLegitGemPanel extends AGUIPanel {
    public Map<Integer, Integer> slots = new HashMap<>();
    public GemGUI gemGUI;

    public TotallyLegitGemPanel(AGUI gui) {
        super(gui);
        gemGUI = (GemGUI) gui;

    }

    @Override
    public String getName() {
        return "Totally Legit Selector";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        int slot = event.getSlot();

        if(Misc.isAirOrNull(event.getCurrentItem())) return;

        ItemStack gemStack = player.getItemInHand();
        PitItem pitItem = ItemFactory.getItem(gemStack);
        if(!(pitItem instanceof TotallyLegitGem)) {
            player.closeInventory();
            return;
        }

        if(event.getClickedInventory().getHolder() == this) {

            int invSlot;
            if(slots.containsKey(slot)) {
                invSlot = slots.get(slot);
            } else return;

            for(int i = 0; i < player.getInventory().getSize(); i++) {
                if(i == invSlot) {
                    NBTItem nbtItem = new NBTItem(player.getInventory().getItem(i));
                    nbtItem.setBoolean(NBTTag.IS_GEMMED.getRef(), true);

                    PitEnchant enchant = null;
                    Map<PitEnchant, Integer> enchants = EnchantManager.getEnchantsOnItem(nbtItem.getItem());
                    for(Map.Entry<PitEnchant, Integer> entry : enchants.entrySet()) {
                        if(entry.getValue() == 2) enchant = entry.getKey();
                    }

                    EnchantManager.setItemLore(nbtItem.getItem(), player);
                    try {
                        player.getInventory().setItem(i, EnchantManager.addEnchant(nbtItem.getItem(), enchant, 3, false));
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    player.closeInventory();
                    Sounds.GEM_USE.play(player);
                    LogManager.onItemGem(player, nbtItem.getItem());

                    PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
                    pitPlayer.stats.itemsGemmed++;

                    if(gemStack.getAmount() == 1) {
                        player.setItemInHand(new ItemStack(Material.AIR));
                    } else {
                        gemStack.setAmount(gemStack.getAmount() - 1);
                        player.setItemInHand(gemStack);
                    }
                }
            }
        }
        updateInventory();
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        int slot = 0;

        for(int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if(Misc.isAirOrNull(item)) continue;

            NBTItem nbtItem = new NBTItem(item);
            if(nbtItem.hasKey(NBTTag.ITEM_JEWEL_ENCHANT.getRef()) && !nbtItem.hasKey(NBTTag.IS_GEMMED.getRef())) {
                PitEnchant enchant = null;
                if(nbtItem.getInteger(NBTTag.ITEM_ENCHANT_NUM.getRef()) < 3 || nbtItem.getInteger(NBTTag.ITEM_TOKENS.getRef()) < 8)
                    continue;
                Map<PitEnchant, Integer> enchants = EnchantManager.getEnchantsOnItem(nbtItem.getItem());
                for(Map.Entry<PitEnchant, Integer> entry : enchants.entrySet()) {
                    if(entry.getValue() == 2) enchant = entry.getKey();
                }

                if(enchant == null || enchant.isRare) continue;
                ItemMeta meta = nbtItem.getItem().getItemMeta();
                List<String> lore = meta.getLore();
                lore.add("");
                lore.add(ChatColor.YELLOW + "Click to upgrade!");
                meta.setLore(lore);
                nbtItem.getItem().setItemMeta(meta);
                getInventory().setItem(slot, nbtItem.getItem());
                slots.put(slot, i);
                slot++;
            }

        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }

}
