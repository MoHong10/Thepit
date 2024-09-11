package me.wisdom.thepit.placeholders;

import me.wisdom.thepit.perks.StrengthChaining;
import me.wisdom.thepitapi.hooks.PAPI.APAPIPlaceholder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.entity.Player;

public class StrengthChainingPlaceholder implements APAPIPlaceholder {

    @Override
    public String getIdentifier() {
        return "strength_level";
    }

    @Override
    public String getValue(Player player) {

        Integer level = StrengthChaining.amplifierMap.getOrDefault(player.getUniqueId(), 0);
        Integer time = StrengthChaining.durationMap.getOrDefault(player.getUniqueId(), 0);
        if(level == null || level == 0) return "None";

        return "&c" + AUtil.toRoman(level) + " &7(" + getTime(time) + ")";
    }

    public int getTime(int time) {

        return (int) Math.ceil(time / 20D);
    }
}
