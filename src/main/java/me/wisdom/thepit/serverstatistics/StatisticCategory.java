package me.wisdom.thepit.serverstatistics;

public enum StatisticCategory {
    OVERWORLD_PVP("主世界 PvP"),
    OVERWORLD_STREAKING("主世界连胜"),
    DARKZONE_VS_PLAYER("黑暗地带玩家对玩家"),
    DARKZONE_VS_MOB("黑暗地带玩家对怪物"),
    DARKZONE_VS_BOSS("黑暗地带玩家对boss"),
    ;

    private final String displayName;

    StatisticCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
