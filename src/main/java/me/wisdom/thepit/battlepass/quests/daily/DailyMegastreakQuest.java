package me.wisdom.thepit.battlepass.quests.daily;

import me.wisdom.thepit.battlepass.PassData;
import me.wisdom.thepit.battlepass.PassManager;
import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DailyMegastreakQuest extends PassQuest {
    public static DailyMegastreakQuest INSTANCE;

    public DailyMegastreakQuest() {
        super("&e&l完成一次超级连胜", "dailymegastreak", QuestType.DAILY);
        INSTANCE = this;
    }

    public void onMegastreakComplete(PitPlayer pitPlayer) {
        PassData passData = pitPlayer.getPassData(PassManager.currentPass.startDate);
        if(!passData.hasPremium) return;
        progressQuest(pitPlayer, 1);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.WHEAT)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7完成 &e" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &7超级连胜" +
                                (questLevel.getRequirement(pitPlayer) == 1 ? "" : "s"),
                        "&7(需要高级通行证)",
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
    public void createPossibleStates() {
        questLevels.add(new QuestLevel(1, 20));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}
