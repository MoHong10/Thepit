package me.wisdom.thepit.placeholders;

import me.wisdom.thepit.controllers.ScoreboardManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.settings.scoreboard.ScoreboardOption;
import me.wisdom.thepitapi.hooks.PAPI.APAPIPlaceholder;
import org.bukkit.entity.Player;

public class CustomScoreboardPlaceholder implements APAPIPlaceholder {

    @Override
    public String getIdentifier() {
        return "custom_scoreboard";
    }

    @Override
    public String getValue(Player player) {
        if(!player.hasPermission("pitsim.scoreboard")) return null;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        for(String refName : pitPlayer.scoreboardData.getPriorityList()) {
            if(!pitPlayer.scoreboardData.getStatusMap().get(refName)) continue;
            ScoreboardOption scoreboardOption = ScoreboardManager.getScoreboardOption(refName);
            String value = scoreboardOption.getValue(pitPlayer);
            if(value == null) continue;
            return value;
        }
        return "&6自定义: &e无可用数据！";
    }
}
