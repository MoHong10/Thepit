package me.wisdom.thepit.perks;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.SpawnManager;
import me.wisdom.thepit.controllers.objects.PitPerk;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.events.PitQuitEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.upgrades.UnlockStreaker;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Streaker extends PitPerk {
    public static Map<LivingEntity, Integer> playerTimes = new HashMap<>();
    public static Map<LivingEntity, Double> xpReward = new HashMap<>();

    public static Streaker INSTANCE;

    public Streaker() {
        super("Streaker", "streaker");
        renownUpgradeClass = UnlockStreaker.class;
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        xpReward.remove(killEvent.getDead());
        playerTimes.remove(killEvent.getDead());

        if (!hasPerk(killEvent.getKiller())) return;
        if (!killEvent.isDeadPlayer() || NonManager.getNon(killEvent.getDead()) == null) return;
        killEvent.xpCap += 80;

        if (xpReward.containsKey(killEvent.getKiller()))
            killEvent.xpMultipliers.add(xpReward.get(killEvent.getKiller()));

        if (!killEvent.isKillerPlayer()) return;
        PitPlayer pitPlayer = killEvent.getKillerPitPlayer();

        if (pitPlayer.getKills() + 1 >= pitPlayer.getMegastreak().requiredKills) {
            if (playerTimes.containsKey(killEvent.getKiller())) {
                Player player = killEvent.getKillerPlayer();
                double xp = 0;

                if (playerTimes.get(player) > 90) xp = 1;
                if (playerTimes.get(player) <= 90) xp = 1.1;
                if (playerTimes.get(player) <= 80) xp = 1.2;
                if (playerTimes.get(player) <= 60) xp = 1.3;
                if (playerTimes.get(player) <= 45) xp = 1.4;
                if (playerTimes.get(player) <= 30) xp = 1.5;
                if (playerTimes.get(player) <= 25) xp = 1.6;
                if (playerTimes.get(player) <= 20) xp = 1.7;
                if (playerTimes.get(player) <= 15) xp = 1.8;
                if (playerTimes.get(player) <= 10) xp = 1.9;
                if (playerTimes.get(player) <= 5) xp = 2;

                xpReward.put(player, xp);

                DecimalFormat format = new DecimalFormat("0.#");
                AOutput.send(player, "&b&lSTREAKER!&7 你在 &e" +
                        playerTimes.get(player) + " 秒&7 内完成了你的连击 streak。获得 &b+" + format.format(Math.ceil((xp - 1) * 100)) + "% XP &7在剩下的 streak 中。");
                Sounds.STREAKER.play(player);
                playerTimes.remove(player);
                return;
            }
        }

        if (!playerTimes.containsKey(killEvent.getKiller()) && !pitPlayer.isOnMega() && !SpawnManager.isInSpawn(pitPlayer.player)) {
            playerTimes.put(killEvent.getKiller(), 0);
            Sounds.STREAKER.play(killEvent.getKiller());
            AOutput.send(killEvent.getKiller(), "&b&lSTREAKER!&7 连胜计时开始!");
        }
    }

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<LivingEntity, Integer> entry : playerTimes.entrySet()) {
                    int time = entry.getValue();
                    time = time + 1;
                    playerTimes.put(entry.getKey(), time);
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 20L);
    }

    @EventHandler
    public void onQuit(PitQuitEvent event) {
        xpReward.remove(event.getPlayer());
        playerTimes.remove(event.getPlayer());
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.WHEAT)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, Player player) {
        loreBuilder.addLongLine(
                "&7 达到你的 &cMegastreak&7 时，击中 mega 的速度越快，获得的 &bXP 就越多。 " +
                        "被动地获得 &b+80 最大 XP"
        );
    }

    @Override
    public String getSummary() {
        return "&eStreaker &7 是一个在 &erenown shop&7 解锁的 perk，能给你更高的 &bXP 上限 &7和更多的 " +
                "&bXP &7，基于你激活 &cMegastreak 的速度";
    }
}
