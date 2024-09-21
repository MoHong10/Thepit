package me.wisdom.thepit.darkzone.mobs;

import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.darkzone.*;
import me.wisdom.thepit.enums.MobStatus;
import me.wisdom.thepit.items.mobdrops.SpiderEye;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Spider;

public class PitSpider extends PitMob {

    public PitSpider(Location spawnLocation, MobStatus mobStatus) {
        super(spawnLocation, mobStatus);
    }

    @Override
    public Creature createMob(Location spawnLocation) {
        Spider spider = spawnLocation.getWorld().spawn(spawnLocation, Spider.class);
        spider.setCustomNameVisible(false);
        spider.setRemoveWhenFarAway(false);
        spider.setCanPickupItems(false);
        return spider;
    }

    @Override
    public SubLevelType getSubLevelType() {
        return SubLevelType.SPIDER;
    }

    @Override
    public String getRawDisplayName() {
        return isMinion() ? "Minion Spider" : "Spider";
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.BLACK;
    }

    @Override
    public int getMaxHealth() {
        int maxHealth = DarkzoneBalancing.getAttributeAsInt(getSubLevelType(), DarkzoneBalancing.Attribute.MOB_HEALTH);
        return isMinion() ? maxHealth * 2 : maxHealth;
    }

    @Override
    public double getDamage() {
        return DarkzoneBalancing.getAttribute(getSubLevelType(), DarkzoneBalancing.Attribute.MOB_DAMAGE);
    }

    @Override
    public int getSpeedAmplifier() {
        return isMinion() ? 2 : 1;
    }

    @Override
    public int getDroppedSouls() {
        return DarkzoneBalancing.getAttributeAsRandomInt(getSubLevelType(), DarkzoneBalancing.Attribute.MOB_SOULS);
    }

    @Override
    public DropPool createDropPool() {
        return new DropPool()
                .addRareItem(() -> ItemFactory.getItem(SpiderEye.class).getItem(), DarkzoneBalancing.MOB_ITEM_DROP_PERCENT);
    }

    @Override
    public PitNameTag createNameTag() {
        return new PitNameTag(this, PitNameTag.NameTagType.NAME_AND_HEALTH)
                .addMob(PitNameTag.RidingType.SMALL_MAGMA_CUBE);
    }
}
