package me.wisdom.thepit.battlepass.quests;

import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enchants.overworld.MegaLongBow;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class LandMLBShotsQuest extends PassQuest {
    public static LandMLBShotsQuest INSTANCE;

    public LandMLBShotsQuest() {
        super("&a&l弓箭手", "mlbshots", QuestType.WEEKLY);
        INSTANCE = this;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(attackEvent.getArrow() == null || !MegaLongBow.mlbShots.contains(attackEvent.getArrow().getUniqueId()) ||
                !PlayerManager.isRealPlayer(attackEvent.getAttackerPlayer()) || !PlayerManager.isRealPlayer(attackEvent.getDefenderPlayer()) ||
                attackEvent.getAttacker() == attackEvent.getDefender()) return;
        progressQuest(attackEvent.getAttackerPitPlayer(), 1);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.ARROW)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7在玩家身上使用 &aMLB 的弓着地 &a" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &7次",
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
        questLevels.add(new QuestLevel(2_000, 100));
        questLevels.add(new QuestLevel(3_000, 150));
        questLevels.add(new QuestLevel(4_000, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}
