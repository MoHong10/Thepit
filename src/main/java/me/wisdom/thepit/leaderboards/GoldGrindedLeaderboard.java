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

public class GoldGrindedLeaderboard extends Leaderboard {
    public GoldGrindedLeaderboard() {
        super("total-gold", "&6Gold Grinded");
    }

    @Override
    public ItemStack getDisplayStack(UUID uuid) {
        ItemStack itemStack = new AItemStackBuilder(Material.GOLD_BLOCK)
                .setName("&6Gold Grinded")
                .setLore(new ALoreBuilder(
                        "&7Players who have &6grinded", "&7the most &6gold", ""
                ).addLore(getTopPlayers(uuid)).addLore(
                        "", "&eClick to pick!"
                ))
                .getItemStack();
        return itemStack;
    }

    @Override
    public String getDisplayValue(LeaderboardPosition position) {
        return "&6" + Formatter.formatLarge(position.doubleValue) + "g";
    }

    @Override
    public String getDisplayValue(PitPlayer pitPlayer) {
        return "&6" + Formatter.formatLarge(pitPlayer.stats.totalGold) + "g";
    }

    @Override
    public void setPosition(LeaderboardPosition position) {
        LeaderboardData data = LeaderboardData.getLeaderboardData(this);

        position.doubleValue = data.getValue(position.uuid).primaryValue;
    }

    @Override
    public boolean isMoreThanOrEqual(LeaderboardPosition position, LeaderboardPosition otherPosition) {
        return position.doubleValue >= otherPosition.doubleValue;
    }
}
