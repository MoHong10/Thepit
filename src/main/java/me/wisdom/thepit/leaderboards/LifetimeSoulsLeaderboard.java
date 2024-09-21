package me.wisdom.thepit.leaderboards;

import me.wisdom.thepit.controllers.objects.Leaderboard;
import me.wisdom.thepit.controllers.objects.LeaderboardData;
import me.wisdom.thepit.controllers.objects.LeaderboardPosition;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class LifetimeSoulsLeaderboard extends Leaderboard {
    public LifetimeSoulsLeaderboard() {
        super("lifetime-souls", "&fLifetime Souls");
    }

    @Override
    public ItemStack getDisplayStack(UUID uuid) {
        ItemStack itemStack = new AItemStackBuilder(Material.INK_SACK, 1, (byte) 7)
                .setName("&fLifetime Souls")
                .setLore(new ALoreBuilder(
                        "&7Players who have &fgathered &7the", "&7most &fTainted Souls", ""
                ).addLore(getTopPlayers(uuid)).addLore(
                        "", "&eClick to pick!"
                ))
                .getItemStack();
        return itemStack;
    }

    @Override
    public String getDisplayValue(LeaderboardPosition position) {
        return "&f" + Formatter.formatLarge(position.intValue) + " soul" + (position.intValue == 1 ? "" : "s");
    }

    @Override
    public String getDisplayValue(PitPlayer pitPlayer) {
        return "&f" + Formatter.formatLarge(pitPlayer.stats.lifetimeSouls) + " soul" + (pitPlayer.stats.lifetimeSouls == 1 ? "" : "s");
    }

    @Override
    public void setPosition(LeaderboardPosition position) {
        LeaderboardData data = LeaderboardData.getLeaderboardData(this);
        position.intValue = (int) data.getValue(position.uuid).primaryValue;
    }

    @Override
    public boolean isMoreThanOrEqual(LeaderboardPosition position, LeaderboardPosition otherPosition) {
        return position.intValue >= otherPosition.intValue;
    }
}
