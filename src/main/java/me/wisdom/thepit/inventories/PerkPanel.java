package me.wisdom.thepit.inventories;

import me.wisdom.thepit.controllers.PrestigeValues;
import me.wisdom.thepit.controllers.objects.Killstreak;
import me.wisdom.thepit.controllers.objects.PitPerk;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.DisplayItemType;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.tutorial.HelpItemStacks;
import me.wisdom.thepit.upgrades.TheWay;
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

import java.util.Arrays;
import java.util.List;

public class PerkPanel extends AGUIPanel {
    public List<Integer> killstreakLevels = Arrays.asList(0, 75, 90);
    public PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
    public PerkGUI perkGUI;

    public PerkPanel(AGUI gui) {
        super(gui);
        perkGUI = (PerkGUI) gui;

        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 7);
    }

    @Override
    public String getName() {
        return "&aPerks &7and &eKillstreaks";
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public void onClick(InventoryClickEvent event) {

        int slot = event.getSlot();
        if(event.getClickedInventory().getHolder() == this) {

            if(slot == 10 || slot == 12 || slot == 14 || slot == 16) {

                perkGUI.selectPerkPanel.perkNum = perkGUI.getPerkNum(slot);
                openPanel(perkGUI.selectPerkPanel);
                return;
            }

            if(slot == 34) {
                openPanel(perkGUI.megastreakPanel);
            }

            if(slot == 32) {
                if(pitPlayer.level < killstreakLevels.get(2) - TheWay.INSTANCE.getLevelReduction(player)) {
                    Sounds.NO.play(player);
                    AOutput.error(player, "&cYou are too low level to use this slot!");
                    return;
                }
                perkGUI.killstreakSlot = 3;
                openPanel(perkGUI.killstreakPanel);
            }

            if(slot == 30) {
                if(pitPlayer.level < killstreakLevels.get(1) - TheWay.INSTANCE.getLevelReduction(player)) {
                    Sounds.NO.play(player);
                    AOutput.error(player, "&cYou are too low level to use this slot!");
                    return;
                }
                perkGUI.killstreakSlot = 2;
                openPanel(perkGUI.killstreakPanel);
            }

            if(slot == 28) {
                if(pitPlayer.level < killstreakLevels.get(0) - TheWay.INSTANCE.getLevelReduction(player)) {
                    Sounds.NO.play(player);
                    AOutput.error(player, "&cYou are too low level to use this slot!");
                    return;
                }
                perkGUI.killstreakSlot = 1;
                openPanel(perkGUI.killstreakPanel);
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        for(int i = 0; i < pitPlayer.pitPerks.size(); i++) {
            PitPerk pitPerk = pitPlayer.pitPerks.get(i);
            if(pitPerk == null) continue;
            ItemStack perkItem = pitPerk.getDisplayStack(player, DisplayItemType.MAIN_PERK_PANEL, i);
            getInventory().setItem(10 + i * 2, perkItem);
        }

        for(int i = 0; i < pitPlayer.killstreaks.size(); i++) {
            int unlockLevel = Math.max(killstreakLevels.get(i) - TheWay.INSTANCE.getLevelReduction(player), 0);
            if(pitPlayer.level < unlockLevel) {
                AItemStackBuilder lockedBuilder = new AItemStackBuilder(Material.BEDROCK);
                lockedBuilder.setLore(new ALoreBuilder("&7Required level: [" + PrestigeValues.
                        getLevelColor(unlockLevel) + unlockLevel + "&7]"));
                lockedBuilder.setName("&cKillstreak Slot #" + (i + 1));
                getInventory().setItem(28 + (2 * i), lockedBuilder.getItemStack());
                continue;
            }
            Killstreak killstreak = pitPlayer.killstreaks.get(i);
            AItemStackBuilder builder = new AItemStackBuilder(killstreak.getDisplayStack(player));
            ALoreBuilder loreBuilder = new ALoreBuilder();
            if(!killstreak.refName.equals("NoKillstreak")) {
                builder.setName("&eKillstreak Slot #" + (i + 1));
                loreBuilder.addLore("&7Selected: &e" + killstreak.displayName, "");
                loreBuilder.addLore(builder.getItemStack().getItemMeta().getLore());
                loreBuilder.addLore("", "&eClick to switch killstreak!");

            } else {
                builder.setName("&eKillstreak Slot #" + (i + 1));
                loreBuilder.addLore("&7Select a killstreak for this", "&7slot.", "", "&eClick to select killstreak!");
            }
            builder.setLore(loreBuilder);
            getInventory().setItem(28 + (2 * i), builder.getItemStack());

        }

        getInventory().setItem(44, HelpItemStacks.getPerksItemStack());
        getInventory().setItem(34, pitPlayer.getMegastreak().getDisplayStack(player, DisplayItemType.MAIN_PERK_PANEL));
        updateInventory();
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }
}
