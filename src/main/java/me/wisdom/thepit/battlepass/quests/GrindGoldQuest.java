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

public class GrindGoldQuest extends PassQuest {
    public static GrindGoldQuest INSTANCE;

    public GrindGoldQuest() {
        super("&6&l富有", "grindgold", QuestType.WEEKLY);
        INSTANCE = this;
    }

    public void gainGold(PitPlayer pitPlayer, long gold) {
        progressQuest(pitPlayer, (double) gold);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.GOLD_INGOT)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7赚取 &6" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + "金币",
                        "",
                        "&进度: &3" + Formatter.formatLarge(progress) + "&7/&3" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &8[" +
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
        questLevels.add(new QuestLevel(1.0, 100));
        questLevels.add(new QuestLevel(1.5, 150));
        questLevels.add(new QuestLevel(2.0, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return Math.pow(PrestigeValues.getPrestigeInfo(pitPlayer.prestige).getGoldReq() / 1000.0, 9.0 / 10.0) * 1000;
    }
}
