package me.wisdom.thepit.darkzone.bosses;

import me.wisdom.thepit.darkzone.DarkzoneBalancing;
import me.wisdom.thepit.darkzone.DropPool;
import me.wisdom.thepit.darkzone.PitBoss;
import me.wisdom.thepit.darkzone.SubLevelType;
import me.wisdom.thepit.darkzone.abilities.CollapseAbility;
import me.wisdom.thepit.darkzone.abilities.ComboAbility;
import me.wisdom.thepit.darkzone.abilities.WorldBorderAbility;
import me.wisdom.thepit.darkzone.abilities.blockrain.AnvilRainAbility;
import me.wisdom.thepit.darkzone.abilities.minion.IronGolemMinionAbility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PitIronGolemBoss extends PitBoss {

    public PitIronGolemBoss(Player summoner) {
        super(summoner);

        abilities(
                new IronGolemMinionAbility(),
                new AnvilRainAbility(2, 25, 250, getDamage() * 1.5),
                new CollapseAbility(2, 5, 5, 10, getDamage() * 12),

                new ComboAbility(5, 20, getDamage() * 0.2),
                new WorldBorderAbility(),
                null
        );

        setRoutineAbilityCooldownTicks(getRoutineAbilityCooldownTicks() * 0.9);
    }

    @Override
    public SubLevelType getSubLevelType() {
        return SubLevelType.IRON_GOLEM;
    }

    @Override
    public String getRawDisplayName() {
        return "Golem Boss";
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.RED;
    }

    @Override
    public String getSkinName() {
        return "IronGolem";
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
        return 4;
    }

    @Override
    public double getReachRanged() {
        return 0;
    }

    @Override
    public int getSpeedLevel() {
        return 0;
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
