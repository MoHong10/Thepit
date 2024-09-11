package me.wisdom.thepit.megastreaks;

import me.wisdom.thepit.battlepass.quests.daily.DailyMegastreakQuest;
import me.wisdom.thepit.controllers.LevelManager;
import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.objects.Megastreak;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.upgrades.DoubleDeath;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class Beastmode extends Megastreak {
    public static Beastmode INSTANCE;

    public Beastmode() {
        super("&aBeastmode", "beastmode", 50, 13, 50);
        INSTANCE = this;
    }

    @EventHandler
    public void onHit(AttackEvent.Apply attackEvent) {
        if(!hasMegastreak(attackEvent.getDefenderPlayer())) return;
        PitPlayer pitPlayer = attackEvent.getDefenderPitPlayer();
        if(!pitPlayer.isOnMega()) return;
        if(NonManager.getNon(attackEvent.getAttacker()) == null) {
            attackEvent.increasePercent += (pitPlayer.getKills() - 50) * 0.3;
        } else {
            attackEvent.increasePercent += (pitPlayer.getKills() - 50) * 5 * 0.3;
        }
    }

    @EventHandler
    public void kill(KillEvent killEvent) {
        if(!hasMegastreak(killEvent.getKillerPlayer())) return;
        PitPlayer pitPlayer = killEvent.getKillerPitPlayer();
        if(!pitPlayer.isOnMega()) return;
        killEvent.xpCap += 130;
        killEvent.xpMultipliers.add(2.0);
        killEvent.goldMultipliers.add(0.5);
    }

    @Override
    public void proc(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        Sounds.MEGA_GENERAL.play(player.getLocation());
        pitPlayer.stats.timesOnBeastmode++;
        DailyMegastreakQuest.INSTANCE.onMegastreakComplete(pitPlayer);
    }

    @Override
    public void reset(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(!pitPlayer.isOnMega()) return;

        int randomXP = Misc.intBetween(1000, 5000);
        if(DoubleDeath.INSTANCE.isDoubleDeath(pitPlayer.player)) randomXP *= 2;
        AOutput.send(pitPlayer.player, getCapsDisplayName() + "!&7 从 megastreak 中获得了 &b" + Formatter.commaFormat.format(randomXP) +
                "&b XP &7！");
        LevelManager.addXP(pitPlayer.player, randomXP);
    }

    @Override
    public String getPrefix(Player player) {
        return "&a&lBEAST";
    }

    @Override
    public ItemStack getBaseDisplayStack(Player player) {
        return new AItemStackBuilder(Material.DIAMOND_HELMET)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, PitPlayer pitPlayer) {
        loreBuilder.addLore(
                "&7触发时:",
                "&a\u25a0 &7从击杀中获得 &b+100% XP",
                "&a\u25a0 &7从击杀中获得 &b+130 最大 XP",
                "",
                "&7但:",
                "&c\u25a0 &7每超过 50 次击杀受到 &c+0.3% &7的伤害",
                "&7（对机器人伤害增加 5 倍）",
                "&c\u25a0 &7从击杀中获得的 &6金币 &7减少 50%",
                "",
                "&7死亡时:",
                "&e\u25a0 &7获得 &b1000 到 &b5000 XP"
        );
    }

    @Override
    public String getSummary() {
        return getCapsDisplayName() + "&7 是一个 Megastreak，使你获得更多的 &bXP&7 和 &b最大 XP&7，" +
                "但在死亡时获得 &bXP&7，且使你从击杀中获得更少的 &6金币&7，并在击杀超过 50 次后受到更多伤害";
    }
}
