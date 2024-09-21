package me.wisdom.thepit.darkzone.abilities.minion;

import me.wisdom.thepit.darkzone.PitBossAbility;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.SubLevel;
import me.wisdom.thepit.darkzone.SubLevelType;
import me.wisdom.thepit.enums.MobStatus;
import me.wisdom.thepit.enums.PitEntityType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.events.PitQuitEvent;
import me.wisdom.thepit.misc.CustomPitBat;
import me.wisdom.thepit.misc.EntityManager;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.*;


public class EndermanMinionAbility extends MinionAbility {

    static {
        EntityManager.registerEntity("PitBat", 65, CustomPitBat.class);
    }

    public List<PitMob> minionList;
    public Map<PitMob, ItemRope> itemRopes;

    public EndermanMinionAbility(double routineWeight, SubLevelType subLevelType, int maxMobs) {
        super(routineWeight, subLevelType, maxMobs);

        minionList = new ArrayList<>();
        itemRopes = new HashMap<>();
    }

    @Override
    public void onRoutineExecute() {
        spawnMobs(null, 1);
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return subLevelType.getSubLevel().mobs.size() < maxMobs;
    }


    @Override
    public void spawnMobs(Location location, int spawnAmount) {
        for(int i = 0; i < spawnAmount; i++) {
            SubLevel subLevel = subLevelType.getSubLevel();
            if(subLevel.mobs.size() >= maxMobs) return;
            PitMob pitMob = subLevel.spawnMob(location, MobStatus.MINION);
            minionList.add(pitMob);
        }
    }

    @Override
    public void onDisable() {
        itemRopes.values().forEach(ItemRope::remove);
    }

    @EventHandler
    public void onHit(AttackEvent.Apply attackEvent) {
        if(!Misc.isEntity(attackEvent.getDefender(), PitEntityType.REAL_PLAYER)) return;

        for(PitMob pitMob : minionList) {
            LivingEntity livingEntity = pitMob.getMob();
            if(livingEntity != attackEvent.getAttacker()) continue;
            if(itemRopes.containsKey(pitMob)) return;

            Player player = (Player) attackEvent.getDefender();
            int itemSlot = new Random().nextInt(9);
            org.bukkit.inventory.ItemStack firstItem = player.getInventory().getItem(itemSlot);
            if(firstItem == null) return;
            player.getInventory().setItem(itemSlot, null);

            ItemRope itemRope = new ItemRope(pitMob, this, firstItem, player);
            itemRopes.put(pitMob, itemRope);
        }
    }

    @EventHandler
    public void onPreAttack(AttackEvent.Pre attackEvent) {
        if(attackEvent.getDefender() instanceof CustomPitBat) attackEvent.setCancelled(true);
    }

    @EventHandler
    public void onEndermanDeath(KillEvent killEvent) {
        if(!Misc.isEntity(killEvent.getKiller(), PitEntityType.REAL_PLAYER)) return;

        for(PitMob pitMob : minionList) {
            LivingEntity livingEntity = pitMob.getMob();
            if(livingEntity != killEvent.getDead()) continue;
            ItemRope itemRope = itemRopes.get(pitMob);
            if(itemRope == null) continue;

            itemRope.remove();
            itemRopes.remove(pitMob);
        }
    }

    @EventHandler
    public void onQuit(PitQuitEvent event) {
        List<PitMob> toRemove = new ArrayList<>();

        for(Map.Entry<PitMob, ItemRope> entry : itemRopes.entrySet()) {
            ItemRope itemRope = entry.getValue();
            if(itemRope.currentlyHolding != event.getPlayer()) continue;

            AUtil.giveItemSafely(event.getPlayer(), itemRope.itemStack);

            itemRope.remove();
            toRemove.add(entry.getKey());
        }

        toRemove.forEach(itemRopes::remove);
    }

    public static class ItemRope {
        public PitMob pitMob;
        public PitBossAbility ability;

        public CustomPitBat bat;
        public EntityArmorStand armorStand;
        public org.bukkit.inventory.ItemStack itemStack;
        public Player currentlyHolding;

        public ItemRope(PitMob pitMob, PitBossAbility ability, org.bukkit.inventory.ItemStack itemStack, Player currentlyHolding) {
            this.pitMob = pitMob;
            this.ability = ability;
            this.itemStack = itemStack;
            this.currentlyHolding = currentlyHolding;

            spawnBat();
            sendArmorStand();
        }

        public void spawnBat() {
            World nmsWorld = ((CraftWorld) pitMob.getMob().getWorld()).getHandle();

            CustomPitBat bat = new CustomPitBat(nmsWorld, ((CraftEntity) pitMob.getMob()).getHandle());
            Location spawnLocation = pitMob.getMob().getLocation().add(0, 2, 0);

            bat.setLocation(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), 0, 0);
            nmsWorld.addEntity(bat);

            MobEffect mobEffect = new MobEffect(14, Integer.MAX_VALUE, 0, false, false);
            bat.addEffect(mobEffect);

            this.bat = bat;
        }

        public void sendArmorStand() {
            World nmsWorld = ((CraftWorld) pitMob.getMob().getWorld()).getHandle();
            Location spawnLocation = pitMob.getMob().getLocation().add(0, 2, 0);

            armorStand = new EntityArmorStand(nmsWorld, spawnLocation.getX(), spawnLocation.getY(),
                    spawnLocation.getZ());
            armorStand.setArms(true);
            armorStand.setBasePlate(false);
            armorStand.setRightArmPose(new Vector3f(0, 90, 330));
            armorStand.setInvisible(true);

            PacketPlayOutSpawnEntityLiving armorStandSpawn = new PacketPlayOutSpawnEntityLiving(armorStand);
            PacketPlayOutEntityEquipment standSword = new PacketPlayOutEntityEquipment(armorStand.getId(), 0,
                    CraftItemStack.asNMSCopy(itemStack));
            PacketPlayOutAttachEntity batAttach = new PacketPlayOutAttachEntity(0, armorStand, bat);
            PacketPlayOutAttachEntity attachPacket = new PacketPlayOutAttachEntity(1, bat,
                    ((CraftEntity) pitMob.getMob()).getHandle());

            for(Player player : ability.getViewers()) {
                EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

                nmsPlayer.playerConnection.sendPacket(armorStandSpawn);
                nmsPlayer.playerConnection.sendPacket(standSword);
                nmsPlayer.playerConnection.sendPacket(batAttach);
                nmsPlayer.playerConnection.sendPacket(attachPacket);
            }
        }

        public void remove() {
            if(itemStack != null && currentlyHolding != null) {
                AUtil.giveItemSafely(currentlyHolding, itemStack, true);
            }

            bat.getBukkitEntity().remove();

            PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(armorStand.getId());
            for(Player player : ability.getViewers()) {
                EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
                nmsPlayer.playerConnection.sendPacket(destroyPacket);
            }
        }
    }
}
