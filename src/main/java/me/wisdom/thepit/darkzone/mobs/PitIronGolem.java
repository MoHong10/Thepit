package me.wisdom.thepit.darkzone.mobs;

import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.darkzone.*;
import me.wisdom.thepit.enums.MobStatus;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.items.mobdrops.IronIngot;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.IronGolem;
import org.bukkit.event.EventHandler;

public class PitIronGolem extends PitMob {

    public PitIronGolem(Location spawnLocation, MobStatus mobStatus) {
        super(spawnLocation, mobStatus);
    }

    @EventHandler
    public void onFireballLaunch(AttackEvent.Apply attackEvent) {
        if(!isMinion() || !isThisMob(attackEvent.getDefender())) return;
        attackEvent.multipliers.add(0.5);
    }

    @Override
    public SubLevelType getSubLevelType() {
        return SubLevelType.IRON_GOLEM;
    }

    @Override
    public Creature createMob(Location spawnLocation) {
        IronGolem ironGolem = spawnLocation.getWorld().spawn(spawnLocation, IronGolem.class);
        ironGolem.setCustomNameVisible(false);
        ironGolem.setRemoveWhenFarAway(false);
        ironGolem.setCanPickupItems(false);

        return ironGolem;
    }

    @Override
    public String getRawDisplayName() {
        return isMinion() ? "Minion Golem" : "Iron Golem";
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.WHITE;
    }

    @Override
    public int getMaxHealth() {
        int maxHealth = DarkzoneBalancing.getAttributeAsInt(getSubLevelType(), DarkzoneBalancing.Attribute.MOB_HEALTH);
        return isMinion() ? maxHealth * 2 : maxHealth;
    }

    @Override
    public double getDamage() {
        double damage = DarkzoneBalancing.getAttribute(getSubLevelType(), DarkzoneBalancing.Attribute.MOB_DAMAGE);
        return isMinion() ? damage * 1.2 : damage;
    }

    @Override
    public int getSpeedAmplifier() {
        return isMinion() ? 4 : 1;
    }

    @Override
    public int getDroppedSouls() {
        return DarkzoneBalancing.getAttributeAsRandomInt(getSubLevelType(), DarkzoneBalancing.Attribute.MOB_SOULS);
    }

    @Override
    public DropPool createDropPool() {
        return new DropPool()
                .addRareItem(() -> ItemFactory.getItem(IronIngot.class).getItem(), DarkzoneBalancing.MOB_ITEM_DROP_PERCENT);
    }

    @Override
    public PitNameTag createNameTag() {
        return new PitNameTag(this, PitNameTag.NameTagType.NAME_AND_HEALTH)
                .addMob(PitNameTag.RidingType.BABY_RABBIT)
                .addMob(PitNameTag.RidingType.BABY_RABBIT);
    }
}
