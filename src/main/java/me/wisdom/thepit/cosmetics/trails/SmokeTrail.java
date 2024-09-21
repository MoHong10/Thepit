package me.wisdom.thepit.cosmetics.trails;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.cosmetics.*;
import me.wisdom.thepit.cosmetics.collections.ParticleCollection;
import me.wisdom.thepit.cosmetics.particles.SmokeLargeParticle;
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

public class SmokeTrail extends PitCosmetic {
    public ParticleCollection collection = new ParticleCollection();

    public SmokeTrail() {
        super("&8Smoke Trail", "smoketrail", CosmeticType.PARTICLE_TRAIL);
        accountForPitch = false;

        PitParticle particle = new SmokeLargeParticle(accountForPitch, accountForYaw);

        for(int i = 0; i < 2; i++) {
            Vector vector = new Vector(0, 0, 0);
            collection.addParticle("main", particle, new ParticleOffset(vector, 1, 1, 1));
        }
    }

    @Override
    public void onEnable(PitPlayer pitPlayer) {
        runnableMap.put(pitPlayer.player.getUniqueId(), new BukkitRunnable() {
            @Override
            public void run() {
                if(CosmeticManager.isStandingStill(pitPlayer.player)) return;
                Location displayLocation = pitPlayer.player.getLocation();
                for(Player onlinePlayer : CosmeticManager.getDisplayPlayers(pitPlayer.player, displayLocation)) {
                    PitPlayer onlinePitPlayer = PitPlayer.getPitPlayer(onlinePlayer);
                    if(onlinePlayer != pitPlayer.player && !onlinePitPlayer.playerSettings.trailParticles) continue;

                    EntityPlayer entityPlayer = ((CraftPlayer) onlinePlayer).getHandle();
                    collection.displayAll(entityPlayer, displayLocation);
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 1L));
    }

    @Override
    public void onDisable(PitPlayer pitPlayer) {
        if(runnableMap.containsKey(pitPlayer.player.getUniqueId()))
            runnableMap.get(pitPlayer.player.getUniqueId()).cancel();
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.FIREWORK_CHARGE)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7Your feet move so fast they",
                        "&7could probably start a fire!"
                ))
                .getItemStack();
        return itemStack;
    }
}
