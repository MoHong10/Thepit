package me.wisdom.thepit.settings;

import me.wisdom.thepit.cosmetics.CosmeticManager;
import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CosmeticPanel extends AGUIPanel {
    public SettingsGUI settingsGUI;

    public ItemStack playerKillEffects;
    public ItemStack botKillEffects;
    public ItemStack bountyMessageItem;
    public ItemStack capesItem;
    public ItemStack particleTrailItem;
    public ItemStack auraItem;
    public ItemStack miscItem;
    public static ItemStack backItem;

    static {
        backItem = new AItemStackBuilder(Material.BARRIER)
                .setName("&c返回")
                .setLore(new ALoreBuilder(
                        "&7点击返回"
                ))
                .getItemStack();
    }

    public CosmeticPanel(AGUI gui) {
        super(gui);
        settingsGUI = (SettingsGUI) gui;

        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 7);

        List<CosmeticType> equippedCosmeticTypes = new ArrayList<>();
        for(PitCosmetic pitCosmetic : CosmeticManager.getEquippedCosmetics(settingsGUI.pitPlayer))
            equippedCosmeticTypes.add(pitCosmetic.cosmeticType);

        playerKillEffects = new AItemStackBuilder(Material.DIAMOND_SWORD)
                .setName("&4&l玩家击杀效果")
                .setLore(new ALoreBuilder(
                        "&7点击选择你的玩家击杀效果"
                ))
                .getItemStack();
        botKillEffects = new AItemStackBuilder(Material.IRON_SWORD)
                .setName("&c&l机器人击杀效果")
                .setLore(new ALoreBuilder(
                        "&7点击选择你的机器人击杀效果"
                ))
                .getItemStack();
        bountyMessageItem = new AItemStackBuilder(Material.GOLD_INGOT)
                .setName("&6&l悬赏消息")
                .setLore(new ALoreBuilder(
                        "&7点击选择你的悬赏领取消息"
                ))
                .getItemStack();
        capesItem = new AItemStackBuilder(Material.BANNER, 1, 15)
                .setName("&f&l披风")
                .setLore(new ALoreBuilder(
                        "&7点击选择你的披风"
                ))
                .getItemStack();
        particleTrailItem = new AItemStackBuilder(Material.FIREWORK)
                .setName("&e&l粒子轨迹")
                .setLore(new ALoreBuilder(
                        "&7点击选择你的粒子轨迹"
                ))
                .getItemStack();
        auraItem = new AItemStackBuilder(Material.BEACON)
                .setName("&a&l光环")
                .setLore(new ALoreBuilder(
                        "&7点击选择你的光环"
                ))
                .getItemStack();
        miscItem = new AItemStackBuilder(Material.LAVA_BUCKET)
                .setName("&e&l杂项")
                .setLore(new ALoreBuilder(
                        "&7点击选择其他装饰"
                ))
                .getItemStack();

        if(equippedCosmeticTypes.contains(CosmeticType.PLAYER_KILL_EFFECT)) Misc.addEnchantGlint(playerKillEffects);
        if(equippedCosmeticTypes.contains(CosmeticType.BOT_KILL_EFFECT)) Misc.addEnchantGlint(botKillEffects);
        if(equippedCosmeticTypes.contains(CosmeticType.BOUNTY_CLAIM_MESSAGE)) Misc.addEnchantGlint(bountyMessageItem);
        if(equippedCosmeticTypes.contains(CosmeticType.CAPE)) Misc.addEnchantGlint(capesItem);
        if(equippedCosmeticTypes.contains(CosmeticType.PARTICLE_TRAIL)) Misc.addEnchantGlint(particleTrailItem);
        if(equippedCosmeticTypes.contains(CosmeticType.AURA)) Misc.addEnchantGlint(auraItem);
        if(equippedCosmeticTypes.contains(CosmeticType.MISC)) Misc.addEnchantGlint(miscItem);

        getInventory().setItem(10, playerKillEffects);
        getInventory().setItem(11, botKillEffects);
        getInventory().setItem(12, bountyMessageItem);
        getInventory().setItem(13, capesItem);
        getInventory().setItem(14, particleTrailItem);
        getInventory().setItem(15, auraItem);
        getInventory().setItem(16, miscItem);
        getInventory().setItem(22, backItem);
    }

    @Override
    public String getName() {
        return ChatColor.YELLOW + "" + ChatColor.BOLD + "装饰";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory().getHolder() != this) return;
        int slot = event.getSlot();

        for(Map.Entry<CosmeticType, SubCosmeticPanel> entry : settingsGUI.cosmeticPanelMap.entrySet()) {
            if(slot != entry.getKey().getSettingsGUISlot()) continue;
            openPanel(entry.getValue());
        }

        if(slot == 22) {
            openPreviousGUI();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
