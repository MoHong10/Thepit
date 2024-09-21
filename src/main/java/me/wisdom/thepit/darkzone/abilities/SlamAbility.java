package me.wisdom.thepit.darkzone.abilities;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.DamageManager;
import me.wisdom.thepit.cosmetics.ParticleOffset;
import me.wisdom.thepit.cosmetics.PitParticle;
import me.wisdom.thepit.cosmetics.particles.BlockCrackParticle;
import me.wisdom.thepit.cosmetics.particles.ExplosionHugeParticle;
import me.wisdom.thepit.darkzone.PitBossAbility;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.misc.effects.FallingBlock;
import me.wisdom.thepit.misc.effects.PacketBlock;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlamAbility extends PitBossAbility {
    public int spawnRadius = 40;
    public int damageRadius = 3;
    public int blockCount;
    public double damage;

    PitParticle dirt = new BlockCrackParticle(new MaterialData(Material.DIRT));

    public SlamAbility(double routineWeight, int blockCount, double damage) {
        super(routineWeight);
        this.blockCount = blockCount;
        this.damage = damage;
    }

    @Override
    public void onRoutineExecute() {
        Location centerLocation = getPitBoss().getBoss().getLocation().clone().subtract(0, 1, 0);

        List<Block> applicableBlocks = new ArrayList<>();

        for(int x = -1 * spawnRadius; x < spawnRadius + 1; x++) {
            for(int z = -1 * spawnRadius; z < spawnRadius + 1; z++) {
                Location blockLocation = centerLocation.clone().add(x, 0, z);

                if(blockLocation.distance(centerLocation) > spawnRadius) continue;

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

        List<Location> usedLocations = new ArrayList<>();
        Random random = new Random();

        for(int i = 0; i < blockCount; i++) {
            int index = random.nextInt(applicableBlocks.size());
            Location spawnLocation = applicableBlocks.get(index).getLocation();
            if(usedLocations.contains(spawnLocation)) {
                i--;
                continue;
            }
            usedLocations.add(spawnLocation);
        }

        for(int i = 0; i < usedLocations.size(); i++) {
            Block block = usedLocations.get(i).getBlock();
            int delay = i * 2;
            int baseTime = blockCount > 20 ? 40 + blockCount : 40;

            new BukkitRunnable() {
                @Override
                public void run() {
                    GravitizedBlock gravitizedBlock = new GravitizedBlock(block);
                    gravitizedBlock.slamAfter(baseTime - delay);

                }
            }.runTaskLater(Thepit.INSTANCE, delay);


        }
    }

    public class GravitizedBlock {
        public Block block;
        public Location initialLocation;
        public FallingBlock fallingBlock;
        public PacketBlock packetBlock;

        public BukkitTask runnable;

        public GravitizedBlock(Block block) {
            this.block = block;
            this.initialLocation = block.getLocation();

            packetBlock = new PacketBlock(Material.BARRIER, (byte) 0, initialLocation)
                    .setViewers(getViewers())
                    .spawnBlock();

            initialLocation.add(0.5, 1, 0.5);
            spawnBlock();
        }

        public void spawnBlock() {
            fallingBlock = new FallingBlock(block.getType(), block.getData(), initialLocation);
            fallingBlock.setViewers(getViewers());
            fallingBlock.spawnBlock();

            new BukkitRunnable() {
                @Override
                public void run() {
                    fallingBlock.setVelocity(new Vector(0, 0.7, 0));
                    Sounds.SLAM.play(initialLocation, 20);

                    for(Player viewer : getViewers()) {

                        EntityPlayer entityPlayer = ((CraftPlayer) viewer).getHandle();

                        for(int j = 0; j < 25; j++) {
                            dirt.display(entityPlayer, initialLocation, new ParticleOffset(0, 1, 0, 1, 1, 1));
                        }
                    }
                }
            }.runTaskLater(Thepit.INSTANCE, 1);


            runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    fallingBlock.setVelocity(new Vector(0, 0.075, 0));
                }
            }.runTaskTimer(Thepit.INSTANCE, 10, 1);
        }

        public void slamAfter(int ticks) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    slam();
                }
            }.runTaskLater(Thepit.INSTANCE, ticks);
        }

        public void slam() {
            runnable.cancel();
            Vector vector = new Vector(0, -1.5, 0);
            fallingBlock.setVelocity(vector);
            fallingBlock.removeAfter(3);
            packetBlock.removeAfter(3);

            for(int i = 0; i < 10; i++) {
                Location location = initialLocation.clone().add(0, -i, 0);
                if(location.getBlock().getType() == Material.AIR) continue;

                for(Player viewer : getViewers()) {
                    EntityPlayer entityPlayer = ((CraftPlayer) viewer).getHandle();
                    new ExplosionHugeParticle().display(entityPlayer, location.add(0, 0.5, 0));
                    for(int j = 0; j < 25; j++) {
                        dirt.display(entityPlayer, initialLocation, new ParticleOffset(0, 0, 0, 1, 1, 1));
                    }

                    Sounds.SLAM_2.play(initialLocation, 20);

                    Location viewerLocation = viewer.getLocation();
                    double distance = viewerLocation.distance(initialLocation);

                    if(distance > damageRadius) continue;

                    double multiplier = Math.pow(5 - distance, 1.5);
                    Vector playerVector = viewerLocation.toVector().subtract(initialLocation.toVector());
                    playerVector.add(new Vector(0, 0.1, 0));
                    playerVector.normalize();
                    playerVector.multiply(0.3);
                    playerVector.multiply(multiplier);

                    viewer.setVelocity(playerVector);
                    DamageManager.createIndirectAttack(getPitBoss().getBoss(), viewer, damage);
                }
                break;
            }
        }
    }
}
