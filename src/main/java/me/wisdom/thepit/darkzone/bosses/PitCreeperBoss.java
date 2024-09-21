package me.wisdom.thepit.darkzone.bosses;

import me.wisdom.thepit.darkzone.DarkzoneBalancing;
import me.wisdom.thepit.darkzone.DropPool;
import me.wisdom.thepit.darkzone.PitBoss;
import me.wisdom.thepit.darkzone.SubLevelType;
import me.wisdom.thepit.darkzone.abilities.LandMineAbility;
import me.wisdom.thepit.darkzone.abilities.LightningAbility;
import me.wisdom.thepit.darkzone.abilities.TNTAbility;
import me.wisdom.thepit.darkzone.abilities.WorldBorderAbility;
import me.wisdom.thepit.darkzone.abilities.minion.GenericMinionAbility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PitCreeperBoss extends PitBoss {

    public PitCreeperBoss(Player summoner) {
        super(summoner);

        abilities(
                new GenericMinionAbility(1, SubLevelType.CREEPER, 1, 2),
                new TNTAbility(2, getDamage() * 2),
                new LandMineAbility(2, 3, 20 * 3, 20 * 45, getDamage() * 15),

                new LightningAbility(4, 2, 0.1),
                new WorldBorderAbility(),
                null
        );
    }

    @Override
    public SubLevelType getSubLevelType() {
        return SubLevelType.CREEPER;
    }

    @Override
    public String getRawDisplayName() {
        return "Creeper Boss";
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.RED;
    }

    @Override
    public String getSkinName() {
        return "Creeper";
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
        return 2.5;
    }

    @Override
    public double getReachRanged() {
        return 0;
    }

    @Override
    public int getSpeedLevel() {
        return 6;
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
