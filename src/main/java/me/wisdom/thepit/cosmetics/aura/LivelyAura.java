package me.wisdom.thepit.cosmetics.aura;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.cosmetics.*;
import me.wisdom.thepit.cosmetics.collections.ParticleCollection;
import me.wisdom.thepit.cosmetics.particles.VillagerHappyParticle;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class LivelyAura extends PitCosmetic {
    public ParticleCollection collection = new ParticleCollection();

    public LivelyAura() {
        super("&aLively Aura", "livelyaura", CosmeticType.AURA);
        accountForPitch = false;

        PitParticle particle = new VillagerHappyParticle(accountForPitch, accountForYaw);
        double distance = 6;
        collection.addParticle("main", particle, new ParticleOffset(0, distance / 4, 0, distance, distance / 2 + 2, distance));
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
                    collection.display("main", entityPlayer, displayLocation);
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
        ItemStack itemStack = new AItemStackBuilder(Material.LEAVES)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7让大自然重现生机!"
                ))
                .getItemStack();
        return itemStack;
    }
}
