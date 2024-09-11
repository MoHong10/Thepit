package me.wisdom.thepit.cosmetics.bounty;

import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BountyBully extends PitCosmetic {

    public BountyBully() {
        super("&5Bully", "bully", CosmeticType.BOUNTY_CLAIM_MESSAGE);
    }

    @Override
    public String getBountyClaimMessage(String killerName, String deadName, String bounty) {
        return killerName + "&7 bullied " + deadName + "&7 into giving them " + bounty;
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.OBSIDIAN)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7Become the biggest bully on",
                        "&7the block by claiming bounties",
                        "&7left and right!"
                ))
                .getItemStack();
        return itemStack;
    }
}
