package me.wisdom.thepit.megastreaks;

import me.wisdom.thepit.Thepit;
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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Overdrive extends Megastreak {
    public static Overdrive INSTANCE;

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    PitPlayer pitPlayer = PitPlayer.getPitPlayer(onlinePlayer);
                    if(pitPlayer.getMegastreak() != Overdrive.INSTANCE || !pitPlayer.isOnMega()) continue;
                    Misc.applyPotionEffect(onlinePlayer, PotionEffectType.SPEED, 200, 0, true, false);
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 60L);
    }

    public Overdrive() {
        super("&cOverdrive", "overdrive", 50, 0, 0);
        INSTANCE = this;
    }

    @EventHandler
    public void onHit(AttackEvent.Apply attackEvent) {
        if(!hasMegastreak(attackEvent.getDefenderPlayer())) return;
        PitPlayer pitPlayer = attackEvent.getDefenderPitPlayer();
        if(!pitPlayer.isOnMega() || NonManager.getNon(attackEvent.getAttacker()) == null) return;
        attackEvent.veryTrueDamage += (pitPlayer.getKills() - 50) / 50D;
    }

    @EventHandler
    public void kill(KillEvent killEvent) {
        if(!hasMegastreak(killEvent.getKillerPlayer())) return;
        PitPlayer pitPlayer = killEvent.getKillerPitPlayer();
        if(!pitPlayer.isOnMega()) return;
        killEvent.xpMultipliers.add(1 + (getXPIncrease() / 100.0));
        killEvent.goldMultipliers.add(1 + (getGoldIncrease() / 100.0));
    }

    @Override
    public void proc(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        Sounds.MEGA_GENERAL.play(player.getLocation());
        pitPlayer.stats.timesOnOverdrive++;
        DailyMegastreakQuest.INSTANCE.onMegastreakComplete(pitPlayer);
    }

    @Override
    public void reset(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(!pitPlayer.isOnMega()) return;

        int randomGold = Misc.intBetween(1000, 5000);
        if(DoubleDeath.INSTANCE.isDoubleDeath(pitPlayer.player)) randomGold = randomGold * 2;
        AOutput.send(pitPlayer.player, getCapsDisplayName() + "!&7 从连击中获得了 &6+" + Formatter.decimalCommaFormat.format(randomGold) +
                "&6金币&7！");
        LevelManager.addGold(pitPlayer.player, randomGold);
    }

    @Override
    public String getPrefix(Player player) {
        return "&c&lOVRDV";
    }

    @Override
    public ItemStack getBaseDisplayStack(Player player) {
        return new AItemStackBuilder(Material.BLAZE_POWDER)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, PitPlayer pitPlayer) {
        loreBuilder.addLore(
                "&7触发时:",
                "&a\u25a0 &7从击杀中获得 &b+" + getXPIncrease() + "% 经验",
                "&a\u25a0 &7从击杀中获得 &6+" + getGoldIncrease() + "% 金币",
                "&a\u25a0 &7永久 &e速度 I",
                "&a\u25a0 &7免疫 &9减速",
                "",
                "&7但:",
                "&c\u25a0 &7每 10 次击杀（仅对机器人有效）",
                "   &7受到 &c+" + Misc.getHearts(0.2) + " 伤害",
                "",
                "&7死亡时:",
                "&e\u25a0 &7获得 &61000 到 &65000 金币"
        );
    }

    @Override
    public String getSummary() {
        return getCapsDisplayName() + "&7 是一种连击，给予你永久的 &e速度 I、增加你的 &6金币 和 &b经验，" +
                "免疫 &9减速，死亡时获得 &6金币，但每 10 次击杀会受到很真实的伤害。";
    }

    public static int getXPIncrease() {
        return 50;
    }

    public static int getGoldIncrease() {
        return 50;
    }
}
