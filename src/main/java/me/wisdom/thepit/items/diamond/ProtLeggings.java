package me.wisdom.thepit.items.diamond;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.enums.MarketCategory;
import me.wisdom.thepit.items.StaticPitItem;
import me.wisdom.thepit.items.TemporaryItem;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProtLeggings extends StaticPitItem implements TemporaryItem {

    public ProtLeggings() {
        hasDropConfirm = true;
        hideExtra = true;
        isProtDiamond = true;
        marketCategory = MarketCategory.MISC;

        itemEnchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
    }

    @Override
    public String getNBTID() {
        return "protection-leggings";
    }

    @Override
    public List<String> getRefNames() {
        return new ArrayList<>(Arrays.asList("p1leggings", "leggings"));
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_LEGGINGS;
    }

    @Override
    public String getName() {
        return "&bProtection I Leggings";
    }

    @Override
    public List<String> getLore() {
        return new ALoreBuilder(
                "&7A relic back from when knights",
                "&7had shining armor",
                "",
                "&cLost on death"
        ).getLore();
    }

    @Override
    public boolean isLegacyItem(ItemStack itemStack, NBTItem nbtItem) {
        return itemStack.getType() == Material.DIAMOND_LEGGINGS && itemStack.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) != 0;
    }

    @Override
    public TemporaryItem.TemporaryType getTemporaryType() {
        return TemporaryType.LOST_ON_DEATH;
    }
}
