package me.wisdom.thepit.battlepass.rewards;

import me.wisdom.thepit.battlepass.PassReward;
import me.wisdom.thepit.controllers.BoosterManager;
import me.wisdom.thepit.controllers.objects.Booster;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PassBoosterReward extends PassReward {
    public Booster boosterType;

    public PassBoosterReward(String boosterName) {
        this.boosterType = BoosterManager.getBooster(boosterName);
    }

    @Override
    public boolean giveReward(PitPlayer pitPlayer) {

        Booster.setBooster(pitPlayer.player, boosterType, pitPlayer.boosters.get(boosterType.refName) + 1);
        AOutput.send(pitPlayer.player, "&6&l获得!&7 收到 &f1 " + boosterType.color + boosterType.name + "&7.");

        return true;
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, boolean hasClaimed) {
        ItemStack itemStack = new AItemStackBuilder(Material.NETHER_STAR)
                .setName("&a&l奖励")
                .setLore(new ALoreBuilder(
                        "&7奖励: " + boosterType.color + boosterType.name
                )).getItemStack();
        return itemStack;
    }
}