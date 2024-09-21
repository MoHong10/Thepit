package me.wisdom.thepit.darkzone.altar;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.PitEntityType;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.misc.AOutput;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class AltarRenownReward {

    public Player player;
    public int amount;
    public List<BukkitTask> bukkitTaskList = new ArrayList<>();
    public Map<EntityItem, Integer> items = new HashMap<>();
    public BukkitTask despawnRunnable;

    public AltarRenownReward(Player player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    public void spawn(Location location) {
        Random random = new Random();
        World world = ((CraftWorld) location.getWorld()).getHandle();


        for(Integer stackSize : Misc.createDistribution(amount, 3.0 / 5.0)) {
            double offsetX = (random.nextInt(20) - 10) * 0.1;
            double offsetZ = (random.nextInt(20) - 10) * 0.1;

            EntityItem entityItem = new EntityItem(world);
            Location spawnLocation = new Location(location.getWorld(), location.getX() + offsetX, location.getY(), location.getZ() + offsetZ);
            entityItem.setPosition(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ());
            ItemStack itemStack = new ItemStack(Material.QUARTZ, stackSize, (short) 0);
            entityItem.setItemStack(CraftItemStack.asNMSCopy(new ItemStack(itemStack)));
            items.put(entityItem, stackSize);

            PacketPlayOutSpawnEntity spawn = new PacketPlayOutSpawnEntity(entityItem, 2, stackSize);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawn);

            PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(entityItem.getId(), entityItem.getDataWatcher(), true);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(meta);

            bukkitTaskList.add(new BukkitRunnable() {
                @Override
                public void run() {
                    for(Entity entity : spawnLocation.getWorld().getNearbyEntities(spawnLocation, 1, 1, 1)) {
                        if(!Misc.isEntity(entity, PitEntityType.REAL_PLAYER)) continue;
                        if(entity.getUniqueId() != player.getUniqueId()) continue;

                        PacketPlayOutCollect packet = new PacketPlayOutCollect(entityItem.getId(), player.getEntityId());
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

                        Sounds.ITEM_PICKUP.play(player);

                        reward(stackSize);
                        items.remove(entityItem);
                        cancel();
                    }
                }
            }.runTaskTimer(Thepit.INSTANCE, 0, 5));
        }

        despawnRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                despawnReward();
            }
        }.runTaskLater(Thepit.INSTANCE, 30 * 20);
    }

    public void despawnReward() {
        for(Map.Entry<EntityItem, Integer> entry : items.entrySet()) {
            reward(entry.getValue());
            despawn(entry.getKey());
        }

        despawnRunnable.cancel();
        for(BukkitTask task : bukkitTaskList) task.cancel();
    }

    public void despawn(EntityItem item) {
        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(item.getId());
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(destroy);
    }

    public void reward(int amount) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(pitPlayer == null) return;
        pitPlayer.renown += amount;
        AOutput.send(player, "&4&lALTAR!&7 Gained &e+" + amount + " Renown");
    }
}
