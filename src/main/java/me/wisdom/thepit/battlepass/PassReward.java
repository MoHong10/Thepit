package me.wisdom.thepit.battlepass;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import org.bukkit.inventory.ItemStack;

public abstract class PassReward {

    public abstract boolean giveReward(PitPlayer pitPlayer);

    public abstract ItemStack getDisplayStack(PitPlayer pitPlayer, boolean hasClaimed);

    public double getMultiplier(PitPlayer pitPlayer) {
        return 1;
    }
}
