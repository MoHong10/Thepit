package me.wisdom.thepit.placeholders;

import me.wisdom.thepit.controllers.PrestigeValues;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.hooks.PAPI.APAPIPlaceholder;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;

public class GoldReqPlaceholder implements APAPIPlaceholder {

    @Override
    public String getIdentifier() {
        return "goldreq";
    }

    @Override
    public String getValue(Player player) {

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        PrestigeValues.PrestigeInfo prestigeInfo = PrestigeValues.getPrestigeInfo(pitPlayer.prestige);
        double goldReq = prestigeInfo.getGoldReq();

        if(goldReq - pitPlayer.goldGrinded <= 0) return "&aDONE!";
        else return (NumberFormat.getNumberInstance(Locale.US).format(goldReq - pitPlayer.goldGrinded)) + "g";
    }
}
