package me.wisdom.thepit.cosmetics.bounty;

import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BountyRatted extends PitCosmetic {

    public BountyRatted() {
        super("&4&lRat", "rat", CosmeticType.BOUNTY_CLAIM_MESSAGE);
    }

    @Override
    public String getBountyClaimMessage(String killerName, String deadName, String bounty) {
        return killerName + "&7 ratted " + deadName + "&7 and stole " + bounty;
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.SPONGE)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7You just activated my trap card!"
                ))
                .getItemStack();
        return itemStack;
    }
}
