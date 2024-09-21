package me.wisdom.thepit.controllers.objects;

import me.wisdom.thepit.controllers.PrestigeValues;

import java.util.*;

public class LeaderboardData {

    public static Map<Leaderboard, LeaderboardData> leaderboards = new HashMap<>();

    private final Leaderboard leaderboard;
    private final String leaderboardData;
    private final Map<UUID, PlayerData> leaderboardDataMap;

    public LeaderboardData(Leaderboard leaderboard, String leaderboardData) {
        this.leaderboard = leaderboard;
        this.leaderboardData = leaderboardData;

        this.leaderboardDataMap = new LinkedHashMap<>();
        String[] playerSplit = leaderboardData.split("\\|");
        if(playerSplit.length > 1) {
            for(String data : playerSplit) {
                String[] playerData = data.split(",");
                UUID uuid = UUID.fromString(playerData[0]);
                double value = Double.parseDouble(playerData[2]);
                String username = playerData[3];

                String[] progressionInfo = playerData[1].split(" ");
                PlayerData dataObject = new PlayerData(username, value, Integer.parseInt(progressionInfo[0]),
                        Integer.parseInt(progressionInfo[1]), Double.parseDouble(progressionInfo[2]));

                leaderboardDataMap.put(uuid, dataObject);
            }
        }

        leaderboards.put(leaderboard, this);
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public String getLeaderboardData() {
        return leaderboardData;
    }

    public Map<UUID, PlayerData> getLeaderboardDataMap() {
        return leaderboardDataMap;
    }

    public String getPrefix(UUID uuid) {
        if(!leaderboardDataMap.containsKey(uuid)) return null;
        return leaderboardDataMap.get(uuid).prefix;
    }

    public PlayerData getValue(UUID uuid) {
        return leaderboardDataMap.get(uuid);
    }

    public static LeaderboardData getLeaderboardData(Leaderboard leaderboard) {
        return leaderboards.get(leaderboard);
    }

    public static class PlayerData {

        public double primaryValue;
        public String username;
        public String prefix;

        public int level;
        public int prestige;
        public double overflow;


        public PlayerData(String username, double primaryValue, int prestige, int level, double overflow) {
            this.username = username;
            this.primaryValue = primaryValue;
            this.prestige = prestige;
            this.level = level;
            this.overflow = overflow;
            this.prefix = PrestigeValues.getPlayerPrefix(prestige, level, overflow);
        }
    }

}
