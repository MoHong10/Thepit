package me.wisdom.thepit.cosmetics.bounty;

import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BountyPacking extends PitCosmetic {

    public BountyPacking() {
        super("&7Packing", "packing", CosmeticType.BOUNTY_CLAIM_MESSAGE);
    }

    @Override
    public String getBountyClaimMessage(String killerName, String deadName, String bounty) {
        return deadName + "&7 was sent packing by " + killerName + "&7 and left behind " + bounty;
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.CHEST)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7Send your foes packing!"
                ))
                .getItemStack();
        return itemStack;
    }
}
