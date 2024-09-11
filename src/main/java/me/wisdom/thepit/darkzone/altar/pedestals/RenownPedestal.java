package me.wisdom.thepit.darkzone.altar.pedestals;

import me.wisdom.thepit.cosmetics.particles.ParticleColor;
import me.wisdom.thepit.darkzone.altar.AltarPedestal;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RenownPedestal extends AltarPedestal {
    public RenownPedestal(Location location) {
        super(location);
    }

    @Override
    public String getDisplayName() {
        return "&e&lRENOWN";
    }

    @Override
    public int getActivationCost() {
        return 100;
    }

    @Override
    public ParticleColor getParticleColor() {
        return ParticleColor.YELLOW;
    }

    @Override
    public ItemStack getItem(Player player) {
        AItemStackBuilder builder = new AItemStackBuilder(Material.QUARTZ)
                .setName("&ePedestal of Renown")
                .setLore(new ALoreBuilder(
                        "&7This pedestal increases your",
                        "&7chance of gaining &eRenown",
                        "",
                        "&7Activation Cost: &f" + getActivationCost() + " Souls",
                        "&7Status: " + getStatus(player)
                ));
        if(isActivated(player)) Misc.addEnchantGlint(builder.getItemStack());

        return builder.getItemStack();
    }
}
