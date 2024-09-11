package me.wisdom.thepit.placeholders;

import me.wisdom.thepit.controllers.CombatManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.hooks.PAPI.APAPIPlaceholder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CombatTimerPlaceholder implements APAPIPlaceholder {

    @Override
    public String getIdentifier() {
        return "combat";
    }

    @Override
    public String getValue(Player player) {

        Integer time = CombatManager.taggedPlayers.get(player.getUniqueId());
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        if(pitPlayer.isOnMega()) return pitPlayer.getMegastreak().getDisplayName();

        if(time == null) return ChatColor.GREEN + "待机中";
        else if(time / 20 > 9) {
            return ChatColor.RED + "战斗中";
        } else {
            return ChatColor.RED + "战斗中 " + ChatColor.GRAY + "(" + (int) Math.ceil(time / 20D) + "s)";
        }
    }
}