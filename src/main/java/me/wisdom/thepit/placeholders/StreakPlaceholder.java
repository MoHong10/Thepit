package me.wisdom.thepit.placeholders;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.hooks.PAPI.APAPIPlaceholder;
import org.bukkit.entity.Player;

public class StreakPlaceholder implements APAPIPlaceholder {

    @Override
    public String getIdentifier() {
        return "streak";
    }

    @Override
    public String getValue(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        int kills = pitPlayer.getKills();
        return String.valueOf(kills);
    }
}
