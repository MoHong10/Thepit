package me.wisdom.thepit.battlepass.quests;

import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AttackBotsWithHealerQuest extends PassQuest {
    public static AttackBotsWithHealerQuest INSTANCE;

    public AttackBotsWithHealerQuest() {
        super("&c&l他们也有感情", "healbots", QuestType.WEEKLY);
        INSTANCE = this;
    }

    public void healPlayer(PitPlayer pitPlayer, double amount) {
        progressQuest(pitPlayer, amount);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.GOLDEN_CARROT)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&cHeal &7nons &c" + Misc.getHearts(questLevel.getRequirement(pitPlayer)) + " &7with the",
                        "&7enchant &9Healer",
                        "",
                        "&7进度: &3" + Formatter.formatLarge(progress / 2) + "&7/&3" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer) / 2) + " &8[" +
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
        questLevels.add(new QuestLevel(20_000, 150));
        questLevels.add(new QuestLevel(20_000, 200));
        questLevels.add(new QuestLevel(20_000, 250));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}
