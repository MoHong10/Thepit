package me.wisdom.thepit.battlepass.rewards;

import me.wisdom.thepit.battlepass.PassReward;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.MysticType;
import me.wisdom.thepit.enums.PantColor;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.inventory.ItemStack;

public class PassPantsReward extends PassReward {
    public int count;

    public PassPantsReward(int count) {
        this.count = count;
    }

    @Override
    public boolean giveReward(PitPlayer pitPlayer) {
        if(Misc.getEmptyInventorySlots(pitPlayer.player) < count) {
            AOutput.error(pitPlayer.player, "&7请在你的背包留出空余格子");
            return false;
        }

        for(int i = 0; i < count; i++) {
            ItemStack jewel = MysticFactory.getJewelItem(MysticType.PANTS);
            AUtil.giveItemSafely(pitPlayer.player, jewel);
        }
        return true;
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, boolean hasClaimed) {
        ItemStack itemStack = MysticFactory.getFreshItem(MysticType.PANTS, PantColor.JEWEL);
        new AItemStackBuilder(itemStack)
                .setName("&3&l裤子 奖励")
                .setLore(new ALoreBuilder(
                        "&7奖励: &3" + count + "x 隐藏宝石裤子"
                ));
        itemStack.setAmount(count);
        return itemStack;
    }
}
