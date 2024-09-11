package me.wisdom.thepit.placeholders;

import me.wisdom.thepit.controllers.ProxyMessaging;
import me.wisdom.thepitapi.hooks.PAPI.APAPIPlaceholder;
import org.bukkit.entity.Player;

public class PlayerCountPlaceholder implements APAPIPlaceholder {

    @Override
    public String getIdentifier() {
        return "players";
    }

    @Override
    public String getValue(Player player) {
        return Integer.toString(ProxyMessaging.playersOnline);
    }
}
