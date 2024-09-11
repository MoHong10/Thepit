package me.wisdom.thepit.settings.scoreboard;

import me.wisdom.thepit.auction.AuctionManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AuctionScoreboard extends ScoreboardOption {

    @Override
    public String getDisplayName() {
        return "&f黑暗拍卖";
    }

    @Override
    public String getRefName() {
        return "darkauctions";
    }

    @Override
    public String getValue(PitPlayer pitPlayer) {
        if (AuctionManager.haveAuctionsEnded()) return "&6拍卖: &e已结束";
        return "&6拍卖: &e" + AuctionManager.getRemainingTime();
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        ItemStack itemStack = new AItemStackBuilder(Material.GHAST_TEAR)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7显示黑暗区拍卖的剩余时间",
                        "&7"
                )).getItemStack();
        return itemStack;
    }
}
