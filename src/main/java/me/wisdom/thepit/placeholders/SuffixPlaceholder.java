package me.wisdom.thepit.placeholders;

import me.wisdom.pitguilds.Guild;
import me.wisdom.pitguilds.GuildManager;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.AFKManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.hooks.PAPI.APAPIPlaceholder;
import org.bukkit.entity.Player;

public class SuffixPlaceholder implements APAPIPlaceholder {

    @Override
    public String getIdentifier() {
        return "suffix";
    }

    @Override
    public String getValue(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(AFKManager.AFKPlayers.contains(player)) return "&8 [AFK]";

        if(Thepit.status.isOverworld()) {
            if(pitPlayer.bounty != 0) return "&7 &6&l" + pitPlayer.bounty + "g";
        } else {
            int souls = (int) Math.ceil(KillEvent.getBaseSouls(pitPlayer));
            if(souls != 0) return "&7 &f&l" + Formatter.formatSouls(souls, false);
        }

        Guild guild = GuildManager.getGuild(player);
        if(guild != null && guild.getTag() != null) return guild.getColor() + " #" + guild.getTag();
        return "";
    }
}
