package me.wisdom.thepit.items.mobdrops;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.brewing.objects.BrewingIngredient;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.MarketCategory;
import me.wisdom.thepit.items.TemporaryItem;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.*;

public class RawPork extends BrewingIngredient implements TemporaryItem {

    public Map<Player, Integer> tickMap = new HashMap<>();

    public RawPork() {
        super(6, "Regeneration", ChatColor.RED, PotionType.INSTANT_HEAL);
        hasDropConfirm = true;
        marketCategory = MarketCategory.DARKZONE_DROPS;
    }

    @Override
    public String getNBTID() {
        return "raw-pork";
    }

    @Override
    public List<String> getRefNames() {
        return new ArrayList<>(Arrays.asList("pork", "rawpork"));
    }

    @Override
    public Material getMaterial() {
        return Material.PORK;
    }

    @Override
    public String getName() {
        return "&aRaw Pork";
    }

    @Override
    public List<String> getLore() {
        return new ALoreBuilder(
                "&7Pork gathered from the Pigmen",
                "&7of the Pigmen Caves",
                "",
                "&cLost on death"
        ).getLore();
    }

    @Override
    public boolean isLegacyItem(ItemStack itemStack, NBTItem nbtItem) {
        return false;
    }

    @Override
    public TemporaryItem.TemporaryType getTemporaryType() {
        return TemporaryType.LOST_ON_DEATH;
    }

    @Override
    public void administerEffect(Player player, BrewingIngredient potency, int duration) {
        if(duration == 0) tickMap.remove(player);
        else tickMap.putIfAbsent(player, 1);

        for (Map.Entry<Player, Integer> entry : tickMap.entrySet()) {
            if(entry.getValue() - 1 == 0) {
                PitPlayer.getPitPlayer(player).heal((Double) getPotency(potency) * 2);
                tickMap.put(entry.getKey(), 5 * 20);
            } else tickMap.put(entry.getKey(), entry.getValue() - 1);
        }
    }

    @Override
    public Object getPotency(BrewingIngredient potencyIngredient) {
        return 0.4 * potencyIngredient.tier;
    }

    @Override
    public List<String> getPotencyLore(BrewingIngredient potency) {
        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(ChatColor.GRAY + "Gain " + color + "+" + getPotency(potency) + "\u2764 " + ChatColor.GRAY + "Every 5 seconds.");
        return lore;
    }

    @Override
    public int getDuration(BrewingIngredient durationIngredient) {
        return 20 * 30 * durationIngredient.tier;
    }
}
