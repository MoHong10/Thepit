package me.wisdom.thepit.battlepass.quests;

import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BrewPotionsQuest extends PassQuest {
    public static BrewPotionsQuest INSTANCE;

    public BrewPotionsQuest() {
        super("&d&l从铅到金", "brewpotions", QuestType.WEEKLY);
        INSTANCE = this;
    }

    public void brewPotion(PitPlayer pitPlayer) {
        progressQuest(pitPlayer, 1);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.BREWING_STAND_ITEM)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7获得 &d" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &7点",
                        "",
                        "&7进度: &3" + Formatter.formatLarge(progress) + "&7/&3" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &8[" +
                                AUtil.createProgressBar("|", ChatColor.AQUA, ChatColor.GRAY, 20, progress / questLevel.getRequirement(pitPlayer)) + "&8]",
                        "&7奖励: &3" + questLevel.rewardPoints + " &7任务点"
                ))
                .getItemStack();
        return itemStack;
    }

    @Override
    public QuestLevel getDailyState() {
        return null;
    }

    @Override
    public void createPossibleStates() {
        questLevels.add(new QuestLevel(20, 100));
        questLevels.add(new QuestLevel(30, 150));
        questLevels.add(new QuestLevel(40, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}
