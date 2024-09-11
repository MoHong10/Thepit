package me.wisdom.thepit.cosmetics.misc;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.cosmetics.*;
import me.wisdom.thepit.cosmetics.collections.ParticleCollection;
import me.wisdom.thepit.cosmetics.particles.RedstoneParticle;
import me.wisdom.thepit.misc.math.RotationUtils;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Halo extends ColorableCosmetic {
    public ParticleCollection collection = new ParticleCollection();

    public Halo() {
        super("&7&lHalo", "halo", CosmeticType.MISC);
        isPermissionRequired = true;

        PitParticle particle = new RedstoneParticle(accountForPitch, accountForYaw);

        for(int i = 0; i < 20; i++) {
            Vector vector = new Vector(0.6, 0.7, 0);
            RotationUtils.rotate(vector, 18 * i, 0, 0);
            vector.add(new Vector(0, 0, 0));
            collection.addParticle("main", particle, new ParticleOffset(vector));
        }
    }

    @Override
    public void onEnable(PitPlayer pitPlayer) {
        runnableMap.put(pitPlayer.player.getUniqueId(), new BukkitRunnable() {
            @Override
            public void run() {
                if(!CosmeticManager.isStandingStill(pitPlayer.player)) return;
                Location displayLocation = pitPlayer.player.getLocation();
                displayLocation.add(0, 1.5, 0);

                for(Player onlinePlayer : CosmeticManager.getDisplayPlayers(pitPlayer.player, displayLocation)) {
                    PitPlayer onlinePitPlayer = PitPlayer.getPitPlayer(onlinePlayer);
                    if(onlinePlayer != pitPlayer.player && !onlinePitPlayer.playerSettings.auraParticles) continue;

                    EntityPlayer entityPlayer = ((CraftPlayer) onlinePlayer).getHandle();
                    collection.display("main", entityPlayer, displayLocation, getColor(pitPlayer));
                }
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
        ItemStack itemStack = new AItemStackBuilder(Material.NETHER_STAR)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7Collect all the colors!"
                ))
                .getItemStack();
        return itemStack;
    }
}
