package me.wisdom.thepit.controllers.objects;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.PrestigeValues;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class Leaderboard {
    public List<LeaderboardPosition> orderedLeaderboard = new ArrayList<>();

    public int slot;
    //	public static List<Integer> slots = new ArrayList<>(Arrays.asList(10, 11, 13, 14, 15, 16, 20, 21, 22, 23, 24));
    public static List<Integer> slots = new ArrayList<>(Arrays.asList(10, 11, 12, 13, 14, 15, 16, 20, 21, 22, 23, 24));
    public String refName;
    public String displayName;

    public Leaderboard(String refName, String displayName) {
        this.slot = slots.remove(0);
        this.refName = refName;
        this.displayName = displayName;
    }

    public abstract ItemStack getDisplayStack(UUID uuid);

    public abstract String getDisplayValue(LeaderboardPosition position);

    public abstract String getDisplayValue(PitPlayer pitPlayer);

    public abstract void setPosition(LeaderboardPosition position);

    public abstract boolean isMoreThanOrEqual(LeaderboardPosition position, LeaderboardPosition otherPosition);

    public static String getRankColor(UUID uuid) {
        return Misc.getRankColor(uuid);
    }

    public List<String> getTopPlayers(UUID uuid) {
        ALoreBuilder aLoreBuilder = new ALoreBuilder();
        boolean isOnLeaderboard = false;
        for(int i = 0; i < 10; i++) {
            if(orderedLeaderboard.size() < i + 1) {
                aLoreBuilder.addLore("&e" + (i + 1) + ". &cERROR");
                continue;
            }
            LeaderboardPosition position = orderedLeaderboard.get(i);
            LeaderboardData data = LeaderboardData.getLeaderboardData(this);
            if(position.uuid.equals(uuid)) isOnLeaderboard = true;
            LeaderboardData.PlayerData playerData = data.getValue(position.uuid);
            String rankColor = getRankColor(position.uuid);
            if(data.getPrefix(position.uuid) == null) {
                aLoreBuilder.addLore("&e" + (i + 1) + ". &cERROR");
                continue;
            }
            aLoreBuilder.addLore("&e" + (i + 1) + ". " + data.getPrefix(position.uuid) + " " + rankColor +
                    playerData.username + "&7 - " + getDisplayValue(position));
        }

        LeaderboardPlayerData data = LeaderboardPlayerData.getData(uuid);
        Player player = Bukkit.getPlayer(uuid);
        if(player.isOnline()) {
            PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

            if(isOnLeaderboard) {
            } else if(data == null) {
                aLoreBuilder.addLore("", "&7You are not on this leaderboard");
            } else {
                Thepit.LUCKPERMS.getUserManager().loadUser(uuid);
                String rankColor = getRankColor(uuid);
                aLoreBuilder.addLore("&7...", "&e" + data.getData(this) + ". " + getPrestigeBrackets(pitPlayer) + " " +
                        rankColor + player.getName() + "&7 - " + getDisplayValue(pitPlayer));
            }
        }

        return aLoreBuilder.getLore();
    }

    private static String getPrestigeBrackets(PitPlayer pitPlayer) {
        return PrestigeValues.getPlayerPrefix(pitPlayer.prestige, pitPlayer.level, pitPlayer.overflowXP);
    }

    public void calculate(UUID uuid) {
        remove(uuid);

        LeaderboardPosition leaderboardPosition = new LeaderboardPosition(this, uuid);
        setPosition(leaderboardPosition);
        for(int i = 0; i < orderedLeaderboard.size(); i++) {
            LeaderboardPosition testPosition = orderedLeaderboard.get(i);
            if(testPosition.isMoreThanOrEqual(leaderboardPosition)) continue;
            orderedLeaderboard.add(i, leaderboardPosition);
            return;
        }
        orderedLeaderboard.add(leaderboardPosition);
    }

    public void remove(UUID uuid) {
        for(LeaderboardPosition leaderboardPosition : orderedLeaderboard) {
            if(!leaderboardPosition.uuid.equals(uuid)) continue;
            orderedLeaderboard.remove(leaderboardPosition);
            break;
        }
    }
}
