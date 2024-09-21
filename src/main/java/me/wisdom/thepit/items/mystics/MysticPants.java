package me.wisdom.thepit.items.mystics;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.MarketCategory;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.enums.PantColor;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.items.StaticPitItem;
import me.wisdom.thepit.items.TemporaryItem;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MysticPants extends StaticPitItem implements TemporaryItem {

    public MysticPants() {
        hasUUID = true;
        hasLastServer = true;
        hasDropConfirm = true;
        destroyIfDroppedInSpawn = true;
        hideExtra = true;
        unbreakable = true;
        isMystic = true;
        marketCategory = MarketCategory.OVERWORLD_GEAR;
    }

    @Override
    public String getNBTID() {
        return "mystic-pants";
    }

    @Override
    public List<String> getRefNames() {
        return new ArrayList<>(Arrays.asList("pants", "mysticpants"));
    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER_LEGGINGS;
    }

    public String getName(PantColor pantColor) {
        return pantColor.chatColor + "Fresh " + pantColor.displayName + " Pants";
    }

    @Override
    public String getName() {
        throw new RuntimeException();
    }

    public List<String> getLore(PantColor pantColor) {
        return new ALoreBuilder(
                pantColor.chatColor + "Used in the mystic well.",
                pantColor.chatColor + "Also a fashion statement",
                "",
                "&7Kept on death"
        ).getLore();
    }

    @Override
    public List<String> getLore() {
        throw new RuntimeException();
    }

    @Override
    public void updateItem(ItemStack itemStack) {
        if(!defaultUpdateItem(itemStack)) return;
        boolean isJewel = MysticFactory.isJewel(itemStack, false);
        boolean isJewelComplete = MysticFactory.isJewel(itemStack, true);

        NBTItem nbtItem = new NBTItem(itemStack);
        Integer enchantNum = nbtItem.getInteger(NBTTag.ITEM_ENCHANT_NUM.getRef());
        if(enchantNum == 0 && !isJewel) {
            PantColor pantColor = PantColor.getPantColor(itemStack);
            assert pantColor != null;
            new AItemStackBuilder(itemStack)
                    .setName(getName(pantColor))
                    .setLore(getLore(pantColor));
            return;
        }

        if(getLives(itemStack) == 0 && isJewelComplete) {
            itemStack.setType(Material.CHAINMAIL_LEGGINGS);
        } else {
            itemStack.setType(Material.LEATHER_LEGGINGS);
            PantColor.updatePantColor(itemStack);
        }

        EnchantManager.setItemLore(itemStack, null);
    }

    public ItemStack getItem(PantColor pantColor) {
        ItemStack itemStack = new AItemStackBuilder(Material.LEATHER_LEGGINGS)
                .setName(getName(pantColor))
                .setLore(new ALoreBuilder(getLore(pantColor)))
                .getItemStack();
        itemStack = buildItem(itemStack);
        itemStack = PantColor.setPantColor(itemStack, pantColor);

        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.addCompound(NBTTag.MYSTIC_ENCHANTS.getRef());
        return nbtItem.getItem();
    }

    @Override
    public ItemStack getItem() {
        return getItem(PantColor.getNormalRandom());
    }

    @Override
    public ItemStack getItem(int amount) {
        if(amount == 1) return getItem();
        throw new RuntimeException();
    }

    @Override
    public ItemStack getReplacementItem(PitPlayer pitPlayer, ItemStack itemStack, NBTItem nbtItem) {
        PantColor pantColor = nbtItem.hasKey(NBTTag.SAVED_PANTS_COLOR.getRef()) ?
                PantColor.getPantColor(nbtItem.getString(NBTTag.SAVED_PANTS_COLOR.getRef())) : PantColor.getNormalRandom();
        if(pantColor == null) pantColor = PantColor.getNormalRandom();
        switch(pantColor) {
            case DARK:
            case JEWEL:
                return null;
        }

        ItemStack newItemStack = new ItemStack(getMaterial(), 1);
        newItemStack = buildItem(newItemStack);
        NBTItem newNBTItem = new NBTItem(newItemStack);

        newNBTItem.addCompound(NBTTag.MYSTIC_ENCHANTS.getRef());
        NBTCompound newItemEnchants = newNBTItem.getCompound(NBTTag.MYSTIC_ENCHANTS.getRef());
        NBTCompound itemEnchants = nbtItem.getCompound(NBTTag.MYSTIC_ENCHANTS.getRef());
        for(String enchantKey : itemEnchants.getKeys()) {
            int enchantLvl = itemEnchants.getInteger(enchantKey);
            if(enchantKey.equals("comoswift")) enchantKey = "comboswift";
            if(enchantKey.equals("gotta-go-fast")) enchantKey = "gottagofast";
            if(enchantKey.equals("boo-boo")) enchantKey = "booboo";
            if(enchantLvl == 0) continue;
            newItemEnchants.setInteger(enchantKey, enchantLvl);
        }

        if(nbtItem.hasKey(NBTTag.ITEM_ENCHANT_NUM.getRef()))
            newNBTItem.setInteger(NBTTag.ITEM_ENCHANT_NUM.getRef(), nbtItem.getInteger(NBTTag.ITEM_ENCHANT_NUM.getRef()));
        if(nbtItem.hasKey(NBTTag.ITEM_TOKENS.getRef()))
            newNBTItem.setInteger(NBTTag.ITEM_TOKENS.getRef(), nbtItem.getInteger(NBTTag.ITEM_TOKENS.getRef()));
        if(nbtItem.hasKey(NBTTag.ITEM_RTOKENS.getRef()))
            newNBTItem.setInteger(NBTTag.ITEM_RTOKENS.getRef(), nbtItem.getInteger(NBTTag.ITEM_RTOKENS.getRef()));
        if(nbtItem.hasKey(NBTTag.CURRENT_LIVES.getRef()))
            newNBTItem.setInteger(NBTTag.CURRENT_LIVES.getRef(), nbtItem.getInteger(NBTTag.CURRENT_LIVES.getRef()));
        if(nbtItem.hasKey(NBTTag.MAX_LIVES.getRef()))
            newNBTItem.setInteger(NBTTag.MAX_LIVES.getRef(), nbtItem.getInteger(NBTTag.MAX_LIVES.getRef()));
        if(nbtItem.hasKey(NBTTag.ITEM_JEWEL_KILLS.getRef()))
            newNBTItem.setInteger(NBTTag.ITEM_JEWEL_KILLS.getRef(), nbtItem.getInteger(NBTTag.ITEM_JEWEL_KILLS.getRef()));

        if(nbtItem.hasKey(NBTTag.MYSTIC_ENCHANT_ORDER.getRef())) {
            List<String> enchantOrder = nbtItem.getStringList(NBTTag.MYSTIC_ENCHANT_ORDER.getRef());
            for(String refName : enchantOrder) {
                if(refName.equals("comoswift")) refName = "comboswift";
                if(refName.equals("gotta-go-fast")) refName = "gottagofast";
                if(refName.equals("boo-boo")) refName = "booboo";
                newNBTItem.getStringList(NBTTag.MYSTIC_ENCHANT_ORDER.getRef()).add(refName);
            }
        }

        if(nbtItem.hasKey(NBTTag.ITEM_JEWEL_ENCHANT.getRef()))
            newNBTItem.setString(NBTTag.ITEM_JEWEL_ENCHANT.getRef(), nbtItem.getString(NBTTag.ITEM_JEWEL_ENCHANT.getRef()));
        newNBTItem.setString(NBTTag.SAVED_PANTS_COLOR.getRef(), pantColor.displayName);
        if(nbtItem.hasKey(NBTTag.ORIGINAL_PANTS_COLOR.getRef()))
            newNBTItem.setString(NBTTag.ORIGINAL_PANTS_COLOR.getRef(), nbtItem.getString(NBTTag.ORIGINAL_PANTS_COLOR.getRef()));

        if(nbtItem.hasKey(NBTTag.IS_GEMMED.getRef()))
            newNBTItem.setBoolean(NBTTag.IS_GEMMED.getRef(), nbtItem.getBoolean(NBTTag.IS_GEMMED.getRef()));
        if(nbtItem.hasKey(NBTTag.IS_VENOM.getRef()))
            newNBTItem.setBoolean(NBTTag.IS_VENOM.getRef(), nbtItem.getBoolean(NBTTag.IS_VENOM.getRef()));
        if(nbtItem.hasKey(NBTTag.IS_JEWEL.getRef()))
            newNBTItem.setBoolean(NBTTag.IS_JEWEL.getRef(), nbtItem.getBoolean(NBTTag.IS_JEWEL.getRef()));
        newItemStack = newNBTItem.getItem();

        String name = itemStack.getItemMeta().getDisplayName();
        new AItemStackBuilder(newItemStack)
                .setName(name == null ? "&7Fresh Pants" : name);

        PantColor.updatePantColor(newItemStack);
        return newItemStack;
    }

    @Override
    public boolean isLegacyItem(ItemStack itemStack, NBTItem nbtItem) {
        return nbtItem.hasKey(NBTTag.ITEM_UUID.getRef()) &&
                (itemStack.getType() == Material.LEATHER_LEGGINGS || itemStack.getType() == Material.CHAINMAIL_LEGGINGS);
    }

    @Override
    public TemporaryType getTemporaryType() {
        return TemporaryType.LOOSES_LIVES_ON_DEATH;
    }
}
