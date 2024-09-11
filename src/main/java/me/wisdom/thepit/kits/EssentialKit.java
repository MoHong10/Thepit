package me.wisdom.thepit.kits;

import me.wisdom.thepit.controllers.objects.Kit;
import me.wisdom.thepit.enums.KitItem;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EssentialKit extends Kit {
    @Override
    public void addItems() {
        items.add(KitItem.DIAMOND_HELMET);
        items.add(KitItem.DIAMOND_CHESTPLATE);
        items.add(KitItem.DIAMOND_BOOTS);
    }

    @Override
    public ItemStack getDisplayStack() {
        ItemStack itemStack = new AItemStackBuilder(Material.DIAMOND_CHESTPLATE)
                .setName("&7Essential Kit")
                .setLore(new ALoreBuilder(
                        "&7Contains Diamond Armor"
                )).getItemStack();
        return itemStack;
    }
}
