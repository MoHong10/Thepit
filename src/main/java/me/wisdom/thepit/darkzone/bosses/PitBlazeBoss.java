package me.wisdom.thepit.darkzone.bosses;

import me.wisdom.thepit.darkzone.DarkzoneBalancing;
import me.wisdom.thepit.darkzone.DropPool;
import me.wisdom.thepit.darkzone.PitBoss;
import me.wisdom.thepit.darkzone.SubLevelType;
import me.wisdom.thepit.darkzone.abilities.ComboAbility;
import me.wisdom.thepit.darkzone.abilities.PopupAbility;
import me.wisdom.thepit.darkzone.abilities.PullAbility;
import me.wisdom.thepit.darkzone.abilities.WorldBorderAbility;
import me.wisdom.thepit.darkzone.abilities.blockrain.FirestormAbility;
import me.wisdom.thepit.darkzone.abilities.minion.GenericMinionAbility;
import me.wisdom.thepit.misc.BlockData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

public class PitBlazeBoss extends PitBoss {

    public PitBlazeBoss(Player summoner) {
        super(summoner);

        abilities(
                new GenericMinionAbility(1, SubLevelType.BLAZE, 2, 5),
                new FirestormAbility(2, 25, 200, getDamage() * 0.75),
                new PopupAbility(2, new BlockData(Material.FIRE, (byte) 0), getDamage(), 40, 150),
                new PullAbility(2, 20, new MaterialData(Material.GLOWSTONE, (byte) 0)),

                new ComboAbility(15, 12, getDamage() * 0.2),
                new WorldBorderAbility(),
                null
        );
    }

    @Override
    public SubLevelType getSubLevelType() {
        return SubLevelType.BLAZE;
    }

    @Override
    public String getRawDisplayName() {
        return "Blaze Boss";
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.RED;
    }

    @Override
    public String getSkinName() {
        return "Blaze";
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
        return -1;
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
