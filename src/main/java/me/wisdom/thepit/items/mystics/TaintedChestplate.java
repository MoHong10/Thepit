package me.wisdom.thepit.items.mystics;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.MarketCategory;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.enums.PantColor;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.items.StaticPitItem;
import me.wisdom.thepit.items.TemporaryItem;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaintedChestplate extends StaticPitItem implements TemporaryItem {

    public TaintedChestplate() {
        hasUUID = true;
        hasLastServer = true;
        hasDropConfirm = true;
        hideExtra = true;
        unbreakable = true;
        hasEnchantGlint = true;
        isMystic = true;
        marketCategory = MarketCategory.DARKZONE_GEAR;
    }

    @Override
    public String getNBTID() {
        return "tainted-chestplate";
    }

    @Override
    public List<String> getRefNames() {
        return new ArrayList<>(Arrays.asList("chestplate", "taintedchestplate"));
    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER_CHESTPLATE;
    }

    @Override
    public String getName() {
        return "&5Fresh Tainted Chestplate";
    }

    @Override
    public List<String> getLore() {
        return new ALoreBuilder(
                "&7Used in the tainted well",
                "",
                "&7Kept on death"
        ).getLore();
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = new AItemStackBuilder(getMaterial())
                .setName(getName())
                .setLore(getLore())
                .getItemStack();
        itemStack = buildItem(itemStack);

        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(Color.fromRGB(PantColor.TAINTED.hexColor));
        itemStack.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.addCompound(NBTTag.MYSTIC_ENCHANTS.getRef());
        return nbtItem.getItem();
    }

    @Override
    public ItemStack getItem(int amount) {
        if(amount == 1) return getItem();
        throw new RuntimeException();
    }

    @Override
    public void updateItem(ItemStack itemStack) {
        if(!defaultUpdateItem(itemStack)) return;
        boolean hasLives = MysticFactory.hasLives(itemStack);

        NBTItem nbtItem = new NBTItem(itemStack);
        Integer enchantNum = nbtItem.getInteger(NBTTag.ITEM_ENCHANT_NUM.getRef());
        if(enchantNum == 0) {
            new AItemStackBuilder(itemStack)
                    .setName(getName())
                    .setLore(getLore());
            return;
        }

        if(getLives(itemStack) == 0 && hasLives) {
            itemStack.setType(Material.CHAINMAIL_CHESTPLATE);
            Misc.removeEnchantGlint(itemStack);
        } else {
            itemStack.setType(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
            meta.setColor(Color.fromRGB(PantColor.TAINTED.hexColor));
            itemStack.setItemMeta(meta);
            Misc.addEnchantGlint(itemStack);
        }

        EnchantManager.setItemLore(itemStack, null);
    }

    @Override
    public ItemStack getReplacementItem(PitPlayer pitPlayer, ItemStack itemStack, NBTItem nbtItem) {
        pitPlayer.giveSouls(30);
        return null;
    }

    @Override
    public boolean isLegacyItem(ItemStack itemStack, NBTItem nbtItem) {
        return nbtItem.hasKey(NBTTag.TAINTED_TIER.getRef()) &&
                (itemStack.getType() == Material.LEATHER_CHESTPLATE || itemStack.getType() == Material.CHAINMAIL_CHESTPLATE);
    }

    @Override
    public TemporaryType getTemporaryType() {
        return TemporaryType.LOOSES_LIVES_ON_DEATH;
    }
}
