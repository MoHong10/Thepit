package me.wisdom.thepit.inventories;

import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.controllers.objects.RenownUpgrade;
import me.wisdom.thepit.items.misc.GoldenHelmet;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HelmetryPanel extends AGUIPanel {

    PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
    RenownUpgrade upgrade = null;
    public RenownShopGUI renownShopGUI;

    public HelmetryPanel(AGUI gui) {
        super(gui);
        renownShopGUI = (RenownShopGUI) gui;
    }

    @Override
    public String getName() {
        return "Helmetry";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory().getHolder() != this) return;
        int slot = event.getSlot();

        if(slot == 15) {
            if(upgrade.prestigeReq > pitPlayer.prestige) {
                AOutput.error(player, "&cYou are too low prestige to acquire this!");
                Sounds.NO.play(player);
                return;
            }
            if(upgrade.isTiered()) {
                if(upgrade.getMaxTiers() != UpgradeManager.getTier(player, upgrade) &&
                        upgrade.getTierCosts().get(UpgradeManager.getTier(player, upgrade)) > pitPlayer.renown) {
                    AOutput.error(player, "&cYou do not have enough renown!");
                    Sounds.NO.play(player);
                    return;
                }
                if(UpgradeManager.getTier(player, upgrade) < upgrade.getMaxTiers()) {
                    RenownShopGUI.purchaseConfirmations.put(player, upgrade);
                    openPanel(renownShopGUI.renownShopConfirmPanel);
                } else {
                    AOutput.error(player, "&aYou already unlocked the last upgrade!");
                    Sounds.NO.play(player);
                }
            } else if(!UpgradeManager.hasUpgrade(player, upgrade)) {
                if(upgrade.getUnlockCost() > pitPlayer.renown) {
                    AOutput.error(player, "&cYou do not have enough renown!");
                    Sounds.NO.play(player);
                    return;
                }
                RenownShopGUI.purchaseConfirmations.put(player, upgrade);
                openPanel(renownShopGUI.renownShopConfirmPanel);
            } else {
                AOutput.error(player, "&aYou already unlocked this upgrade!");
                Sounds.NO.play(player);
            }

        }
        if(slot == 11) {
            if(pitPlayer.renown < 10) {
                AOutput.error(player, "&cYou do not have enough renown to do this!");
                Sounds.NO.play(player);
                return;
            }

            ItemStack helmetStack = ItemFactory.getItem(GoldenHelmet.class).getItem();
            AUtil.giveItemSafely(player, helmetStack, true);

            pitPlayer.renown -= 10;
            player.closeInventory();
            Sounds.HELMET_CRAFT.play(player);
            AOutput.send(player, "&6&lITEM CRAFTED!&7 Received &6Golden Helmet&7!");
        }
        if(slot == 22) {
            openPanel(renownShopGUI.getHomePanel());
        }
        updateInventory();
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

        for(RenownUpgrade renownUpgrade : UpgradeManager.upgrades) {
            if(renownUpgrade.refName.equals("HELMETRY")) upgrade = renownUpgrade;
        }

        ItemStack gem = new ItemStack(Material.GOLD_HELMET);
        ItemMeta meta = gem.getItemMeta();
        if(pitPlayer.renown >= 5) meta.setDisplayName(ChatColor.YELLOW + "Craft Golden Helmet");
        else meta.setDisplayName(ChatColor.RED + "Craft Golden Helmet");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Craft a &6Golden Helmet &7that unlocks"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7passive bonuses the more &6gold &7you"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7put into it. Use abilities that cost"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6gold &7from the helmet."));
        lore.add("");
        if(pitPlayer.renown >= 10) lore.add(ChatColor.YELLOW + "Craft for 10 Renown!");
        else lore.add(ChatColor.RED + "Not enough renown!");
        meta.setLore(lore);
        gem.setItemMeta(meta);

        getInventory().setItem(11, gem);
        getInventory().setItem(15, upgrade.getDisplayStack(player));

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.GREEN + "Go Back");
        List<String> backLore = new ArrayList<>();
        backLore.add(ChatColor.GRAY + "To Renown Shop");
        backMeta.setLore(backLore);
        back.setItemMeta(backMeta);

        getInventory().setItem(22, back);

    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

}
