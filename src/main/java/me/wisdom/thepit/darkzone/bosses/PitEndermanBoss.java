package me.wisdom.thepit.darkzone.bosses;

import me.wisdom.thepit.darkzone.DarkzoneBalancing;
import me.wisdom.thepit.darkzone.DropPool;
import me.wisdom.thepit.darkzone.PitBoss;
import me.wisdom.thepit.darkzone.SubLevelType;
import me.wisdom.thepit.darkzone.abilities.*;
import me.wisdom.thepit.darkzone.abilities.minion.GenericMinionAbility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PitEndermanBoss extends PitBoss {

    public PitEndermanBoss(Player summoner) {
        super(summoner);

        abilities(
//				new EndermanMinionAbility(2, getSubLevelType(), 5)
                new GenericMinionAbility(1, SubLevelType.ENDERMAN, 1, 5),
                new DisorderAbility(1, 2),
                new RuptureAbility(1, 40, getDamage() * 0.5),

                new ReincarnationAbility(),
                new TeleportAbility(8, 17),
                new WorldBorderAbility()
        );

        setRoutineAbilityCooldownTicks(getRoutineAbilityCooldownTicks() * 0.75);
    }

    @Override
    public SubLevelType getSubLevelType() {
        return SubLevelType.ENDERMAN;
    }

    @Override
    public String getRawDisplayName() {
        return "Enderman Boss";
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.RED;
    }

    @Override
    public String getSkinName() {
        return "Enderman";
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
        return 5;
    }

    @Override
    public double getReachRanged() {
        return 0;
    }

    @Override
    public int getSpeedLevel() {
        return 8;
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
