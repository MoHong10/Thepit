package me.wisdom.thepit.settings.scoreboard;

import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.perks.StrengthChaining;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class StrengthScoreboard extends ScoreboardOption {

    @Override
    public String getDisplayName() {
        return "&cStrength-Chaining";
    }

    @Override
    public String getRefName() {
        return "strengthchaining";
    }

    @Override
    public String getValue(PitPlayer pitPlayer) {
        if(!StrengthChaining.INSTANCE.hasPerk(pitPlayer.player)) return null;
        int amplifier = StrengthChaining.amplifierMap.getOrDefault(pitPlayer.player.getUniqueId(), 0);
        if(amplifier == 0) return null;
        int duration = StrengthChaining.durationMap.getOrDefault(pitPlayer.player.getUniqueId(), 0);
        int seconds = (int) Math.ceil(duration / 20.0);
        return "&6Strength: &c" + AUtil.toRoman(amplifier) + " &7(" + seconds + ")";
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        ItemStack itemStack = new AItemStackBuilder(Material.REDSTONE)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7Shows the current level and",
                        "&7duration of strength from",
                        "&a" + StrengthChaining.INSTANCE.displayName + " &7when applicable"
                )).getItemStack();
        return itemStack;
    }
}
