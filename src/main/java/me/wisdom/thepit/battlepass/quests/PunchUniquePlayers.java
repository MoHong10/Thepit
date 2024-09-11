package me.wisdom.thepit.battlepass.quests;

import me.wisdom.thepit.battlepass.PassData;
import me.wisdom.thepit.battlepass.PassManager;
import me.wisdom.thepit.battlepass.PassQuest;
import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import me.clip.placeholderapi.PlaceholderAPI;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class PunchUniquePlayers extends PassQuest {
    public static PunchUniquePlayers INSTANCE;

    public PunchUniquePlayers() {
        super("&f&l尴尬的问候", "punchplayers", QuestType.WEEKLY);
        INSTANCE = this;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!PlayerManager.isRealPlayer(attackEvent.getAttackerPlayer()) || !PlayerManager.isRealPlayer(attackEvent.getDefenderPlayer()) ||
                attackEvent.getAttacker() == attackEvent.getDefender() || attackEvent.getArrow() != null || attackEvent.getPet() != null ||
                !canProgressQuest(attackEvent.getAttackerPitPlayer())) return;

        if(!Misc.isAirOrNull(attackEvent.getAttackerPlayer().getItemInHand())) return;
        PassData passData = attackEvent.getAttackerPitPlayer().getPassData(PassManager.currentPass.startDate);
        if(passData.uniquePlayersPunched.contains(attackEvent.getDefenderPlayer().getUniqueId().toString())) return;
        passData.uniquePlayersPunched.add(attackEvent.getDefenderPlayer().getUniqueId().toString());
        progressQuest(attackEvent.getAttackerPitPlayer(), 1);
        Sounds.PUNCH_UNIQUE_PLAYER.play(attackEvent.getAttackerPlayer());
        String playerName = PlaceholderAPI.setPlaceholders(attackEvent.getDefenderPlayer(),
                ChatColor.translateAlternateColorCodes('&', "%luckperms_prefix%" + attackEvent.getDefenderPlayer().getName()));
        AOutput.send(attackEvent.getAttackerPlayer(), "&f&l问候!&7 你欢迎了 " + playerName);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, QuestLevel questLevel, double progress) {
        ItemStack itemStack = new AItemStackBuilder(Material.BRICK)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7向 &f" + Formatter.formatLarge(questLevel.getRequirement(pitPlayer)) + " &7玩家打一个招呼吧",
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
        questLevels.add(new QuestLevel(20, 100));
        questLevels.add(new QuestLevel(30, 150));
        questLevels.add(new QuestLevel(40, 200));
    }

    @Override
    public double getMultiplier(PitPlayer pitPlayer) {
        return 1.0;
    }
}
