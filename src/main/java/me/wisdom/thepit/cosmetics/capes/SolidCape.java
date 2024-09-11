package me.wisdom.thepit.cosmetics.capes;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.cosmetics.ColorableCosmetic;
import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.collections.CapeCollection;
import me.wisdom.thepit.cosmetics.particles.ParticleColor;
import me.wisdom.thepit.cosmetics.particles.RedstoneParticle;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class SolidCape extends ColorableCosmetic {
    public CapeCollection cape;

    public SolidCape() {
        super("&7&lSolid", "solidcape", CosmeticType.CAPE);
        accountForPitch = false;

        cape = new CapeCollection(new RedstoneParticle(accountForPitch, accountForYaw));
    }

    @Override
    public void onEnable(PitPlayer pitPlayer) {
        ParticleColor particleColor = getColor(pitPlayer);
        runnableMap.put(pitPlayer.player.getUniqueId(), new BukkitRunnable() {
            @Override
            public void run() {
                cape.draw(SolidCape.this, pitPlayer, particleColor);
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 2L));
    }

    @Override
    public void onDisable(PitPlayer pitPlayer) {
        if(runnableMap.containsKey(pitPlayer.player.getUniqueId()))
            runnableMap.get(pitPlayer.player.getUniqueId()).cancel();
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.BANNER, 1, 15)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7Collect all the colors!"
                ))
                .getItemStack();
        return itemStack;
    }
}
