package me.wisdom.thepit.cosmetics.killeffectsbot;

import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IronKill extends PitCosmetic {

    public IronKill() {
        super("&f&lIron Kill", "ironkill", CosmeticType.BOT_KILL_EFFECT);
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.ANVIL)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "",
                        "&7Become a &f&lBLACKSMITH &7and",
                        "&7listen to your enemies be smashed",
                        "&7by anvils!"
                ))
                .getItemStack();
        return itemStack;
    }
}
