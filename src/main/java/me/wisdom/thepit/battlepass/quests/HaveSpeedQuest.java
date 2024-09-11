package me.wisdom.thepit.battlepass.quests;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.SpawnManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class HaveSpeedQuest extends PassQuest {
    public static HaveSpeedQuest INSTANCE;

    public HaveSpeedQuest() {
        super("&f&lZooooom", "havespeed", QuestType.WEEKLY);
        INSTANCE = this;
    }

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!HaveSpeedQuest.INSTANCE.isQuestActive()) return;
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if(!onlinePlayer.hasPotionEffect(PotionEffectType.SPEED) ||
                            SpawnManager.isInSpawn(onlinePlayer) || MapManager.inDarkzone(onlinePlayer))
                        continue;
                    PitPlayer pitPlayer = PitPlayer.getPitPlayer(onlinePlayer);
                    HaveSpeedQuest.INSTANCE.progressQuest(pitPlayer, 1);
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0, 20);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.SUGAR)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7保持疾跑 &f" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer) / 60) + " &7分钟",
                        "&7(不包括在出生点和黑暗区)",
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
        questLevels.add(new QuestLevel(60 * 60, 100));
        questLevels.add(new QuestLevel(60 * 90, 100));
        questLevels.add(new QuestLevel(60 * 120, 100));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}