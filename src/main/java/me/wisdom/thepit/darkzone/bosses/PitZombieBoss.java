package me.wisdom.thepit.darkzone.bosses;

import me.wisdom.thepit.darkzone.DarkzoneBalancing;
import me.wisdom.thepit.darkzone.DropPool;
import me.wisdom.thepit.darkzone.PitBoss;
import me.wisdom.thepit.darkzone.SubLevelType;
import me.wisdom.thepit.darkzone.abilities.ComboAbility;
import me.wisdom.thepit.darkzone.abilities.PoundAbility;
import me.wisdom.thepit.darkzone.abilities.PullAbility;
import me.wisdom.thepit.darkzone.abilities.minion.GenericMinionAbility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

public class PitZombieBoss extends PitBoss {

    public PitZombieBoss(Player summoner) {
        super(summoner);

        abilities(
                new GenericMinionAbility(1, SubLevelType.ZOMBIE, 2, 8),
                new PullAbility(2, 20, new MaterialData(Material.DIRT, (byte) 0)),
                new PoundAbility(2, 5),

                new ComboAbility(8, 5, getDamage() * 0.2),
                null
        );
    }

    @Override
    public SubLevelType getSubLevelType() {
        return SubLevelType.ZOMBIE;
    }

    @Override
    public String getRawDisplayName() {
        return "Zombie Boss";
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.RED;
    }

    @Override
    public String getSkinName() {
        return "Zombie";
    }

    @Override
    public double getMaxHealth() {
        return DarkzoneBalancing.getAttributeAsInt(getSubLevelType(), DarkzoneBalancing.Attribute.BOSS_HEALTH);
    }

    @Override
    public double getDamage() {
        return DarkzoneBalancing.getAttribute(getSubLevelType(), DarkzoneBalancing.Attribute.BOSS_DAMAGE);
    }

    @Override
    public double getReach() {
        return 3;
    }

    @Override
    public double getReachRanged() {
        return 0;
    }

    @Override
    public int getSpeedLevel() {
        return 2;
    }

    @Override
    public int getDroppedSouls() {
        return DarkzoneBalancing.getAttributeAsRandomInt(getSubLevelType(), DarkzoneBalancing.Attribute.BOSS_SOULS);
    }

    @Override
    public DropPool createDropPool() {
        return new DropPool();
    }
}
