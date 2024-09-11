package me.wisdom.thepit.battlepass.rewards;

import me.wisdom.thepit.battlepass.PassReward;
import me.wisdom.thepit.controllers.LevelManager;
import me.wisdom.thepit.controllers.PrestigeValues;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PassXPReward extends PassReward {
    public long xp;

    public PassXPReward(long xp) {
        this.xp = xp;
    }

    @Override
    public boolean giveReward(PitPlayer pitPlayer) {
        LevelManager.addXP(pitPlayer.player, (long) (xp * getMultiplier(pitPlayer)));
        return true;
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        PrestigeValues.PrestigeInfo prestigeInfo = PrestigeValues.getPrestigeInfo(pitPlayer.prestige);
        return Math.pow(prestigeInfo.getXpMultiplier(), 3.0 / 4.0);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, boolean hasClaimed) {
        ItemStack itemStack = new AItemStackBuilder(Material.INK_SACK, 1, 12)
                .setName("&b&lXP 奖励")
                .setLore(new ALoreBuilder(
                        "&7奖励: &b+" + Formatter.formatLarge((long) (xp * getMultiplier(pitPlayer))) + " XP"
                )).getItemStack();
        return itemStack;
    }
}
