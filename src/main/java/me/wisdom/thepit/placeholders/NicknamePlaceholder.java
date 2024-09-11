package me.wisdom.thepit.placeholders;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.hooks.PAPI.APAPIPlaceholder;
import org.bukkit.entity.Player;

public class NicknamePlaceholder implements APAPIPlaceholder {

    @Override
    public String getIdentifier() {
        return "nickname";
    }

    @Override
    public String getValue(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(pitPlayer.nickname == null || !player.hasPermission("pitsim.nick")) return player.getName();
        return pitPlayer.nickname;
    }
}
