package me.wisdom.thepit.placeholders;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.hooks.PAPI.APAPIPlaceholder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.entity.Player;

public class PrestigePlaceholder implements APAPIPlaceholder {

    @Override
    public String getIdentifier() {
        return "prestige";
    }

    @Override
    public String getValue(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        return pitPlayer.prestige == 0 ? "0" : AUtil.toRoman(pitPlayer.prestige);
    }
}
