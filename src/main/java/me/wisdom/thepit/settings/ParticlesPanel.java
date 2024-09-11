package me.wisdom.thepit.settings;

import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
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
import org.bukkit.inventory.ItemStack;

public class ParticlesPanel extends AGUIPanel {
    public SettingsGUI settingsGUI;

    public static ItemStack backItem;
    public ItemStack auraItem;
    public ItemStack trailsItem;

    static {
        backItem = new AItemStackBuilder(Material.BARRIER)
                .setName("&c返回")
                .setLore(new ALoreBuilder(
                        "&7点击返回上一步"
                ))
                .getItemStack();
    }

    public ParticlesPanel(AGUI gui) {
        super(gui);
        settingsGUI = (SettingsGUI) gui;

        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 7);
        getInventory().setItem(22, backItem);

        setItems();
    }

    public void setItems() {
        auraItem = new AItemStackBuilder(Material.BEACON)
                .setName("&a光环")
                .setLore(new ALoreBuilder(
                        "&7点击切换光环粒子",
                        "",
                        "&7状态: " + Misc.getStateMessage(settingsGUI.pitPlayer.playerSettings.auraParticles)
                ))
                .getItemStack();
        getInventory().setItem(10, auraItem);

        trailsItem = new AItemStackBuilder(Material.FIREWORK)
                .setName("&e粒子轨迹")
                .setLore(new ALoreBuilder(
                        "&7点击切换粒子轨迹",
                        "",
                        "&7状态: " + Misc.getStateMessage(settingsGUI.pitPlayer.playerSettings.trailParticles)
                ))
                .getItemStack();
        getInventory().setItem(11, trailsItem);
    }

    @Override
    public String getName() {
        return ChatColor.GREEN + "" + ChatColor.BOLD + "设置粒子";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        // 检查点击的背包是否是当前的
        if(event.getClickedInventory().getHolder() != this) return;
        int slot = event.getSlot();

        // 处理点击事件
        if(slot == 10) {
            // 切换光环粒子状态
            settingsGUI.pitPlayer.playerSettings.auraParticles = !settingsGUI.pitPlayer.playerSettings.auraParticles;
            informPlayer("&a&l光环", settingsGUI.pitPlayer.playerSettings.auraParticles);
        } else if(slot == 11) {
            // 切换粒子轨迹状态
            settingsGUI.pitPlayer.playerSettings.trailParticles = !settingsGUI.pitPlayer.playerSettings.trailParticles;
            informPlayer("&e&l粒子轨迹", settingsGUI.pitPlayer.playerSettings.trailParticles);
        } else if(slot == 22) {
            // 打开上一界面
            openPreviousGUI();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

    public void informPlayer(String name, boolean state) {
        setItems();
        Sounds.SUCCESS.play(player);
        AOutput.send(player, "&e&l设置！&7 设置 " + name + "&7 已被 " + Misc.getStateMessage(state));
    }
}
