package me.wisdom.thepit.battlepass.quests;

import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.AttackEvent;
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

public class DoTrueDamageVSPlayersQuest extends PassQuest {

    public DoTrueDamageVSPlayersQuest() {
        super("&9&l当玩家飞起来", "truedamageplayers", QuestType.WEEKLY);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!PlayerManager.isRealPlayer(attackEvent.getAttackerPlayer()) || !canProgressQuest(attackEvent.getAttackerPitPlayer())
                || !PlayerManager.isRealPlayer(attackEvent.getDefenderPlayer()) || attackEvent.getDefenderPlayer().isOnGround())
            return;
        progressQuest(attackEvent.getAttackerPitPlayer(), attackEvent.trueDamage);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.RED_ROSE, 1, 1)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7对不在地面上的玩家造成 &9" + Misc.getHearts(questLevel.getRequirement(pitPlayer)) + " &7点真实伤害",
                        "",
                        "&7进度: &3" + Formatter.formatLarge(progress / 2) + "&7/&3" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer) / 2) + " &8[" +
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
        questLevels.add(new QuestLevel(3_000.0, 100));
        questLevels.add(new QuestLevel(4_500.0, 150));
        questLevels.add(new QuestLevel(6_000.0, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}
