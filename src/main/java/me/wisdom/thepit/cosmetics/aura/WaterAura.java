package me.wisdom.thepit.cosmetics.aura;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.cosmetics.*;
import me.wisdom.thepit.cosmetics.collections.ParticleCollection;
import me.wisdom.thepit.cosmetics.particles.WaterDropParticle;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class WaterAura extends PitCosmetic {
    public ParticleCollection collection = new ParticleCollection();

    public WaterAura() {
        super("&9Water &bAura", "wateraura", CosmeticType.AURA);
        accountForPitch = false;

        PitParticle particle = new WaterDropParticle(accountForPitch, accountForYaw);
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
                    for(int i = 0; i < 3; i++) collection.display("main", entityPlayer, displayLocation);
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
        ItemStack itemStack = new AItemStackBuilder(Material.WATER_BUCKET)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7*Splish Splash*"
                ))
                .getItemStack();
        return itemStack;
    }
}
