package me.wisdom.thepit.storage;

import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class OutfitSettingsPanel extends AGUIPanel {
    public StorageProfile profile;
    public Outfit outfit;

    public OutfitSettingsPanel(AGUI gui, Outfit outfit) {
        super(gui, true);
        this.profile = StorageManager.getProfile(player);
        this.outfit = outfit;
        addBackButton(getRows() * 9 - 5);
        buildInventory();
        setInventory();
        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 15, false);
        inventoryBuilder.setSlots(Material.STAINED_GLASS_PANE, 15, 11, 12, 13, 14, 15, 16);

        Outfit.OutfitState outfitState = outfit.getState();

        addTaggedItem(10, () -> new AItemStackBuilder(Material.DIAMOND_BLOCK)
                .setName("&a保存装备")
                .setLore(new PitLoreBuilder(
                        "&7将你当前的物品栏和盔甲保存到此 &2装备"
                ))
                .getItemStack(), event -> {
            outfit.save();
            AOutput.send(player, "&2&l衣柜！&2 装备 " + (outfit.getIndex() + 1) + " &7已保存！");
            Sounds.SUCCESS.play(player);
            player.closeInventory();
        }).setItem();

        addTaggedItem(11, () -> new AItemStackBuilder(Material.HOPPER)
                .setName("&c清除装备")
                .setLore(new PitLoreBuilder(
                        "&7清除此 &2装备"
                ))
                .getItemStack(), event -> {
            outfit.clear();
            AOutput.send(player, "&2&l衣柜！&2 装备 " + (outfit.getIndex() + 1) + " &7已清除！");
            Sounds.SUCCESS.play(player);
            openPreviousGUI();
        }).setItem();

        addTaggedItem(13, () -> new AItemStackBuilder(outfit.getDisplayItem())
                .setName("&e设置显示物品")
                .setLore(new PitLoreBuilder(
                        "&7点击将显示物品设置为你正在持有的物品"
                ))
                .getItemStack(), event -> {
            ItemStack itemStack = player.getItemInHand().clone();
            itemStack.setAmount(1);
            outfit.setDisplayItem(itemStack);
            AOutput.send(player, "&2&l衣柜！&7 更新了 &2装备 " + (outfit.getIndex() + 1) + " &7的显示物品！");
            Sounds.SUCCESS.play(player);
            openPreviousGUI();
        }).setItem();

        ALoreBuilder setOverworldLore = new ALoreBuilder();
        Consumer<InventoryClickEvent> setOverworldClick = event -> {
            profile.setDefaultOverworldSet(outfit.getIndex());
            AOutput.send(player, "&2&l装备！&7 将你的默认 &a世界 &2装备 &7设置为 &2装备 " + (outfit.getIndex() + 1));
            Sounds.SUCCESS.play(player);
            openPreviousGUI();
        };
        if(!outfitState.isEquippable()) {
            setOverworldLore.addLore(
                    "&c无法将空装备",
                    "&7设置为默认"
            );
            setOverworldClick = null;
        } else if(profile.getDefaultOverworldSet() == -1) {
            setOverworldLore.addLore("&7当前: 无！");
        } else if(profile.getDefaultOverworldSet() == outfit.getIndex()) {
            setOverworldLore.addLore("&7当前: &2这个！");
            setOverworldClick = null;
        } else {
            setOverworldLore.addLore("&7当前: &2装备 " + (profile.getDefaultOverworldSet() + 1));
        }
        ItemStack setOverworldBaseStack = new ItemStack(Material.STAINED_CLAY, 1, (short) 13);
        if(!outfitState.isEquippable()) setOverworldBaseStack.setType(Material.BARRIER);
        addTaggedItem(15, () -> new AItemStackBuilder(setOverworldBaseStack)
                .setName("&7设置默认 &a世界 &2装备")
                .setLore(setOverworldLore)
                .getItemStack(), setOverworldClick).setItem();

        ALoreBuilder setDarkzoneLore = new ALoreBuilder();
        Consumer<InventoryClickEvent> setDarkzoneClick = event -> {
            profile.setDefaultDarkzoneSet(outfit.getIndex());
            AOutput.send(player, "&2&l装备！&7 将你的默认 &5黑暗区域 &2装备 &7设置为 &2装备 " + (outfit.getIndex() + 1));
            Sounds.SUCCESS.play(player);
            openPreviousGUI();
        };
        if(!outfitState.isEquippable()) {
            setDarkzoneLore.addLore(
                    "&c无法将空装备",
                    "&7设置为默认"
            );
            setDarkzoneClick = null;
        } else if(profile.getDefaultDarkzoneSet() == -1) {
            setDarkzoneLore.addLore("&7当前: 无！");
        } else if(profile.getDefaultDarkzoneSet() == outfit.getIndex()) {
            setDarkzoneLore.addLore("&7当前: &2这个！");
            setDarkzoneClick = null;
        } else {
            setDarkzoneLore.addLore("&7当前: &2装备 " + (profile.getDefaultDarkzoneSet() + 1));
        }
        ItemStack setDarkzoneBaseStack = new ItemStack(Material.STAINED_CLAY, 1, (short) 10);
        if(!outfitState.isEquippable()) setDarkzoneBaseStack.setType(Material.BARRIER);
        addTaggedItem(16, () -> new AItemStackBuilder(setDarkzoneBaseStack)
                .setName("&7设置默认 &5黑暗区域 &2装备")
                .setLore(setDarkzoneLore)
                .getItemStack(), setDarkzoneClick).setItem();
    }

    @Override
    public String getName() {
        return "&2装备设置";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory().getHolder() != this) return;
        int slot = event.getSlot();
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }
}
