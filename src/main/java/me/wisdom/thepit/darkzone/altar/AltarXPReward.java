package me.wisdom.thepit.darkzone.altar;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.darkzone.DarkzoneLeveling;
import me.wisdom.thepit.misc.Misc;
import net.minecraft.server.v1_8_R3.EntityExperienceOrb;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityExperienceOrb;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class AltarXPReward {
    public Player player;
    public int xp;

    public AltarXPReward(Player player, int xp) {
        this.player = player;
        this.xp = xp;
    }

    public void spawn(Location location) {
        Random random = new Random();
        World world = ((CraftWorld) location.getWorld()).getHandle();

        for(Integer stackSize : Misc.createDistribution(xp, 1.0 / 2.0)) {
            double offsetX = (random.nextInt(20) - 10) * 0.1;
            double offsetZ = (random.nextInt(20) - 10) * 0.1;

            EntityExperienceOrb orb = new EntityExperienceOrb(world, location.getX() + offsetX, location.getY(), location.getZ() + offsetZ, stackSize);
            PacketPlayOutSpawnEntityExperienceOrb spawn = new PacketPlayOutSpawnEntityExperienceOrb(orb);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawn);

            new BukkitRunnable() {
                @Override
                public void run() {
                    PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(orb.getId());
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(destroy);

                    DarkzoneLeveling.giveXP(PitPlayer.getPitPlayer(player), stackSize);
                }
            }.runTaskLater(Thepit.INSTANCE, new Random().nextInt(31) + 10);
        }
    }
}
