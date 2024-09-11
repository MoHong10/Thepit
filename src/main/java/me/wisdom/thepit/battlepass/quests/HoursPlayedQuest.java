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

public class HoursPlayedQuest extends PassQuest {
    public static HoursPlayedQuest INSTANCE;

    public HoursPlayedQuest() {
        super("&e&l拖延症", "hoursplayed", QuestType.WEEKLY);
        INSTANCE = this;
    }

    public void progressTime(PitPlayer pitPlayer) {
        progressQuest(pitPlayer, 1);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.WATCH)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7在服务器上游玩 &e" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer) / 60) + " &7小时",
                        "",
                        "&7进度: &3" + Formatter.formatLarge(progress / 60) + "&7/&3" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer) / 60) + " &8[" +
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
        questLevels.add(new QuestLevel(8 * 60, 150));
        questLevels.add(new QuestLevel(12 * 60, 200));
        questLevels.add(new QuestLevel(16 * 60, 250));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}
