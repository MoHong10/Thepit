package me.wisdom.thepit.battlepass.quests;

import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class EarnRenownQuest extends PassQuest {
    public static EarnRenownQuest INSTANCE;

    public EarnRenownQuest() {
        super("&e&l声望", "earnrenown", QuestType.WEEKLY);
        INSTANCE = this;
    }

    public void gainRenown(PitPlayer pitPlayer, int amount) {
        progressQuest(pitPlayer, amount);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        ItemStack itemStack = new AItemStackBuilder(Material.BEACON)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7获得 &f" + decimalFormat.format(questLevel.getRequirement(pitPlayer)) + " 声望",
                        "",
                        "&7进度: &3" + decimalFormat.format(progress) + "&7/&3" + decimalFormat.format(questLevel.getRequirement(pitPlayer)) + " &8[" +
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
        questLevels.add(new QuestLevel(5, 100));
        questLevels.add(new QuestLevel(7, 150));
        questLevels.add(new QuestLevel(10, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        if(pitPlayer.prestige == 0) return 1;
        return Math.pow(pitPlayer.prestige, 2.0 / 3.0);
    }
}
