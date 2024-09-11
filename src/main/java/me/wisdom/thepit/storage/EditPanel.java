package me.wisdom.thepit.storage;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.ProxyMessaging;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class EditPanel extends AGUIPanel {

    public EditSession session;
    public boolean playerClosed = true;

    public boolean hasClicked = false;

    public EditPanel(AGUI gui, EditSession session) {
        super(gui);
        this.session = session;
    }

    @Override
    public String getName() {
        return "选择编辑模式";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory().getHolder() != this || hasClicked) return;

        int slot = event.getSlot();
        if(slot == 11) {
            hasClicked = true;
            if(session.isPlayerOnline() && session.getPlayerServer().equals(Thepit.serverName)) {
                if(Bukkit.getPlayer(session.getPlayerUUID()) == null) {
                    AOutput.error(session.getStaffMember(), "&c玩家不在线!");
                    session.respond(EditType.CANCELED);
                } else session.respond(EditType.ONLINE);
            } else if(session.isPlayerOnline()) {
                String[] server = session.getPlayerServer().split("-");
                if(!session.getPlayerServer().contains("pitsim") && !session.getPlayerServer().contains("darkzone"))
                    return;
                if(server.length != 2) return;
                int serverNum = Integer.parseInt(server[1]);

                if(server[0].equalsIgnoreCase("pitsim")) {
                    ProxyMessaging.switchPlayer(player, serverNum);
                    session.respond(EditType.CANCELED);
                }

                if(server[0].equalsIgnoreCase("darkzone")) {
                    ProxyMessaging.darkzoneSwitchPlayer(player, serverNum);
                    session.respond(EditType.CANCELED);
                }
            } else {
                session.respond(EditType.CANCELED);
                player.closeInventory();
            }
        } else if(slot == 15) {
            hasClicked = true;
            if(!session.isPlayerOnline()) {
                session.respond(EditType.OFFLINE);
            } else session.respond(EditType.OFFLINE_KICK);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        AItemStackBuilder onlineBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 5);
        if(session.isPlayerOnline() && Thepit.serverName.equals(session.getPlayerServer())) {
            onlineBuilder.setName("&a&l在线编辑");
            ALoreBuilder onlineLore = new ALoreBuilder();
            onlineLore.addLore("&7该玩家当前在线上", "&7你可以编辑他们的物品栏",
                    "", "&a点击编辑物品栏");
            onlineBuilder.setLore(onlineLore);
        } else if(session.isPlayerOnline()) {
            ALoreBuilder onlineLore = new ALoreBuilder();
            onlineBuilder.getItemStack().setDurability((short) 4);
            onlineBuilder.setName("&e&l在线编辑");
            onlineLore.addLore("&7该玩家当前在其他地方", "&7你不能从这里编辑他们的物品栏",
                    "", "&e点击转移到玩家");
            onlineBuilder.setLore(onlineLore);
        } else {
            ALoreBuilder onlineLore = new ALoreBuilder();
            onlineBuilder.getItemStack().setDurability((short) 14);
            onlineBuilder.setName("&c&l在线编辑");
            onlineLore.addLore("&7该玩家当前离线", "&7你不能从这里编辑他们的物品栏",
                    "", "&c点击关闭此菜单");
            onlineBuilder.setLore(onlineLore);
        }
        getInventory().setItem(11, onlineBuilder.getItemStack());

        AItemStackBuilder offlineBuilder = new AItemStackBuilder(Material.STAINED_CLAY, 1, 5);
        if(!session.isPlayerOnline()) {
            offlineBuilder.setName("&a&l离线编辑");
            ALoreBuilder offlineLore = new ALoreBuilder();
            offlineLore.addLore("&7该玩家未在 pitsim 中", "&7你可以编辑他们的物品栏",
                    "", "&a点击编辑物品栏");
            offlineBuilder.setLore(offlineLore);
        } else {
            ALoreBuilder offlineLore = new ALoreBuilder();
            offlineBuilder.getItemStack().setDurability((short) 4);
            offlineBuilder.setName("&e&l离线编辑");
            offlineLore.addLore("&7该玩家当前在线", "&7他们需要离线才能进行编辑",
                    "", "&e点击踢出并编辑物品栏");
            offlineBuilder.setLore(offlineLore);
        }
        getInventory().setItem(15, offlineBuilder.getItemStack());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if(!session.hasResponded) session.respond(EditType.CANCELED);
    }
}
