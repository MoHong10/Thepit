package me.wisdom.thepit.darkzone.altar.pedestals;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.darkzone.altar.AltarManager;
import me.wisdom.thepit.darkzone.altar.AltarPedestal;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.tutorial.TutorialObjective;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AltarPanel extends AGUIPanel {

    public PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
    public int totalCost = AltarPedestal.getTotalCost(player);

    public AltarPanel(AGUI gui) {
        super(gui);

        if(isTutorial()) {
            for(AltarPedestal altarPedestal : AltarPedestal.altarPedestals) {
                if(altarPedestal.isActivated(player)) altarPedestal.deactivate(player, true);
            }
        }

        setItems();
    }

    public void setItems() {

        int slot = 0;
        for(AltarPedestal pedestal : AltarPedestal.altarPedestals) {
            ItemStack item = pedestal.getItem(player);
            ALoreBuilder loreBuilder = new ALoreBuilder(item.getItemMeta().getLore());
            String status = "&eClick to activate!";
            if(!pedestal.isUnlocked(player)) status = "&cPedestal is locked!";
            else if(pedestal.isActivated(player)) status = "&eClick to deactivate!";
            loreBuilder.addLore("", status);

            ItemMeta meta = item.getItemMeta();
            meta.setLore(loreBuilder.getLore());
            item.setItemMeta(meta);

            getInventory().setItem(slot, item);

            slot += 2;
        }

        setCost();
        String costStatus = totalCost > pitPlayer.taintedSouls ? "&cYou do not have enough souls!" : "&aClick to confirm your selections!";
        boolean free = isTutorial();

        ALoreBuilder loreBuilder = new ALoreBuilder("");
        for(AltarPedestal altarPedestal : AltarPedestal.altarPedestals) {
            loreBuilder.addLore(altarPedestal.getDisplayName() + "&7: " + (altarPedestal.isActivated(player) ?
                    "&a&lACTIVE" : "&c&lINACTIVE"));
        }
        loreBuilder.addLore(
                "",
                "&7Total Cost: &f" + (free ? "&m" : "") +  totalCost + " Souls" + (free ? "&a&l FREE!" : ""),
                "&7Your Souls: &f" + pitPlayer.taintedSouls + " Souls",
                "",
                costStatus
        );


        AItemStackBuilder confirm = new AItemStackBuilder(Material.STAINED_CLAY, 1, pitPlayer.taintedSouls < totalCost ? 14 : 5)
                .setName((pitPlayer.taintedSouls < totalCost ? "&c" : "&a") + "Confirm Selections")
                .setLore(loreBuilder);

        getInventory().setItem(22, confirm.getItemStack());
    }

    @Override
    public String getName() {
        return "Tainted Altar";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory().getHolder() != this) return;
        int slot = event.getSlot();

        setCost();

        for(int i = 0; i < AltarPedestal.altarPedestals.size(); i++) {
            if(slot != i * 2) continue;
            AltarPedestal pedestal = AltarPedestal.altarPedestals.get(i);

            if(!pedestal.isUnlocked(player) || isTutorial()) {
                Sounds.ERROR.play(player);
                return;
            }

            if(pedestal.isActivated(player)) {
                pedestal.deactivate(player, false);
            } else {
                pedestal.activate(player);
            }

            setCost();
            setItems();
        }

        if(slot == 22) {
            if(pitPlayer.taintedSouls < totalCost) {
                Sounds.ERROR.play(player);
                return;
            }

            AltarManager.activateAltar(player, totalCost);
            player.closeInventory();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

    public void setCost() {
        totalCost = isTutorial() ? 0 : AltarPedestal.getTotalCost(player);
    }

    public boolean isTutorial() {
        return pitPlayer.darkzoneTutorial.isActive() &&
                !pitPlayer.darkzoneTutorial.data.completedObjectives.contains(TutorialObjective.ALTAR);

    }
}
