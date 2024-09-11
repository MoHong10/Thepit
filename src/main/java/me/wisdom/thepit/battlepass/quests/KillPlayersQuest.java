package me.wisdom.thepit.battlepass.quests;

import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.controllers.objects.PlayerToPlayerCooldown;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

public class KillPlayersQuest extends PassQuest {
    public PlayerToPlayerCooldown cooldown = new PlayerToPlayerCooldown(20 * 60 * 2);

    public KillPlayersQuest() {
        super("&c&l1v1 神", "killplayers", QuestType.WEEKLY);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKill(KillEvent killEvent) {
        if(!killEvent.isKillerPlayer() || !killEvent.isDeadPlayer()) return;
        if(!PlayerManager.isRealPlayer(killEvent.getKillerPlayer()) || !canProgressQuest(killEvent.getKillerPitPlayer())
                || !PlayerManager.isRealPlayer(killEvent.getDeadPlayer())) return;

        if(cooldown.isOnCooldown(killEvent.getKillerPlayer(), killEvent.getDeadPlayer())) return;

        progressQuest(killEvent.getKillerPitPlayer(), 1);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.DIAMOND_SWORD)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7击杀 &c" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &7玩家 (没有机器人)",
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
        questLevels.add(new QuestLevel(100.0, 100));
        questLevels.add(new QuestLevel(150.0, 150));
        questLevels.add(new QuestLevel(200.0, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}
