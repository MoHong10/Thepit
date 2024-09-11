package me.wisdom.thepit.cosmetics.bounty;

import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BountyBlueShell extends PitCosmetic {

    public BountyBlueShell() {
        super("&9Blue &bShell", "blueshell", CosmeticType.BOUNTY_CLAIM_MESSAGE);
    }

    @Override
    public String getBountyClaimMessage(String killerName, String deadName, String bounty) {
        return killerName + "&7 threw a blue shell at " + deadName + "&7 and collected " + bounty;
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.INK_SACK, 1, 4)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7You'll always overtake first",
                        "&7place now!"
                ))
                .getItemStack();
        return itemStack;
    }
}
