package me.wisdom.thepit.cosmetics.bounty;

import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BountyRobbery extends PitCosmetic {

    public BountyRobbery() {
        super("&6R&co&6b&cb&6e&cr&6y", "robbery", CosmeticType.BOUNTY_CLAIM_MESSAGE);
    }

    @Override
    public String getBountyClaimMessage(String killerName, String deadName, String bounty) {
        return killerName + "&7 robbed " + deadName + "&7 for " + bounty;
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.GOLD_INGOT)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7Put your hands in the air, this",
                        "&7is a robbery!"
                ))
                .getItemStack();
        return itemStack;
    }
}
