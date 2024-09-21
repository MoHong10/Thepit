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

public class TurmoilPedestal extends AltarPedestal {
    public TurmoilPedestal(Location location) {
        super(location);
    }

    @Override
    public String getDisplayName() {
        return "&2&lTURMOIL";
    }

    @Override
    public ParticleColor getParticleColor() {
        return ParticleColor.DARK_GREEN;
    }

    @Override
    public int getActivationCost() {
        return 1;
    }

    @Override
    public ItemStack getItem(Player player) {
        AItemStackBuilder builder = new AItemStackBuilder(Material.SAPLING, 1, 3)
                .setName("&2Pedestal of Turmoil")
                .setLore(new ALoreBuilder(
                        "&7This pedestal greatly &2randomizes",
                        "&7your reward chances",
                        "",
                        "&7Activation Cost: &f" + getActivationCost() + " Souls",
                        "&7Status: " + getStatus(player)
                ));
        if(isActivated(player)) Misc.addEnchantGlint(builder.getItemStack());

        return builder.getItemStack();
    }
}
