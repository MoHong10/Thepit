package me.wisdom.thepit.controllers.objects;

import java.util.UUID;

public class LeaderboardPosition {
    public Leaderboard leaderboard;
    public UUID uuid;

    public double doubleValue;
    public int intValue;
    public long longValue;

    public LeaderboardPosition(Leaderboard leaderboard, UUID uuid) {
        this.leaderboard = leaderboard;
        this.uuid = uuid;
    }

    public boolean isMoreThanOrEqual(LeaderboardPosition toCompare) {
        return leaderboard.isMoreThanOrEqual(this, toCompare);
    }
}
