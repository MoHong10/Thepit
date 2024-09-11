package me.wisdom.thepit.battlepass.quests;

import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepit.upgrades.Helmetry;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UseHelmetGoldQuest extends PassQuest {
    public static UseHelmetGoldQuest INSTANCE;

    public UseHelmetGoldQuest() {
        super("&6&l多余的支出", "usehelmetgold", QuestType.WEEKLY);
        INSTANCE = this;
    }

    public void spendGold(PitPlayer pitPlayer, int gold) {
        progressQuest(pitPlayer, gold);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.GOLD_NUGGET)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7在头盔技能上花费 &6" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &7金币",
                        "&7需要 (&6黄金头盔&7)",
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
        questLevels.add(new QuestLevel(50_000, 100));
        questLevels.add(new QuestLevel(75_000, 150));
        questLevels.add(new QuestLevel(100_000, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        int effectivePrestige = Math.max(pitPlayer.prestige, Helmetry.INSTANCE.prestigeReq);
        return effectivePrestige;
    }
}