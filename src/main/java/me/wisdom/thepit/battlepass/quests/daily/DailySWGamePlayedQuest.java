package me.wisdom.thepit.battlepass.quests.daily;

import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.controllers.objects.PluginMessage;
import me.wisdom.thepit.events.MessageEvent;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class DailySWGamePlayedQuest extends PassQuest {

    public DailySWGamePlayedQuest() {
        super("&f&l玩 天空战争", "dailyskywars", QuestType.DAILY);
    }

    @EventHandler
    public void onMessage(MessageEvent event) {
        PluginMessage message = event.getMessage();
        List<String> strings = message.getStrings();
        List<Integer> ints = message.getIntegers();
        if(strings.isEmpty()) return;
        if(strings.get(0).equals("天空战争通行证任务")) {
            UUID playerUUID = UUID.fromString(strings.get(1));

            Player player = Bukkit.getPlayer(playerUUID);
            if(!player.isOnline()) return;

            PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
            progressQuest(pitPlayer, ints.get(0));
        }
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.EYE_OF_ENDER)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7至少击杀 &f1 &7名玩家",
                        "&7在 &f" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &7天空战争游戏",
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
        questLevels.add(new QuestLevel(2, 40));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}
