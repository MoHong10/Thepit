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

public class BossesKilledLeaderboard extends Leaderboard {
    public BossesKilledLeaderboard() {
        super("bosses-killed", "&5Bosses Killed");
    }

    @Override
    public ItemStack getDisplayStack(UUID uuid) {
        ItemStack itemStack = new AItemStackBuilder(Material.MOB_SPAWNER)
                .setName("&5Bosses Killed")
                .setLore(new ALoreBuilder(
                        "&7Players who have &ckilled &7the most", "&5Bosses &7in the &5Darkzone", ""
                ).addLore(getTopPlayers(uuid)).addLore(
                        "", "&eClick to pick!"
                ))
                .getItemStack();
        return itemStack;
    }

    @Override
    public String getDisplayValue(LeaderboardPosition position) {
        return "&5" + Formatter.formatLarge(position.intValue) + " boss" + (position.intValue == 1 ? "" : "es");
    }

    @Override
    public String getDisplayValue(PitPlayer pitPlayer) {
        return "&5" + Formatter.formatLarge(pitPlayer.stats.bossesKilled) + " boss" + (pitPlayer.stats.bossesKilled == 1 ? "" : "es");
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