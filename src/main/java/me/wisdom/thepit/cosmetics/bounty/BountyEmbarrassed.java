package me.wisdom.thepit.cosmetics.bounty;

import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BountyEmbarrassed extends PitCosmetic {

    public BountyEmbarrassed() {
        super("&cE&dm&cb&da&cr&dr&ca&ds&cs&de&cd", "embarassed", CosmeticType.BOUNTY_CLAIM_MESSAGE);
    }

    @Override
    public String getBountyClaimMessage(String killerName, String deadName, String bounty) {
        return killerName + "&7 embarrased " + deadName + "&7 into giving them " + bounty;
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.RED_ROSE)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7Oh, the embarrassment they",
                        "&7must feel!"
                ))
                .getItemStack();
        return itemStack;
    }
}
