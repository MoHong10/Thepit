package me.wisdom.thepit.battlepass.rewards;

import me.wisdom.thepit.battlepass.PassReward;
import me.wisdom.thepit.controllers.LevelManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PassGoldReward extends PassReward {
    public int gold;

    public PassGoldReward(int gold) {
        this.gold = gold;
    }

    @Override
    public boolean giveReward(PitPlayer pitPlayer) {
        LevelManager.addGold(pitPlayer.player, (int) (gold * getMultiplier(pitPlayer)));
        return true;
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        if(pitPlayer.prestige == 0) return 1;
        return pitPlayer.prestige;
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, boolean hasClaimed) {
        ItemStack itemStack = new AItemStackBuilder(Material.GOLD_INGOT)
                .setName("&6&l金币 奖励")
                .setLore(new ALoreBuilder(
                        "&7奖励: &6" + Formatter.formatLarge((int) (gold * getMultiplier(pitPlayer))) + "金币"
                )).getItemStack();
        return itemStack;
    }
}
