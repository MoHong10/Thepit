package me.wisdom.thepit.darkzone.mobs;

import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.darkzone.*;
import me.wisdom.thepit.enums.MobStatus;
import me.wisdom.thepit.items.mobdrops.Leather;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTameEvent;

public class PitWolf extends PitMob {

    public PitWolf(Location spawnLocation, MobStatus mobStatus) {
        super(spawnLocation, mobStatus);
    }

    @EventHandler
    public void onTame(EntityTameEvent event) {
        LivingEntity entity = event.getEntity();
        if(!isThisMob(entity)) return;
        event.setCancelled(true);
    }

    @Override
    public SubLevelType getSubLevelType() {
        return SubLevelType.WOLF;
    }

    @Override
    public Creature createMob(Location spawnLocation) {
        Wolf wolf = spawnLocation.getWorld().spawn(spawnLocation, Wolf.class);
        wolf.setCustomNameVisible(false);
        wolf.setRemoveWhenFarAway(false);
        wolf.setCanPickupItems(false);

        return wolf;
    }

    @Override
    public String getRawDisplayName() {
        return isMinion() ? "Minion Wolf" : "Wolf";
    }

    @Override
    public String getRawDisplayNamePlural() {
        return "Wolves";
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.BLUE;
    }

    @Override
    public int getMaxHealth() {
        return isMinion() ? 10 : DarkzoneBalancing.getAttributeAsInt(getSubLevelType(), DarkzoneBalancing.Attribute.MOB_HEALTH);
    }

    @Override
    public double getDamage() {
        return DarkzoneBalancing.getAttribute(getSubLevelType(), DarkzoneBalancing.Attribute.MOB_DAMAGE);
    }

    @Override
    public int getSpeedAmplifier() {
        return isMinion() ? 3 : 1;
    }

    @Override
    public int getDroppedSouls() {
        return DarkzoneBalancing.getAttributeAsRandomInt(getSubLevelType(), DarkzoneBalancing.Attribute.MOB_SOULS);
    }

    @Override
    public DropPool createDropPool() {
        return new DropPool()
                .addRareItem(() -> ItemFactory.getItem(Leather.class).getItem(), DarkzoneBalancing.MOB_ITEM_DROP_PERCENT);
    }

    @Override
    public PitNameTag createNameTag() {
        return new PitNameTag(this, PitNameTag.NameTagType.NAME_AND_HEALTH)
                .addMob(PitNameTag.RidingType.SMALL_MAGMA_CUBE);
    }
}
