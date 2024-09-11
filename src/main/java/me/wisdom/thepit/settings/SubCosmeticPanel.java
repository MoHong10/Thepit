package me.wisdom.thepit.settings;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.cosmetics.ColorableCosmetic;
import me.wisdom.thepit.cosmetics.CosmeticManager;
import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepit.cosmetics.particles.ParticleColor;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class SubCosmeticPanel extends AGUIPanel {
    public SettingsGUI settingsGUI;

    public static ItemStack previousPageItem;
    public static ItemStack nextPageItem;
    public static ItemStack backItem;
    public ItemStack disableItem;

    public CosmeticType cosmeticType;
    public int slot;

    public int page = 1;
    public static List<Integer> cosmeticSlots = new ArrayList<>();
    public Map<Integer, PitCosmetic> cosmeticMap = new HashMap<>();

    static {
        previousPageItem = new AItemStackBuilder(Material.PAPER)
                .setName("&f&l上一页")
                .setLore(new ALoreBuilder(
                        "&7点击查看上一页"
                ))
                .getItemStack();

        nextPageItem = new AItemStackBuilder(Material.PAPER)
                .setName("&f&l下一页")
                .setLore(new ALoreBuilder(
                        "&7点击查看下一页"
                ))
                .getItemStack();

        backItem = new AItemStackBuilder(Material.BARRIER)
                .setName("&c返回")
                .setLore(new ALoreBuilder(
                        "&7点击返回到上一屏幕"
                ))
                .getItemStack();

        for(int i = 9; i < 45; i++) {
            if(i % 9 == 0 || (i + 1) % 9 == 0) continue;
            cosmeticSlots.add(i);
        }
    }

    public SubCosmeticPanel(AGUI gui, CosmeticType cosmeticType) {
        super(gui, true);
        settingsGUI = (SettingsGUI) gui;
        this.cosmeticType = cosmeticType;
        this.slot = cosmeticType.getSettingsGUISlot();
        buildInventory();

        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 7);

        getInventory().setItem(getRows() * 9 - 5, backItem);

        PitCosmetic activeCosmetic = CosmeticManager.getEquippedCosmetic(settingsGUI.pitPlayer, cosmeticType);
        if(activeCosmetic != null) {
            ItemStack disableItem = activeCosmetic.getDisplayStack(true);
            new AItemStackBuilder(disableItem)
                    .setName("&c&l禁用")
                    .setLore(new ALoreBuilder(
                            "&7点击禁用你当前激活的装饰"
                    ));
            getInventory().setItem(getRows() * 9 - 4, disableItem);
        }

        if(getPages() != 1) {
            getInventory().setItem(getRows() * 9 - 9, previousPageItem);
            getInventory().setItem(getRows() * 9 - 1, nextPageItem);
        }

        for(int i = 10; i < settingsGUI.getRows(this) * 9 - 9; i++) {
            if(i % 9 == 0 || (i + 1) % 9 == 0) continue;
            cosmeticMap.put(i, null);
        }

        setPage(1);
    }

    public void deselectCosmetic(CosmeticType cosmeticType) {
        PitPlayer.EquippedCosmeticData cosmeticData = settingsGUI.pitPlayer.equippedCosmeticMap.get(cosmeticType.name());
        if(cosmeticData != null) {
            PitCosmetic pitCosmetic = CosmeticManager.getCosmetic(cosmeticData.refName);
            if(pitCosmetic != null) pitCosmetic.disable(settingsGUI.pitPlayer);
        }
        settingsGUI.pitPlayer.equippedCosmeticMap.put(cosmeticType.name(), null);
    }

    public boolean selectCosmetic(PitCosmetic pitCosmetic) {
        return selectCosmetic(pitCosmetic, null);
    }

    public boolean selectCosmetic(PitCosmetic pitCosmetic, ParticleColor particleColor) {
        PitPlayer.EquippedCosmeticData cosmeticData = settingsGUI.pitPlayer.equippedCosmeticMap.get(cosmeticType.name());
        if(cosmeticData != null) {
            if(!pitCosmetic.isColorCosmetic) {
                if(pitCosmetic.refName.equals(cosmeticData.refName)) {
                    Sounds.NO.play(player);
                    return false;
                }
                deselectCosmetic(pitCosmetic.cosmeticType);
            } else {
                if(pitCosmetic.refName.equals(cosmeticData.refName)) {
                    if(particleColor == cosmeticData.particleColor) {
                        Sounds.NO.play(player);
                        return false;
                    }
                }
                deselectCosmetic(pitCosmetic.cosmeticType);
                ColorableCosmetic colorableCosmetic = (ColorableCosmetic) pitCosmetic;
                colorableCosmetic.setParticleColor(player, particleColor);
            }
        }
        settingsGUI.pitPlayer.equippedCosmeticMap.put(cosmeticType.name(), new PitPlayer.EquippedCosmeticData(pitCosmetic.refName, particleColor));
        pitCosmetic.enable(settingsGUI.pitPlayer);
        return true;
    }

    public void setPage(int page) {
        this.page = page;

        List<PitCosmetic> unlockedCosmetics = CosmeticManager.getUnlockedCosmetics(settingsGUI.pitPlayer, cosmeticType);
        int cosmeticListSize = unlockedCosmetics.size();
        for(int i = cosmeticSlots.size() * (page - 1); i < cosmeticSlots.size() * page; i++) {
            int slot = cosmeticSlots.get(i % cosmeticSlots.size());
            if(i >= cosmeticListSize) {
                cosmeticMap.put(slot, null);
                continue;
            }

            PitCosmetic pitCosmetic = unlockedCosmetics.get(i);
            cosmeticMap.put(slot, pitCosmetic);
            getInventory().setItem(slot, pitCosmetic.getDisplayStack(pitCosmetic.isEnabled(settingsGUI.pitPlayer)));
        }
    }

    @Override
    public String getName() {
        return cosmeticType.getPanelName();
    }

    @Override
    public int getRows() {
        SettingsGUI settingsGUI = (SettingsGUI) gui;
        return settingsGUI.getRows(this);
    }

    public int getPages() {
        return settingsGUI.getPages(this);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory().getHolder() != this) return;
        int slot = event.getSlot();

        if(cosmeticMap.get(slot) != null) {
            PitCosmetic pitCosmetic = cosmeticMap.get(slot);
            if(pitCosmetic.isColorCosmetic) {
                openPanel(new ColorCosmeticPanel(gui, this, pitCosmetic));
            } else {
                boolean success = selectCosmetic(pitCosmetic);
                if(success) {
                    player.closeInventory();
                    Sounds.SUCCESS.play(player);
                }
            }
        } else if(slot == getRows() * 9 - 9) {
            if(page > 1) {
                setPage(page - 1);
            } else {
                Sounds.NO.play(player);
            }
        } else if(slot == getRows() * 9 - 5) {
            openPreviousGUI();
        } else if(slot == getRows() * 9 - 4) {
            PitPlayer.EquippedCosmeticData cosmeticData = settingsGUI.pitPlayer.equippedCosmeticMap.get(cosmeticType.name());
            if(cosmeticData != null) {
                deselectCosmetic(cosmeticType);
                player.closeInventory();
                Sounds.SUCCESS.play(player);
            }
        } else if(slot == getRows() * 9 - 1) {
            if(page < settingsGUI.getPages(this)) {
                setPage(page + 1);
            } else {
                Sounds.NO.play(player);
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {}

    @Override
    public void onClose(InventoryCloseEvent event) {}
}
