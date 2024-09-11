package me.wisdom.thepit.items.misc;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.enums.MarketCategory;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.inventories.GemGUI;
import me.wisdom.thepit.items.StaticPitItem;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TotallyLegitGem extends StaticPitItem {

    public TotallyLegitGem() {
        hasDropConfirm = true;
        marketCategory = MarketCategory.PURE_RELATED;
    }

    @Override
    public String getNBTID() {
        return "gem";
    }

    @Override
    public List<String> getRefNames() {
        return new ArrayList<>(Arrays.asList("gem", "totallylegitgem"));
    }

    @Override
    public Material getMaterial() {
        return Material.EMERALD;
    }

    @Override
    public String getName() {
        return "&aTotally Legit Gem";
    }

    @Override
    public List<String> getLore() {
        return new ALoreBuilder(
                "&7Kept on death",
                "&7Adds &d1 tier &7to a mystic enchant.",
                "&8Once per item!",
                "",
                "&eHold and right-click to use!"
        ).getLore();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return;
        if(!isThisItem(player.getItemInHand())) return;

        GemGUI gemGUI = new GemGUI(event.getPlayer());
        gemGUI.open();
    }

    @Override
    public boolean isLegacyItem(ItemStack itemStack, NBTItem nbtItem) {
        return nbtItem.hasKey(NBTTag.IS_GEM.getRef());
    }
}
