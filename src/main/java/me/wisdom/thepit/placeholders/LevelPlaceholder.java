package me.wisdom.thepit.placeholders;

import me.wisdom.thepit.controllers.PrestigeValues;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.hooks.PAPI.APAPIPlaceholder;
import org.bukkit.entity.Player;

public class LevelPlaceholder implements APAPIPlaceholder {

    @Override
    public String getIdentifier() {
        return "level";
    }

    @Override
    public String getValue(Player player) {

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        return PrestigeValues.getPlayerPrefix(player);
    }
}
