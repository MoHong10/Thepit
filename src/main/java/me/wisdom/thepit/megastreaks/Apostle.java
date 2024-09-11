package me.wisdom.thepit.megastreaks;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.battlepass.quests.daily.DailyMegastreakQuest;
import me.wisdom.thepit.controllers.LevelManager;
import me.wisdom.thepit.controllers.objects.Megastreak;
import me.wisdom.thepit.controllers.objects.PitPlayer;
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
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class Apostle extends Megastreak {
    public static Apostle INSTANCE;
    public static Map<Player, Long> storedXPMap = new HashMap<>();

    public Apostle() {
        super("&3Apostle", "apostle", 100, 42, 110);
        INSTANCE = this;

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    PitPlayer pitPlayer = PitPlayer.getPitPlayer(onlinePlayer);
                    if (!(pitPlayer.getMegastreak() instanceof Apostle) || !pitPlayer.isOnMega()) continue;
                    Misc.sendActionBar(onlinePlayer, "&7存储的 XP: &b" +
                            Formatter.commaFormat.format(storedXPMap.getOrDefault(onlinePlayer, 0L)) +
                            " &7(&b" + Formatter.decimalCommaFormat.format(getMultiplier(pitPlayer)) + "x&7)");
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0, 1L);
    }

    public static int getRemovedHealth(PitPlayer pitPlayer) {
        if(!(pitPlayer.getMegastreak() instanceof Apostle) || !pitPlayer.isOnMega()) return 0;
        return ((pitPlayer.getKills() - getStartingHeartsReduction()) / getFrequencyHeartsReduction() + 1) * 2;
    }

    public static double getMultiplier(PitPlayer pitPlayer) {
        if(!pitPlayer.isOnMega()) return 0;
        return Math.min(getPerKillMultiplier() * (pitPlayer.getKills() - INSTANCE.requiredKills), getMaxMultiplier());
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!hasMegastreak(killEvent.getKillerPlayer())) return;
        PitPlayer pitPlayer = killEvent.getKillerPitPlayer();
        if(!pitPlayer.isOnMega()) return;
        killEvent.xpMultipliers.add(1 + (getXPIncrease() / 100.0));
        killEvent.xpCap += getInitialMaxXP() + pitPlayer.apostleBonus;
        killEvent.goldMultipliers.add(0.5);
        pitPlayer.updateMaxHealth();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKillMonitor(KillEvent killEvent) {
        if(!hasMegastreak(killEvent.getKillerPlayer())) return;
        PitPlayer pitPlayer = killEvent.getKillerPitPlayer();
        if(!pitPlayer.isOnMega()) return;
        storedXPMap.put(killEvent.getKillerPlayer(), storedXPMap.getOrDefault(killEvent.getKillerPlayer(), 0L) + killEvent.getFinalXp());
    }

    @Override
    public void proc(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        Sounds.MEGA_GENERAL.play(player.getLocation());
        pitPlayer.stats.timesOnApostle++;
        DailyMegastreakQuest.INSTANCE.onMegastreakComplete(pitPlayer);
    }

    @Override
    public void reset(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(!pitPlayer.isOnMega()) return;

        int doubleDeathMultiplier = DoubleDeath.INSTANCE.isDoubleDeath(pitPlayer.player) ? 2 : 1;

        if(storedXPMap.containsKey(player)) {
            long finalXP = (long) (storedXPMap.remove(player) * getMultiplier(pitPlayer) * doubleDeathMultiplier);
            AOutput.send(pitPlayer.player, getCapsDisplayName() + "!&7 从 megastreak 中获得了 &b" + Formatter.commaFormat.format(finalXP) +
                    "&b XP &7！");
            LevelManager.addXP(pitPlayer.player, finalXP);
        }

        if(pitPlayer.getKills() >= 1000) {
            int apostleIncrease = Math.min(getMaxXPIncrement() * doubleDeathMultiplier, getMaxMaxXPIncrease() - pitPlayer.apostleBonus);
            if(apostleIncrease != 0) {
                pitPlayer.apostleBonus += apostleIncrease;
                AOutput.send(pitPlayer.player, getCapsDisplayName() + "!&7 使用这个 megastreak 时永久增加 &b+" + apostleIncrease +
                        " 最大 XP &7！(&b" + pitPlayer.apostleBonus + "&7/&b" + getMaxMaxXPIncrease() + "&7)");
            }
        }
    }

    @Override
    public String getPrefix(Player player) {
        return "&3&lAPSTL";
    }

    @Override
    public ItemStack getBaseDisplayStack(Player player) {
        return new AItemStackBuilder(Material.STEP, 1, 7)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, PitPlayer pitPlayer) {
        loreBuilder.addLore(
                "&7触发时：",
                "&a\u25a0 &7从击杀中获得 &b+" + getXPIncrease() + "% XP",
                "&a\u25a0 &7从击杀中获得 &b+" + (getInitialMaxXP() + pitPlayer.apostleBonus) + " 最大 XP",
                "",
                "&7但是：",
                "&c\u25a0 &7从 " + getStartingHeartsReduction() + " 次击杀开始，每 " + getFrequencyHeartsReduction() + " 次击杀",
                "   &c减少 1 最大 \u2764",
                "&c\u25a0 &7从击杀中获得 &c-50% &7金币",
                "",
                "&7在连胜期间：",
                "&e\u25a0 &7储存你获得的 &bXP",
                "",
                "&7死亡时：",
                "&e\u25a0 &7获得储存的 &bXP &7并乘以",
                "   &7每超过 100 次击杀的 &b" + getPerKillMultiplier() + "x",
                "   &7最高可达 &b" + Formatter.decimalCommaFormat.format(getMaxMultiplier()) + "x",
                "&e\u25a0 &7如果你的连胜至少为 1,000，",
                "   &7永久改变此 megastreak 的",
                "   &b最大 XP &7每击杀增加 &b+" + getMaxXPIncrement() + " (&b" + pitPlayer.apostleBonus + "&7/&b" + getMaxMaxXPIncrease() + "&7)"
        );
    }

    @Override
    public String getSummary() {
        return getCapsDisplayName() + "&7它是一个 Megastreak";}

    public static int getXPIncrease() {
        return 140;
    }

    public static int getInitialMaxXP() {
        return 100;
    }

    public static int getMaxMaxXPIncrease() {
        return 500;
    }

    public static int getMaxXPIncrement() {
        return 3;
    }

    public static double getPerKillMultiplier() {
        return 0.002;
    }

    public static double getMaxMultiplier() {
        return 1;
    }

    public static int getStartingHeartsReduction() {
        return 200;
    }

    public static int getFrequencyHeartsReduction() {
        return 100;
    }
}
