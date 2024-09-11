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

public class ReachKillstreakQuest extends PassQuest {
    public static ReachKillstreakQuest INSTANCE;
    public static final int KILLSTREAK_REQUIREMENT = 100;

    public ReachKillstreakQuest() {
        super("&e&l神级连胜者", "reachkillstreak", QuestType.WEEKLY);
        INSTANCE = this;
    }

    public void endStreak(PitPlayer pitPlayer, int streak) {
        if(streak >= KILLSTREAK_REQUIREMENT) progressQuest(pitPlayer, 1);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.BLAZE_POWDER)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7达到 " + KILLSTREAK_REQUIREMENT + " 连杀",
                        "&e" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &7次",
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
        questLevels.add(new QuestLevel(100, 100));
        questLevels.add(new QuestLevel(150, 150));
        questLevels.add(new QuestLevel(200, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}
