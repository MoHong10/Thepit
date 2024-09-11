package me.wisdom.thepit.battlepass.rewards;

import me.wisdom.thepit.battlepass.PassReward;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.items.misc.FunkyFeather;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PassFeatherReward extends PassReward {
    public int count;

    public PassFeatherReward(int count) {
        this.count = count;
    }

    @Override
    public boolean giveReward(PitPlayer pitPlayer) {
        if(Misc.getEmptyInventorySlots(pitPlayer.player) < 1) {
            AOutput.error(pitPlayer.player, "&7请在你的背包留出空余格子");
            return false;
        }

        ItemFactory.getItem(FunkyFeather.class).giveItem(pitPlayer.player, count);
        return true;
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, boolean hasClaimed) {
        ItemStack itemStack = new AItemStackBuilder(Material.FEATHER, count)
                .setName("&3&l羽毛 奖励")
                .setLore(new ALoreBuilder(
                        "&7奖励: &3" + count + "x 奇特羽毛"
                )).getItemStack();
        return itemStack;
    }
}
