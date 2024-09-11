package me.wisdom.thepit.settings.scoreboard;

import me.wisdom.thepit.controllers.HitCounter;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enchants.overworld.ReallyToxic;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ReallyToxicScoreboard extends ScoreboardOption {

    @Override
    public String getDisplayName() {
        return "&a真正毒性";
    }

    @Override
    public String getRefName() {
        return "reallytoxic";
    }

    @Override
    public String getValue(PitPlayer pitPlayer) {
        int attackerCharge = HitCounter.getCharge(pitPlayer.player, ReallyToxic.INSTANCE);
        if (attackerCharge == 0) return null;
        return "&6毒性: &a-" + Math.min(attackerCharge, ReallyToxic.getMaxReduction()) + "% 治疗";
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        ItemStack itemStack = new AItemStackBuilder(Material.INK_SACK, 1, 2)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7显示当前的治疗",
                        "&7减少来自 " + ReallyToxic.INSTANCE.getDisplayName(),
                        "&7当适用时"
                )).getItemStack();
        return itemStack;
    }
}