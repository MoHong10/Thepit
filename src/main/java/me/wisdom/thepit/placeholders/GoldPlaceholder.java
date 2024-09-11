package me.wisdom.thepit.placeholders;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.hooks.PAPI.APAPIPlaceholder;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class GoldPlaceholder implements APAPIPlaceholder {

    DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    public String getIdentifier() {
        return "gold";
    }

    @Override
    public String getValue(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        return formatter.format(pitPlayer.gold) + "g";
    }
}
