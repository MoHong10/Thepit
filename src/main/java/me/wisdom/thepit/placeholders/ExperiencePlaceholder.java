package me.wisdom.thepit.placeholders;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.hooks.PAPI.APAPIPlaceholder;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;

public class ExperiencePlaceholder implements APAPIPlaceholder {

    @Override
    public String getIdentifier() {
        return "experience";
    }

    @Override
    public String getValue(Player player) {

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(pitPlayer.remainingXP == 0) return "MAXED!";
        else return (NumberFormat.getNumberInstance(Locale.US).format(pitPlayer.remainingXP));
    }
}