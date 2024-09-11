package me.wisdom.thepit.battlepass.quests.dzkillmobs;

import me.wisdom.thepit.battlepass.PassManager;
import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.darkzone.DarkzoneManager;
import me.wisdom.thepit.darkzone.mobs.PitZombie;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class KillZombiesQuest extends PassQuest {

    public KillZombiesQuest() {
        super("&c&l僵尸杀手", "killzombies", QuestType.WEEKLY);
        weight = PassManager.DARKZONE_KILL_QUEST_WEIGHT;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!PlayerManager.isRealPlayer(killEvent.getKillerPlayer())) return;
        if(!(DarkzoneManager.getPitMob(killEvent.getDead()) instanceof PitZombie)) return;

        progressQuest(killEvent.getKillerPitPlayer(), 1);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.ROTTEN_FLESH)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7击杀 &c" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &7僵尸",
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
        questLevels.add(new QuestLevel(1000, 100));
        questLevels.add(new QuestLevel(1500, 150));
        questLevels.add(new QuestLevel(2000, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}