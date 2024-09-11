package me.wisdom.thepit.battlepass.quests;

import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.HopperManager;
import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepit.controllers.objects.Hopper;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

public class JudgementHopperQuest extends PassQuest {

    public JudgementHopperQuest() {
        super("&e&lPit is Fine", "judgementhopper", QuestType.WEEKLY);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKill(KillEvent killEvent) {
        Hopper hopper = HopperManager.getHopper(killEvent.getKillerPlayer());
        if(hopper == null || !PlayerManager.isRealPlayer(killEvent.getDeadPlayer())) return;

        Player player = hopper.judgementPlayer;
        if(player == null) return;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        progressQuest(pitPlayer, 1);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.HOPPER)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7使用&9审判头盔技能 &e" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &7次时产生的跳跃杀死一名玩家",
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
        questLevels.add(new QuestLevel(1, 100));
        questLevels.add(new QuestLevel(2, 150));
        questLevels.add(new QuestLevel(3, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}
