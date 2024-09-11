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

public class PlayerKillsLeaderboard extends Leaderboard {
    public PlayerKillsLeaderboard() {
        super("player-kills", "&cPlayer Kills");
    }

    @Override
    public ItemStack getDisplayStack(UUID uuid) {
        ItemStack itemStack = new AItemStackBuilder(Material.DIAMOND_SWORD)
                .setName("&cPlayer Kills")
                .setLore(new ALoreBuilder(
                        "&7Players who have &ckilled &7the", "&7most &cplayers", ""
                ).addLore(getTopPlayers(uuid)).addLore(
                        "", "&eClick to pick!"
                ))
                .getItemStack();
        return itemStack;
    }

    @Override
    public String getDisplayValue(LeaderboardPosition position) {
        return "&c" + Formatter.formatLarge(position.intValue) + " kills";
    }

    @Override
    public String getDisplayValue(PitPlayer pitPlayer) {
        return "&c" + Formatter.formatLarge(pitPlayer.stats.playerKills) + " kills";
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
