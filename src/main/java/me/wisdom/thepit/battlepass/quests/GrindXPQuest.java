package me.wisdom.thepit.battlepass.quests;

import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.PrestigeValues;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GrindXPQuest extends PassQuest {
    public static GrindXPQuest INSTANCE;

    public GrindXPQuest() {
        super("&b&l专业", "grindxp", QuestType.WEEKLY);
        INSTANCE = this;
    }

    public void gainXP(PitPlayer pitPlayer, long xp) {
        progressQuest(pitPlayer, (double) xp);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.INK_SACK, 1, 12)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7获得 &b" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " XP",
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
        questLevels.add(new QuestLevel(25_000.0, 100));
        questLevels.add(new QuestLevel(37_500.0, 150));
        questLevels.add(new QuestLevel(50_000.0, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return Math.pow(PrestigeValues.getPrestigeInfo(pitPlayer.prestige).getXpMultiplier(), 9.0 / 10.0);
    }
}
