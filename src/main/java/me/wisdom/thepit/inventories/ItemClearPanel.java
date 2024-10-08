package me.wisdom.thepit.inventories;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.items.misc.AncientGemShard;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ItemClearPanel extends AGUIPanel {
    public PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
    public Map<Integer, Integer> slots = new HashMap<>();
    public RenownShopGUI renownShopGUI;

    public ItemClearPanel(AGUI gui) {
        super(gui);
        renownShopGUI = (RenownShopGUI) gui;

    }

    @Override
    public String getName() {
        return "Choose an item to clear";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        int slot = event.getSlot();

        if(event.getClickedInventory().getHolder() == this) {

            int invSlot = slots.get(slot);

            for(int i = 0; i < player.getInventory().getSize(); i++) {
                if(i == invSlot) {
                    NBTItem nbtItem = new NBTItem(player.getInventory().getItem(i));

                    AOutput.send(player, "&5&lWITHERCRAFT!&7 Cleared enchants from " + nbtItem.getItem().getItemMeta().getDisplayName() + "&7!");

                    if(nbtItem.hasKey(NBTTag.IS_GEMMED.getRef())) {
                        nbtItem.removeKey(NBTTag.IS_GEMMED.getRef());

                        Random r = new Random();
                        int low = 32;
                        int high = 64;
                        int result = r.nextInt(high - low) + low;

                        AUtil.giveItemSafely(player, ItemFactory.getItem(AncientGemShard.class).getItem(result), true);

                        AOutput.send(player, "&7Received &f" + result + " &aAncient Gem Shards&7.");
                        Sounds.SHARD_FIND.play(player);

                    }

                    EnchantManager.setItemLore(nbtItem.getItem(), player);

                    Map<PitEnchant, Integer> enchants = EnchantManager.getEnchantsOnItem(nbtItem.getItem());
                    ItemStack item1 = null;
                    int removedEnchants = 0;
                    for(Map.Entry<PitEnchant, Integer> entry : enchants.entrySet()) {

                        if((EnchantManager.getEnchant(nbtItem.getString(NBTTag.ITEM_JEWEL_ENCHANT.getRef())) == entry.getKey()))
                            continue;
                        try {
                            if(item1 == null)
                                item1 = EnchantManager.addEnchant(nbtItem.getItem(), entry.getKey(), 0, true);
                            else
                                player.getInventory().setItem(i, EnchantManager.addEnchant(item1, entry.getKey(), 0, true));
                            removedEnchants++;
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                    }
                    if(removedEnchants == 1) player.getInventory().setItem(i, item1);

                    pitPlayer.renown -= 5;

                    player.closeInventory();
                    Sounds.CLEAR_JEWEL.play(player);
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
            if(nbtItem.hasKey(NBTTag.ITEM_JEWEL_ENCHANT.getRef())) {
                PitEnchant enchant = null;
                if(nbtItem.getInteger(NBTTag.ITEM_ENCHANT_NUM.getRef()) < 2) continue;

                ItemMeta meta = nbtItem.getItem().getItemMeta();
                List<String> lore = meta.getLore();
                lore.add("");
                lore.add(ChatColor.YELLOW + "Clear for 5 renown!");
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
