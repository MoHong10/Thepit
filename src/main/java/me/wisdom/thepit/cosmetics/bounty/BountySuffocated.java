package me.wisdom.thepit.cosmetics.bounty;

import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BountySuffocated extends PitCosmetic {

    public BountySuffocated() {
        super("&7S&2u&7f&2f&7o&2c&7a&2t&7i&2o&7n", "suffocate", CosmeticType.BOUNTY_CLAIM_MESSAGE);
    }

    @Override
    public String getBountyClaimMessage(String killerName, String deadName, String bounty) {
        return deadName + "&7 suffocated in a wall";
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.INK_SACK, 1, 15)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7Is it legal to cement them into",
                        "&7the walls of this place?"
                ))
                .getItemStack();
        return itemStack;
    }
}
