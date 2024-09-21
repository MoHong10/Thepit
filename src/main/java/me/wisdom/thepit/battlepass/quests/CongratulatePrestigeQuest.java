package me.wisdom.thepit.battlepass.quests;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CongratulatePrestigeQuest extends PassQuest {
    public static CongratulatePrestigeQuest INSTANCE;

    public static long prestigedTick;
    public static List<UUID> alreadyCompleted = new ArrayList<>();

    public CongratulatePrestigeQuest() {
        super("&e&l不错的游戏", "congratulateprestige", QuestType.WEEKLY);
        INSTANCE = this;
    }

    public static void updateRecentlyPrestiged() {
        prestigedTick = Thepit.currentTick;
        alreadyCompleted.clear();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(prestigedTick + 20 * 10 > Thepit.currentTick) return;
        Player player = event.getPlayer();

        String[] words = ChatColor.stripColor(event.getMessage()).toLowerCase().replaceAll("[^A-Za-z0-9]", "").split(" ");
        if(words.length == 0 || words[0].length() < 2) return;
        for(char character : words[0].toCharArray()) {
            if(character != 'g') return;
        }

        if(alreadyCompleted.contains(player.getUniqueId())) return;
        alreadyCompleted.add(player.getUniqueId());

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        progressQuest(pitPlayer, 1);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.FIREWORK)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7说 &egg!&7 当 &e" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &7玩家",
                        "&7获得声望的时候",
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
        questLevels.add(new QuestLevel(8, 100));
        questLevels.add(new QuestLevel(12, 150));
        questLevels.add(new QuestLevel(16, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}
