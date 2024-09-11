package me.wisdom.thepit.storage;

import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.PlayerItemLocation;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WardrobePanel extends AGUIPanel {
    public StorageProfile profile;

    public WardrobePanel(AGUI gui) {
        super(gui, true);
        this.profile = StorageManager.getProfile(player);
        addBackButton(getRows() * 9 - 5);
        buildInventory();
        setInventory();
        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 15, false);

        for (int i = 0; i < 9; i++) {
            Outfit outfit = profile.getOutfits()[i];
            Outfit.OutfitState outfitState = outfit.getState();

            addTaggedItem(i + 9, outfit::getDisplayItem, event -> {
            });
            if (outfit.isUnlocked()) {
                ItemStack settingsStack = new AItemStackBuilder(Material.REDSTONE_COMPARATOR)
                        .setName("&2装备 " + (outfit.getIndex() + 1) + " 设置")
                        .setLore(new PitLoreBuilder(
                                "&7点击打开此 &2装备 的设置"
                        ))
                        .getItemStack();
                addTaggedItem(i + 18, () -> settingsStack, event -> openPanel(new OutfitSettingsPanel(gui, outfit))).setItem();
            } else {
                ItemStack settingsStack = new AItemStackBuilder(Material.STAINED_GLASS_PANE, 1, 15)
                        .getItemStack();
                getInventory().setItem(i + 9, settingsStack);
            }
            if (outfitState.isEquippable()) {
                addTaggedItem(i + 27, outfit::getStateItem, event -> {
                    boolean success = outfit.equip(true);
                    if (success) {
                        AOutput.send(player, "&2&l衣柜！&7 装备了 &2装备 " + (outfit.getIndex() + 1) + "&7！");
                        Sounds.SUCCESS.play(player);
                        player.closeInventory();
                    }
                }).setItem();
            } else {
                addTaggedItem(i + 27, outfit::getStateItem, event -> {
                    if (outfitState == Outfit.OutfitState.NO_DATA) {
                        AOutput.error(player, "&c&l错误！&7 此 &2装备&7 没有保存数据！");
                    } else if (outfitState == Outfit.OutfitState.LOCKED) {
                        AOutput.error(player, "&c&l错误！&7 你需要更高的等级！");
                    }
                    Sounds.NO.play(player);
                }).setItem();
            }
        }

        addTaggedItem(39, () -> new AItemStackBuilder(Material.HOPPER)
                .setName("&c清空物品栏")
                .setLore(new PitLoreBuilder(
                        "&7 将你的物品栏和盔甲放入末影箱"
                )).getItemStack(), event -> {
            Map<PlayerItemLocation, ItemStack> proposedChanges = new HashMap<>();
            boolean success = profile.storeInvAndArmor(proposedChanges, new ArrayList<>(), true);
            if (success) {
                for (Map.Entry<PlayerItemLocation, ItemStack> entry : proposedChanges.entrySet())
                    entry.getKey().setItem(profile.getUniqueID(), entry.getValue(), true);
                player.updateInventory();
                AOutput.send(player, "&2&l衣柜！&7 已将你的物品栏和盔甲移入末影箱！");
                Sounds.SUCCESS.play(player);
            }
        });

        addTaggedItem(41, () -> new AItemStackBuilder(Material.BARRIER)
                .setName("&c清除默认设置")
                .setLore(new PitLoreBuilder(
                        "&7 清除默认 &a世界 &7 和 &5黑暗区域 &7 设置"
                )).getItemStack(), event -> {
            profile.setDefaultOverworldSet(-1);
            profile.setDefaultDarkzoneSet(-1);
            setInventory();
            AOutput.send(player, "&2&l衣柜！&7 已清除默认 &a世界 &7 和 &5黑暗区域 &7 设置！");
            Sounds.SUCCESS.play(player);
        });
    }

    @Override
    public void setInventory() {
        super.setInventory();
        for(TaggedItem taggedItem : taggedItemMap.values()) taggedItem.setItem();
        updateInventory();
    }

    @Override
    public String getName() {
        return "&2衣柜";
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory().getHolder() != this) return;
        int slot = event.getSlot();
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        setInventory();
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }
}