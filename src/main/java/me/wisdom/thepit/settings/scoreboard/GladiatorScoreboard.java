package me.wisdom.thepit.settings.scoreboard;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.perks.Gladiator;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GladiatorScoreboard extends ScoreboardOption {

    @Override
    public String getDisplayName() {
        return "&9角斗士";
    }

    @Override
    public String getRefName() {
        return "gladiator";
    }

    @Override
    public String getValue(PitPlayer pitPlayer) {
        if (!Gladiator.INSTANCE.hasPerk(pitPlayer.player)) return null;
        int reduction = Gladiator.getReduction(pitPlayer.player);
        if (reduction == 0) return null;
        return "&6角斗士: &9-" + reduction + "%";
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        ItemStack itemStack = new AItemStackBuilder(Material.BONE)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7显示当前的伤害",
                        "&7减少来自 " + "&a" + Gladiator.INSTANCE.displayName + " &7的",
                        "&7当适用时"
                )).getItemStack();
        return itemStack;
    }
}
