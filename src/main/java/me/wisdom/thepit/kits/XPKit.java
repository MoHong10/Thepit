package me.wisdom.thepit.kits;

import me.wisdom.thepit.controllers.objects.Kit;
import me.wisdom.thepit.enums.KitItem;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class XPKit extends Kit {
    @Override
    public void addItems() {
        items.add(KitItem.EXE_SWEATY);
        items.add(KitItem.SWEATY_GHEART);
        items.add(KitItem.SWEATY_ELEC);
        items.add(KitItem.STREAKING_BILL_LS);
        items.add(KitItem.STREAKING_CH_LS);
        items.add(KitItem.VOLLEY_FTTS);
    }

    @Override
    public ItemStack getDisplayStack() {
        ItemStack itemStack = new AItemStackBuilder(Material.WHEAT)
                .setName("&bXP Streaking Kit")
                .setLore(new ALoreBuilder(
                        "&7Contains &bXP &7Streaking Mystics"
                )).getItemStack();
        return itemStack;
    }
}
