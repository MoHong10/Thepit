package me.wisdom.thepit.battlepass.quests;

import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.HealEvent;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

public class GainAbsorptionQuest extends PassQuest {
    public static GainAbsorptionQuest INSTANCE;

    public GainAbsorptionQuest() {
        super("&6&l汲取", "gainabsorption", QuestType.WEEKLY);
        INSTANCE = this;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHeal(HealEvent healEvent) {
        if(healEvent.pitEnchant == null) return;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(healEvent.player);
        progressQuest(pitPlayer, healEvent.getEffectiveHeal());
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.GOLDEN_APPLE)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7从附魔中获得 &6" + Misc.getHearts(questLevel.getRequirement(pitPlayer)) + " &7",
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
        questLevels.add(new QuestLevel(20_000, 100));
        questLevels.add(new QuestLevel(30_000, 150));
        questLevels.add(new QuestLevel(40_000, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}