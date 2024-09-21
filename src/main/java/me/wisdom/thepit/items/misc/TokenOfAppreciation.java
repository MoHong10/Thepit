package me.wisdom.thepit.items.misc;

import de.tr7zw.nbtapi.NBTItem;
import me.clip.placeholderapi.PlaceholderAPI;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.MarketCategory;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokenOfAppreciation extends PitItem {

    public TokenOfAppreciation() {
        hasUUID = true;
        hasDropConfirm = true;
        marketCategory = MarketCategory.MISC;
        hasEnchantGlint = true;
    }

    @Override
    public String getNBTID() {
        return "token-of-appreciation";
    }

    @Override
    public List<String> getRefNames() {
        return new ArrayList<>(Arrays.asList("token", "tokenofappreciation"));
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    public Material getMaterial() {
        return Material.MAGMA_CREAM;
    }

    public String getName() {
        return "&6Token of Appreciation";
    }

    public List<String> getLore(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        String giverString = nbtItem.getString(NBTTag.COOKIE_GIVER.getRef());
        String receiverString = nbtItem.getString(NBTTag.COOKIE_RECEIVER.getRef());

        return new ALoreBuilder(
                "&eSpecial item",
                "&7A token of appreciation for understanding",
                "&7why we have to reset the PitSim economy.",
                "&7Who knows, this might do something some day",
                "",
                "&7From: " + giverString,
                "&7To: " + receiverString
        ).getLore();
    }

    @Override
    public void updateItem(ItemStack itemStack) {
        if(!defaultUpdateItem(itemStack)) return;

        itemStack.setType(getMaterial());
        new AItemStackBuilder(itemStack)
                .setName(getName())
                .setLore(getLore(itemStack));
    }

    public ItemStack getItem(Player staff, Player receiver, int amount) {
        ItemStack itemStack = new ItemStack(getMaterial(), amount);
        itemStack = buildItem(itemStack);

        NBTItem nbtItem = new NBTItem(itemStack);
        String giverString = PlaceholderAPI.setPlaceholders(staff,
                "&8[%luckperms_primary_group_name%&8] %luckperms_prefix%") + staff.getName();
        String receiverString = PlaceholderAPI.setPlaceholders(receiver,
                "&8[%luckperms_primary_group_name%&8] %luckperms_prefix%") + receiver.getName();
        nbtItem.setString(NBTTag.COOKIE_GIVER.getRef(), giverString);
        nbtItem.setString(NBTTag.COOKIE_RECEIVER.getRef(), receiverString);
        itemStack = nbtItem.getItem();

        return new AItemStackBuilder(itemStack)
                .setName(getName())
                .setLore(getLore(itemStack))
                .getItemStack();
    }

    @Override
    public ItemStack getReplacementItem(PitPlayer pitPlayer, ItemStack itemStack, NBTItem nbtItem) {
        if(itemStack.getItemMeta() == null || itemStack.getItemMeta().getLore() == null) return null;
        ItemStack newItemStack = new ItemStack(getMaterial(), 1);
        newItemStack = buildItem(newItemStack);

        String receiverString = null;
        for(String line : itemStack.getItemMeta().getLore()) {
            if(!line.contains("To:")) continue;
            receiverString = line.replaceAll(".+To: ", "");
            break;
        }

        NBTItem newNBTItem = new NBTItem(newItemStack);
        String giverString = "&8[&9Dev&8] &9wiji1";
        newNBTItem.setString(NBTTag.COOKIE_GIVER.getRef(), giverString);
        newNBTItem.setString(NBTTag.COOKIE_RECEIVER.getRef(), receiverString);
        newItemStack = newNBTItem.getItem();

        return new AItemStackBuilder(newItemStack)
                .setName(getName())
                .setLore(getLore(newItemStack))
                .getItemStack();
    }

    @Override
    public boolean isLegacyItem(ItemStack itemStack, NBTItem nbtItem) {
        return nbtItem.hasKey(NBTTag.IS_TOKEN.getRef());
    }
}
