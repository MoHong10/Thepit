package me.wisdom.thepit.storage;

import me.wisdom.thepit.enums.RankInformation;
import me.wisdom.thepit.inventories.view.ViewGUI;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class EnderchestPanel extends AGUIPanel {
    public AGUI enderchestGUI;
    public StorageProfile profile;

    public EnderchestPanel(AGUI gui, StorageProfile profile) {
        super(gui);
        this.enderchestGUI = gui;
        this.profile = profile;
        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 15);

        if(!isAdminSession() && !isViewSession()) addTaggedItem(35, () -> new AItemStackBuilder(Material.ARMOR_STAND)
                .setName("&2衣柜")
                .setLore(new ALoreBuilder(
                        "&7点击打开你的衣柜"
                ))
                .getItemStack(), event -> openPanel(((EnderchestGUI) enderchestGUI).wardrobePanel)).setItem();
    }

    @Override
    public String getName() {
        return "Enderchest";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory().getHolder() != this) return;
        int slot = event.getSlot();

        RankInformation rank = RankInformation.getRank(profile.getUniqueID());
        int accessiblePages = isAdminSession() ? StorageManager.MAX_ENDERCHEST_PAGES : rank.enderchestPages;

        if(slot == 8 && isAdminSession()) {
            EditSession session = StorageManager.getSession(player);
            session.playerClosed = false;
            player.openInventory(session.inventory.getInventory());
            session.playerClosed = true;
        } else if(slot == 8 && isViewSession()) {
            ViewGUI viewGUI = ViewGUI.viewGUIs.get(player.getUniqueId());
            viewGUI.playerClosed = false;
            openPanel(viewGUI.mainViewPanel);
        }

        if(slot < 9 || slot >= StorageManager.MAX_ENDERCHEST_PAGES + 9) return;

        if((slot - 9) + 1 > accessiblePages) {
            event.setCancelled(true);
            if(!isViewSession() && !isAdminSession()) AOutput.error(player, "&5&l等级要求！&7 查看等级请访问 &6&nhttps://store.pitsim.net");
            Sounds.ERROR.play(player);
            return;
        }

        if(!profile.isLoaded() || profile.isSaving()) return;

        EnderchestPage enderchestPage = profile.getEnderchestPage(slot - 9);

        if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {
            if(StorageManager.isEditing(player)) {
                EditSession session = StorageManager.getSession(player);
                session.playerClosed = false;
                player.openInventory(enderchestPage.getInventory());
                session.playerClosed = true;
                return;
            }

            if(getViewGUI() != null) {
                ViewGUI viewGUI = getViewGUI();
                viewGUI.playerClosed = false;
            }
            player.openInventory(enderchestPage.getInventory());

        } else if(event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.RIGHT) {
            if(isViewSession() || isAdminSession()) return;
            if(enderchestPage.isWardrobeEnabled()) {
                AOutput.send(player, "&2&l衣柜！&7 衣柜 &c已禁用 &7对于 " + enderchestPage.getDisplayName());
            } else {
                AOutput.send(player, "&2&l衣柜！&7 衣柜 &a已启用 &7对于 " + enderchestPage.getDisplayName());
            }
            Sounds.SUCCESS.play(player);
            enderchestPage.setWardrobeEnabled(!enderchestPage.isWardrobeEnabled());
            setInventory();
        }
    }

    @Override
    public void setInventory() {
        super.setInventory();
        RankInformation rank = RankInformation.getRank(profile.getUniqueID());
        int accessiblePages = isAdminSession() ? StorageManager.MAX_ENDERCHEST_PAGES : rank.enderchestPages;

        for(int i = 9; i < 27; i++) {
            int pageIndex = (i - 9);

            EnderchestPage enderchestPage = profile.getEnderchestPage(pageIndex);
            AItemStackBuilder stackBuilder = new AItemStackBuilder(enderchestPage.getDisplayItem())
                    .setName(enderchestPage.getDisplayName());
            ALoreBuilder lore = new ALoreBuilder();

            if(pageIndex + 1 <= accessiblePages) {
                lore.addLore(
                        "&7状态: &a已解锁",
                        "&7衣柜: " + (enderchestPage.isWardrobeEnabled() ? "&a启用" : "&c禁用"),
                        "&7物品: &d" + enderchestPage.getItemCount() + "&7/&d" + StorageManager.ENDERCHEST_ITEM_SLOTS,
                        "",
                        "&e左键点击打开！"
                );
                if(!isAdminSession() && !isViewSession()) lore.addLore("&e右键点击切换衣柜！");
                Misc.addEnchantGlint(stackBuilder.getItemStack());
            } else {
                stackBuilder.getItemStack().setType(Material.BARRIER);
                lore.addLore(
                        "&7状态: &c已锁定",
                        "&7所需等级: " + RankInformation.getMinimumRankForPages(pageIndex + 1).rankName,
                        "&7商店: &d&nstore.pitsim.net"
                );
            }

            stackBuilder.setLore(lore);
            getInventory().setItem(i, stackBuilder.getItemStack());

            if(!isAdminSession() && !isViewSession()) continue;

            AItemStackBuilder builder = new AItemStackBuilder(Material.CHEST);
            builder.setName("&6查看背包");
            getInventory().setItem(8, builder.getItemStack());
        }
        updateInventory();
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        setInventory();

        if(getViewGUI() != null) {
            ViewGUI viewGUI = getViewGUI();
            viewGUI.playerClosed = true;
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

        if(StorageManager.isEditing((Player) event.getPlayer())) {
            EditSession session = StorageManager.getSession(player);

            if(!session.playerClosed) return;
            session.end();
        }
    }

    public boolean isAdminSession() {
        boolean edit = false;
        for(EditSession editSession : StorageManager.editSessions) {
            if(editSession.getStaffMember().getUniqueId().equals(player.getUniqueId())) {
                edit = true;
                break;
            }
        }

        return !profile.getUniqueID().equals(player.getUniqueId()) && edit;
    }

    public ViewGUI getViewGUI() {
        return ViewGUI.viewGUIs.get(player.getUniqueId());
    }

    public boolean isViewSession() {
        return StorageManager.viewProfiles.contains(profile);
    }
}
