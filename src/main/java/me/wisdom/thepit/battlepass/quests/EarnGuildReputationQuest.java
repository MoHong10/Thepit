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

public class EarnGuildReputationQuest extends PassQuest {
    public static EarnGuildReputationQuest INSTANCE;

    public EarnGuildReputationQuest() {
        super("&a&l带领队伍", "earnguildrep", QuestType.WEEKLY);
        INSTANCE = this;
    }

    public void gainReputation(PitPlayer pitPlayer, int amount) {
        progressQuest(pitPlayer, amount);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.BANNER, 1, 15)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7为你的工会赢得 &a" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &7声望点",
                        "",
                        "&7进度: &3" + Formatter.formatLarge(progress) + "&7/&3" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &8[" +
                                AUtil.createProgressBar("|", ChatColor.AQUA, ChatColor.GRAY, 20,
                                        progress / questLevel.getRequirement(pitPlayer)) + "&8]",
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
        questLevels.add(new QuestLevel(30_000, 100));
        questLevels.add(new QuestLevel(45_000, 150));
        questLevels.add(new QuestLevel(60_000, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1;
    }
}
