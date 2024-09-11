package me.wisdom.thepit.placeholders;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.hooks.PAPI.APAPIPlaceholder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;

public class SoulsPlaceholder implements APAPIPlaceholder {

    @Override
    public String getIdentifier() {
        return "souls";
    }

    @Override
    public String getValue(Player player) {

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        return ChatColor.WHITE + (NumberFormat.getNumberInstance(Locale.US).format(pitPlayer.taintedSouls));
    }
}
