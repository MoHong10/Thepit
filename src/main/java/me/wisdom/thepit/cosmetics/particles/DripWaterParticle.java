package me.wisdom.thepit.cosmetics.particles;

import me.wisdom.thepit.cosmetics.PitParticle;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Location;

public class DripWaterParticle extends PitParticle {
    public DripWaterParticle() {
    }

    public DripWaterParticle(boolean accountForPitch, boolean accountForYaw) {
        super(accountForPitch, accountForYaw);
    }

    @Override
    public void display(EntityPlayer entityPlayer, Location location, ParticleColor particleColor) {
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldParticles(
                EnumParticle.DRIP_WATER, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(),
                0, 0, 0, 0, 0
        ));
    }
}
