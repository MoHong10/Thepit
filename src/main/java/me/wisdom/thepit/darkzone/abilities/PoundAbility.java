package me.wisdom.thepit.darkzone.abilities;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.cosmetics.ParticleOffset;
import me.wisdom.thepit.cosmetics.PitParticle;
import me.wisdom.thepit.cosmetics.particles.BlockCrackParticle;
import me.wisdom.thepit.cosmetics.particles.ExplosionLargeParticle;
import me.wisdom.thepit.cosmetics.particles.SmokeLargeParticle;
import me.wisdom.thepit.darkzone.PitBossAbility;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.misc.effects.FallingBlock;
import me.wisdom.thepit.misc.effects.PacketBlock;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PoundAbility extends PitBossAbility {
    public int radius;
    public boolean isActive = false;
    List<Player> effectedPlayers = new ArrayList<>();

    public PoundAbility(double routineWeight, int radius) {
        super(routineWeight);
        this.radius = radius;
    }

    @Override
    public void onRoutineExecute() {
        isActive = true;
        Location centerLocation = getPitBoss().getBoss().getLocation().clone().subtract(0, 1, 0);

        List<Block> applicableBlocks = new ArrayList<>();

        for(int x = -1 * radius; x < radius + 1; x++) {
            for(int z = -1 * radius; z < radius + 1; z++) {
                Location blockLocation = centerLocation.clone().add(x, 0, z);

                if(blockLocation.distance(centerLocation) > radius) continue;

                if(blockLocation.getBlock().getType() != Material.AIR && blockLocation.clone().add(0, 1, 0).getBlock().getType() == Material.AIR) {
                    applicableBlocks.add(blockLocation.getBlock());
                    continue;
                }

                for(int i = -2; i < 3; i++) {
                    Location checkPosition = blockLocation.clone().add(0, i, 0);
                    if(checkPosition.getBlock().getType() == Material.AIR || checkPosition.clone().add(0, 1, 0).getBlock().getType() != Material.AIR)
                        continue;
                    applicableBlocks.add(checkPosition.getBlock());
                }
            }
        }

        List<Player> viewers = new ArrayList<>();
        for(Entity entity : getPitBoss().getBoss().getNearbyEntities(50, 50, 50)) {
            if(!(entity instanceof Player)) continue;
            Player player = Bukkit.getPlayer(entity.getUniqueId());
            if(player != null) viewers.add(player);
        }


        for(Block block : applicableBlocks) {
            Vector vector = new Vector(0, 0.6, 0);

            FallingBlock fallingBlock = new FallingBlock(block.getType(), block.getData(), block.getLocation().add(0, 1, 0));
            fallingBlock.setViewers(viewers);
            fallingBlock.spawnBlock();
            fallingBlock.removeAfter(25);
            fallingBlock.setVelocity(vector);

            new PacketBlock(Material.BARRIER, (byte) 0, block.getLocation())
                    .setViewers(viewers)
                    .spawnBlock()
                    .removeAfter(25);
        }

        PitParticle dirt = new BlockCrackParticle(new MaterialData(Material.DIRT));
        PitParticle smoke = new SmokeLargeParticle();
        PitParticle explosion = new ExplosionLargeParticle();

        for(Player viewer : viewers) {
            EntityPlayer nmsPlayer = ((CraftPlayer) viewer).getHandle();

            for(int i = 0; i < 200; i++) {
                if(i < 5) explosion.display(nmsPlayer, centerLocation, new ParticleOffset(0, 4, 0, 10, 4, 10));
                if(i < 15) smoke.display(nmsPlayer, centerLocation, new ParticleOffset(0, 4, 0, 10, 10, 10));
                dirt.display(nmsPlayer, centerLocation, new ParticleOffset(0, 4, 0, 10, 10, 10));
            }

            if(viewer.getLocation().distance(getPitBoss().getBoss().getLocation()) > radius + 2) continue;
            effectedPlayers.add(viewer);

            Vector vector = new Vector(0, 0.6, 0);
            vector.setY(vector.getY() + 1.5);
            vector.normalize();

            viewer.setVelocity(vector);
        }

        for(PitMob mob : getPitBoss().getSubLevel().mobs) {
            if(mob.getMob().getLocation().distance(getPitBoss().getBoss().getLocation()) > radius + 2) continue;
            Vector vector = new Vector(0, 0.6, 0);
            vector.setY(vector.getY() + 1.5);
            vector.normalize();

            mob.getMob().setVelocity(vector);
        }

        Sounds.EXTRACT.play(getPitBoss().getBoss().getLocation());

        new BukkitRunnable() {
            @Override
            public void run() {
                isActive = false;
                effectedPlayers.clear();
            }
        }.runTaskLater(Thepit.INSTANCE, 25);
    }

    @EventHandler
    public void onAttack(AttackEvent.Pre event) {
        if(effectedPlayers.contains(event.getDefenderPlayer())) event.setCancelled(true);
    }
}
