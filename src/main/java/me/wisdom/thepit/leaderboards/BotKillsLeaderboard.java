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

public class BotKillsLeaderboard extends Leaderboard {
    public BotKillsLeaderboard() {
        super("bot-kills", "&cBot Kills");
    }

    @Override
    public ItemStack getDisplayStack(UUID uuid) {
        ItemStack itemStack = new AItemStackBuilder(Material.IRON_SWORD)
                .setName("&cBot Kills")
                .setLore(new ALoreBuilder(
                        "&7Players who have &ckilled &7the", "&7most &cbots", ""
                ).addLore(getTopPlayers(uuid)).addLore(
                        "", "&eClick to pick!"
                ))
                .getItemStack();
        return itemStack;
    }

    @Override
    public String getDisplayValue(LeaderboardPosition position) {
        return "&c" + Formatter.formatLarge(position.intValue) + " kill" + (position.intValue == 1 ? "" : "s");
    }

    @Override
    public String getDisplayValue(PitPlayer pitPlayer) {
        return "&c" + Formatter.formatLarge(pitPlayer.stats.botKills) + " kill" + (pitPlayer.stats.botKills == 1 ? "" : "s");
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
