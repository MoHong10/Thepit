package me.wisdom.thepit.cosmetics.aura;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.cosmetics.*;
import me.wisdom.thepit.cosmetics.collections.ParticleCollection;
import me.wisdom.thepit.cosmetics.particles.FlameParticle;
import me.wisdom.thepit.cosmetics.particles.LavaParticle;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FireAura extends PitCosmetic {
    public ParticleCollection collection = new ParticleCollection();

    public FireAura() {
        super("&cFire Aura", "fireaura", CosmeticType.AURA);
        accountForPitch = false;

        PitParticle fireParticle = new FlameParticle(accountForPitch, accountForYaw);
        PitParticle lavalParticle = new LavaParticle(accountForPitch, accountForYaw);
        double distance = 6;
        collection.addParticle("fire", fireParticle, new ParticleOffset(0, distance / 4, 0, distance, distance / 2 + 2, distance));
        collection.addParticle("lava", lavalParticle, new ParticleOffset(0, distance / 4, 0, distance, distance / 2 + 2, distance));
    }

    @Override
    public void onEnable(PitPlayer pitPlayer) {
        runnableMap.put(pitPlayer.player.getUniqueId(), new BukkitRunnable() {
            @Override
            public void run() {
                Location displayLocation = pitPlayer.player.getLocation();

                for(Player onlinePlayer : CosmeticManager.getDisplayPlayers(pitPlayer.player, displayLocation)) {
                    PitPlayer onlinePitPlayer = PitPlayer.getPitPlayer(onlinePlayer);
                    if(onlinePlayer != pitPlayer.player && !onlinePitPlayer.playerSettings.auraParticles) continue;

                    EntityPlayer entityPlayer = ((CraftPlayer) onlinePlayer).getHandle();
                    if(Math.random() < 0.67) {
                        collection.display("fire", entityPlayer, displayLocation);
                    } else {
                        collection.display("lava", entityPlayer, displayLocation);
                    }
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
        ItemStack itemStack = new AItemStackBuilder(Material.FLINT_AND_STEEL)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7你周围的一切都是既定的",
                        "&7着火!"
                ))
                .getItemStack();
        return itemStack;
    }
}
