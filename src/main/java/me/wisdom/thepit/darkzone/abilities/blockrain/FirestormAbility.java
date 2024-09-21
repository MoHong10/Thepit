package me.wisdom.thepit.darkzone.abilities.blockrain;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.DamageManager;
import me.wisdom.thepit.misc.BlockData;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.misc.effects.FallingBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;

public class FirestormAbility extends BlockRainAbility {
    public static LinkedHashMap<BlockData, Double> blockMap = getBlocks();

    public FirestormAbility(double routineWeight, int radius, int blockCount, double damage) {
        super(routineWeight, radius, blockCount, blockMap, damage);
    }

    @Override
    public void onBlockLand(FallingBlock fallingBlock, Location location) {
        location.add(0.5, 0, 0.5);
        Material material = fallingBlock.getMaterial();

        if(material == Material.FIRE) Sounds.FIRE_EXTINGUISH.play(location, 20);
        else Sounds.BLOCK_LAND.play(location, 20);

        for(Entity nearbyEntity : location.getWorld().getNearbyEntities(location, 1.5, 1.5, 1.5)) {
            if(!(nearbyEntity instanceof Player)) continue;
            Player player = Bukkit.getPlayer(nearbyEntity.getUniqueId());
            if(player == null) continue;

            if(material == Material.FIRE) {
                if(player.getFireTicks() <= 0) {
                    player.setFireTicks(5 * 20);
                    new BukkitRunnable() {
                        int i = 0;

                        @Override
                        public void run() {
                            if(++i >= 5) cancel();
                            DamageManager.createIndirectAttack(getPitBoss().getBoss(), player, damage);
                        }
                    }.runTaskTimer(Thepit.INSTANCE, 0, 20);
                }
            } else DamageManager.createIndirectAttack(getPitBoss().getBoss(), player, damage * 3);
        }
    }

    public static LinkedHashMap<BlockData, Double> getBlocks() {
        LinkedHashMap<BlockData, Double> blocks = new LinkedHashMap<>();
        blocks.put(new BlockData(Material.FIRE, (byte) 0), 1.0);
        blocks.put(new BlockData(Material.NETHER_BRICK, (byte) 0), 1.0);
        return blocks;
    }
}
