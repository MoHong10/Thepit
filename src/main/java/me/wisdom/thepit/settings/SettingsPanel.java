package me.wisdom.thepit.settings;

import me.clip.placeholderapi.PlaceholderAPI;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.enums.MysticType;
import me.wisdom.thepit.enums.PantColor;
import me.wisdom.thepit.inventories.ChatColorPanel;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.misc.ItemRename;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class SettingsPanel extends AGUIPanel {
    public SettingsGUI settingsGUI;

    public static ItemStack cosmeticItem;
    public static ItemStack particleItem;
    public static ItemStack scoreboardItem;

    static {
        cosmeticItem = new AItemStackBuilder(Material.RED_ROSE)
                .setName("&c装饰")
                .setLore(new ALoreBuilder(
                        "&7点击打开装饰菜单"
                ))
                .getItemStack();

        particleItem = new AItemStackBuilder(Material.SEEDS)
                .setName("&a粒子设置")
                .setLore(new ALoreBuilder(
                        "&7点击打开粒子菜单"
                ))
                .getItemStack();

        scoreboardItem = new AItemStackBuilder(Material.SIGN)
                .setName("&2记分板设置")
                .setLore(new ALoreBuilder(
                        "&7点击自定义你的记分板"
                ))
                .getItemStack();
    }

    public SettingsPanel(AGUI gui) {
        super(gui);
        settingsGUI = (SettingsGUI) gui;

        // 创建边框
        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 7);

        // 设置物品
        getInventory().setItem(13, cosmeticItem);
        getInventory().setItem(14, particleItem);
        getInventory().setItem(19, scoreboardItem);

        // 创建裤子物品
        ItemStack pants = MysticFactory.getFreshItem(MysticType.PANTS, PantColor.PURE_RED);
        ItemMeta pantsmeta = pants.getItemMeta();
        List<String> pantslore = new ArrayList<>();
        pantslore.add(ChatColor.GRAY + "从高级颜色中选择");
        pantslore.add(ChatColor.GRAY + "为你的裤子染色");
        pantslore.add("");
        if(player.hasPermission("pitsim.pantscolor")) {
            pantslore.add(ChatColor.YELLOW + "点击选择！");
            pantsmeta.setDisplayName(ChatColor.YELLOW + "裤子上色器");
        } else {
            pantslore.add(ChatColor.translateAlternateColorCodes('&', "&c需要 &5超凡 &c等级！"));
            pantsmeta.setDisplayName(ChatColor.RED + "裤子上色器");
        }
        pantsmeta.setLore(pantslore);
        pants.setItemMeta(pantsmeta);
        pants = PantColor.setPantColor(pants, PantColor.HARVEST_RED);

        // 创建重命名物品
        ItemStack rename = new ItemStack(Material.NAME_TAG);
        ItemMeta renamemeta = rename.getItemMeta();
        List<String> renamelore = new ArrayList<>();
        renamelore.add(ChatColor.GRAY + "将你手中的任何神秘物品");
        renamelore.add(ChatColor.GRAY + "重命名为你想要的名称");
        renamelore.add("");
        ItemStack heldItem = player.getItemInHand();
        if(player.hasPermission("pitsim.itemrename")) {
            PitItem pitItem = ItemFactory.getItem(heldItem);
            if(pitItem != null && pitItem.isMystic) {
                renamelore.add(ChatColor.GRAY + "持有: " + heldItem.getItemMeta().getDisplayName());
                renamelore.add("");
                renamelore.add(ChatColor.YELLOW + "点击选择！");
                renamemeta.setDisplayName(ChatColor.YELLOW + "重命名物品");
            } else {
                renamelore.add(ChatColor.GRAY + "持有: " + ChatColor.RED + "无效物品！");
                renamelore.add("");
                renamelore.add(ChatColor.RED + "必须持有一个神秘物品！");
                renamemeta.setDisplayName(ChatColor.RED + "重命名物品");
            }
        } else {
            renamelore.add(ChatColor.translateAlternateColorCodes('&', "&c需要 &b奇迹 &c等级！"));
            renamemeta.setDisplayName(ChatColor.RED + "重命名物品");
        }
        renamemeta.setLore(renamelore);
        rename.setItemMeta(renamemeta);

        // 创建聊天颜色物品
        ItemStack chat = new ItemStack(Material.SIGN);
        ItemMeta chatmeta = chat.getItemMeta();
        List<String> chatlore = new ArrayList<>();
        chatlore.add(ChatColor.GRAY + "选择任何颜色来发送你的聊天");
        chatlore.add(ChatColor.GRAY + "消息");
        chatlore.add("");
        if(player.hasPermission("pitsim.chatcolor")) {
            if(ChatColorPanel.playerChatColors.containsKey(player)) {
                chatlore.add(ChatColor.GRAY + "选择: " + ChatColorPanel.playerChatColors.get(player).chatColor + ChatColorPanel.playerChatColors.get(player).refName);
                chatlore.add("");
            }
            chatlore.add(ChatColor.YELLOW + "点击选择！");
            chatmeta.setDisplayName(ChatColor.YELLOW + "聊天颜色");
        } else {
            chatlore.add(ChatColor.translateAlternateColorCodes('&', "&c需要 &6不可思议 &c等级！"));
            chatmeta.setDisplayName(ChatColor.RED + "聊天颜色");
        }
        chatmeta.setLore(chatlore);
        chat.setItemMeta(chatmeta);

        // 创建过滤器物品
        ItemStack filter = new ItemStack(Material.REDSTONE_COMPARATOR);
        ItemMeta filtermeta = filter.getItemMeta();
        List<String> filterlore = new ArrayList<>();
        filterlore.add(ChatColor.GRAY + "过滤某些游戏内消息");
        filterlore.add(ChatColor.GRAY + "以改善你的游戏体验");
        filterlore.add("");
        if(player.hasPermission("pitsim.chat")) {
            filterlore.add(ChatColor.YELLOW + "点击选择！");
            filtermeta.setDisplayName(ChatColor.YELLOW + "聊天选项");
        } else {
            filterlore.add(ChatColor.translateAlternateColorCodes('&', "&c需要 &e传奇 &c等级！"));
            filtermeta.setDisplayName(ChatColor.RED + "聊天选项");
        }
        filtermeta.setLore(filterlore);
        filter.setItemMeta(filtermeta);

        // 创建头颅物品
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta headmeta = (SkullMeta) head.getItemMeta();
        headmeta.setOwner(player.getDisplayName());
        headmeta.setDisplayName(ChatColor.YELLOW + "等级信息");
        List<String> headlore = new ArrayList<>();
        String rank = "&7当前等级: %luckperms_groups%";
        headlore.add(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, rank)));
        headlore.add("");
        if(player.hasPermission("pitsim.show")) headlore.add(ChatColor.GREEN + "访问 /show");
        else headlore.add(ChatColor.RED + "访问 /show");
        if(player.hasPermission("pitsim.chat")) headlore.add(ChatColor.GREEN + "访问 /chat");
        else headlore.add(ChatColor.RED + "访问 /chat");
        if(player.hasPermission("pitsim.pantscolor")) headlore.add(ChatColor.GREEN + "裤子上色器");
        else headlore.add(ChatColor.RED + "裤子上色器");
        if(player.hasPermission("pitsim.itemrename")) headlore.add(ChatColor.GREEN + "物品重命名");
        else headlore.add(ChatColor.RED + "物品重命名");
        if(player.hasPermission("pitsim.stereo")) headlore.add(ChatColor.GREEN + "立体裤子");
        else headlore.add(ChatColor.RED + "立体裤子");
        if(player.hasPermission("pitsim.chatcolor")) headlore.add(ChatColor.GREEN + "聊天颜色");
        else headlore.add(ChatColor.RED + "聊天颜色");
        if(player.hasPermission("pitsim.scoreboard")) headlore.add(ChatColor.GREEN + "记分板设置");
        else headlore.add(ChatColor.RED + "记分板设置");
        if(player.hasPermission("galacticvaults.limit.14"))
            headlore.add(ChatColor.GRAY + "14x " + ChatColor.DARK_PURPLE + "末影箱页数");
        else if(player.hasPermission("galacticvaults.limit.10"))
            headlore.add(ChatColor.GRAY + "10x " + ChatColor.DARK_PURPLE + "末影箱页数");
        else if(player.hasPermission("galacticvaults.limit.7"))
            headlore.add(ChatColor.GRAY + "7x " + ChatColor.DARK_PURPLE + "末影箱页数");
        else if(player.hasPermission("galacticvaults.limit.5"))
            headlore.add(ChatColor.GRAY + "5x " + ChatColor.DARK_PURPLE + "末影箱页数");
        else if(player.hasPermission("galacticvaults.limit.3"))
            headlore.add(ChatColor.GRAY + "3x " + ChatColor.DARK_PURPLE + "末影箱页数");
        else if(player.hasPermission("galacticvaults.limit.1"))
            headlore.add(ChatColor.GRAY + "1x " + ChatColor.DARK_PURPLE + "末影箱页数");
        headmeta.setLore(headlore);
        head.setItemMeta(headmeta);

        // 将物品设置到背包中
        getInventory().setItem(10, head);
        getInventory().setItem(11, filter);
        getInventory().setItem(12, pants);
        getInventory().setItem(15, rename);
        getInventory().setItem(16, chat);
    }

    @Override
    public String getName() {
        return ChatColor.WHITE + "" + ChatColor.BOLD + "设置";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory().getHolder() != this) return;
        int slot = event.getSlot();

        if(slot == 11) {
            if(!player.hasPermission("pitsim.chat")) return;
            openPanel(settingsGUI.chatOptionsPanel);
        } else if(slot == 12) {
            if(!player.hasPermission("pitsim.pantscolor")) return;
            openPanel(settingsGUI.pantsColorPanel);
        } else if(slot == 15) {
            if(!player.hasPermission("pitsim.itemrename")) return;
            ItemStack heldItem = player.getItemInHand();
            PitItem pitItem = ItemFactory.getItem(heldItem);
            if(pitItem == null || !pitItem.isMystic) return;
            player.closeInventory();
            ItemRename.renameItem(player, player.getItemInHand());
        } else if(slot == 16) {
            if(!player.hasPermission("pitsim.chatcolor")) return;
            openPanel(settingsGUI.chatColorPanel);
        } else if(slot == 13) {
            openPanel(settingsGUI.cosmeticPanel);
        } else if(slot == 14) {
            openPanel(settingsGUI.particlesPanel);
        } else if(slot == 19) {
            if(!player.hasPermission("pitsim.scoreboard")) return;
            openPanel(settingsGUI.scoreboardOptionsPanel);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
