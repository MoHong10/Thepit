package me.wisdom.thepit.cosmetics.bounty;

import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BountyRailed extends PitCosmetic {

    public BountyRailed() {
        super("&8Railed", "railed", CosmeticType.BOUNTY_CLAIM_MESSAGE);
    }

    @Override
    public String getBountyClaimMessage(String killerName, String deadName, String bounty) {
        return killerName + "&7 railed " + deadName + "&7 and took " + bounty;
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.RAILS)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7Hasta la vista, baby!"
                ))
                .getItemStack();
        return itemStack;
    }
}
