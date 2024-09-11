package me.wisdom.thepit.items;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class StaticPitItem extends PitItem {
    public abstract Material getMaterial();
    public abstract String getName();
    public abstract List<String> getLore();

    @Override
    public int getMaxStackSize() {
        return getMaterial().getMaxStackSize();
    }

    @Override
    public void updateItem(ItemStack itemStack) {
        if(!defaultUpdateItem(itemStack)) return;

        itemStack.setType(getMaterial());
        new AItemStackBuilder(itemStack)
                .setName(getName())
                .setLore(getLore());
    }

    public ItemStack getItem() {
        return getItem(1);
    }

    public ItemStack getItem(int amount) {
        ItemStack itemStack = new AItemStackBuilder(getMaterial(), amount, itemData)
                .setName(getName())
                .setLore(getLore())
                .getItemStack();
        return buildItem(itemStack);
    }

    public void giveItem(Player player, int amount) {
        AUtil.giveItemSafely(player, getItem(amount), true);
    }

    @Override
    public ItemStack getReplacementItem(PitPlayer pitPlayer, ItemStack itemStack, NBTItem nbtItem) {
        return getItem(itemStack.getAmount());
    }
}
