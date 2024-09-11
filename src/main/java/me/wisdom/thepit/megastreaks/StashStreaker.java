package me.wisdom.thepit.megastreaks;

import me.wisdom.thepit.battlepass.quests.daily.DailyMegastreakQuest;
import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.objects.Megastreak;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class StashStreaker extends Megastreak {
    public static StashStreaker INSTANCE;

    public StashStreaker() {
        super("&8Stash Streaker", "stashstreaker", 200, 0, 50);
        INSTANCE = this;
    }

    public static boolean isActive(PitPlayer pitPlayer) {
        if (pitPlayer == null) return false;
        return pitPlayer.getMegastreak() instanceof StashStreaker && pitPlayer.isOnMega();
    }

    @EventHandler
    public void onAttack(AttackEvent.Pre attackEvent) {
        if (!hasMegastreak(attackEvent.getAttackerPlayer())) return;
        PitPlayer pitPlayer = attackEvent.getAttackerPitPlayer();
        if (!pitPlayer.isOnMega() || NonManager.getNon(attackEvent.getDefender()) == null) return;
        attackEvent.setCancelled(true);
        AOutput.error(pitPlayer.player, "&c&l错误!&7 你不能在 " + getCapsDisplayName() + "&7 上攻击机器人!");
    }

    @Override
    public void proc(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        Sounds.MEGA_GENERAL.play(player.getLocation());
        pitPlayer.stats.timesOnStashStreaker++;
        DailyMegastreakQuest.INSTANCE.onMegastreakComplete(pitPlayer);
    }

    @Override
    public String getPrefix(Player player) {
        return "&8&lSTASH";
    }

    @Override
    public ItemStack getBaseDisplayStack(Player player) {
        return new AItemStackBuilder(Material.CHAINMAIL_LEGGINGS)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, PitPlayer pitPlayer) {
        loreBuilder.addLore(
                "&7触发时:",
                "&a\u25a0 &7在非战斗状态下可以使用 /ec",
                "",
                "&7但:",
                "&c\u25a0 &7你不能攻击机器人",
                "",
                "&7死亡时:",
                "&e\u25a0 &7保护你的物品栏"
        );
    }

    @Override
    public String getSummary() {
        return getCapsDisplayName() + "&7 是一个 Megastreak";
    }
}
