package me.wisdom.thepit.battlepass.rewards;

import me.wisdom.thepit.battlepass.PassReward;
import me.wisdom.thepit.brewing.objects.BrewingIngredient;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.inventory.ItemStack;

public class PassDarkzoneDropReward extends PassReward {
    public BrewingIngredient ingredient;
    public int count;

    public PassDarkzoneDropReward(int tier, int count) {
        this.ingredient = BrewingIngredient.getIngredientFromTier(tier);
        this.count = count;
    }

    @Override
    public boolean giveReward(PitPlayer pitPlayer) {
        if(Misc.getEmptyInventorySlots(pitPlayer.player) < 1) {
            AOutput.error(pitPlayer.player, "&7请在你的背包留出空余格子");
            return false;
        }

        ItemStack itemStack = ingredient.getItem();
        itemStack.setAmount(count);
        AUtil.giveItemSafely(pitPlayer.player, itemStack);
        return true;
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, boolean hasClaimed) {
        ItemStack itemStack = ingredient.getItem();
        itemStack.setAmount(count);
        return itemStack;
    }
}
