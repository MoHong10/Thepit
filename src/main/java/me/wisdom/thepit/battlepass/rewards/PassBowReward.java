package me.wisdom.thepit.battlepass.rewards;

import me.wisdom.thepit.battlepass.PassReward;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.MysticType;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PassBowReward extends PassReward {
    public int count;

    public PassBowReward(int count) {
        this.count = count;
    }

    @Override
    public boolean giveReward(PitPlayer pitPlayer) {
        if(Misc.getEmptyInventorySlots(pitPlayer.player) < count) {
            AOutput.error(pitPlayer.player, "&7请在你的背包留出空余格子");
            return false;
        }

        for(int i = 0; i < count; i++) {
            ItemStack jewelBow = MysticFactory.getJewelItem(MysticType.BOW);
            AUtil.giveItemSafely(pitPlayer.player, jewelBow);
        }
        return true;
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, boolean hasClaimed) {
        ItemStack itemStack = new AItemStackBuilder(Material.BOW, count)
                .setName("&b&l弓 奖励")
                .setLore(new ALoreBuilder(
                        "&7奖励: &b" + count + "x 隐藏宝石弓"
                )).getItemStack();
        return itemStack;
    }
}
