package me.wisdom.thepit.megastreaks;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.battlepass.quests.daily.DailyMegastreakQuest;
import me.wisdom.thepit.controllers.ChatTriggerManager;
import me.wisdom.thepit.controllers.LevelManager;
import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.objects.Megastreak;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.HealEvent;
import me.wisdom.thepit.events.KillEvent;
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

import java.text.DecimalFormat;

public class Highlander extends Megastreak {
    public static Highlander INSTANCE;

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

    public Highlander() {
        super("&6Highlander", "highlander", 50, 17, 90);
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!hasMegastreak(killEvent.getKillerPlayer())) return;
        PitPlayer pitPlayer = killEvent.getKillerPitPlayer();
        if(!pitPlayer.isOnMega()) return;
        killEvent.goldMultipliers.add(1 + (getGoldIncrease() / 100.0));
        killEvent.xpMultipliers.add(0.5);
    }

    @EventHandler
    public void ohHeal(HealEvent healEvent) {
        if(!hasMegastreak(healEvent.getPlayer())) return;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(healEvent.getPlayer());
        if(!pitPlayer.isOnMega()) return;
        if(pitPlayer.getKills() > 200) healEvent.multipliers.add(1 / ((pitPlayer.getKills() - 200) / 50.0 + 1));
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!hasMegastreak(attackEvent.getAttackerPlayer())) return;
        PitPlayer pitPlayer = attackEvent.getAttackerPitPlayer();
        if(!pitPlayer.isOnMega() || NonManager.getNon(attackEvent.getDefender()) == null) return;
        attackEvent.increasePercent += getDamageIncrease();
    }

    @Override
    public void proc(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        Sounds.MEGA_GENERAL.play(pitPlayer.player.getLocation());
        pitPlayer.stats.timesOnHighlander++;
        DailyMegastreakQuest.INSTANCE.onMegastreakComplete(pitPlayer);
    }

    @Override
    public void reset(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(!pitPlayer.isOnMega()) return;

        if(DoubleDeath.INSTANCE.isDoubleDeath(pitPlayer.player)) pitPlayer.bounty = pitPlayer.bounty * 2;
        LevelManager.addGold(pitPlayer.player, pitPlayer.bounty);
        if(pitPlayer.bounty != 0 && pitPlayer.isOnMega()) {
            DecimalFormat formatter = new DecimalFormat("#,###.#");
            AOutput.send(pitPlayer.player, getCapsDisplayName() + "!&7 从 Megastreak 中获得了 &6+" + formatter.format(pitPlayer.bounty) + "&6金币&7！");
            pitPlayer.bounty = 0;
            ChatTriggerManager.sendBountyInfo(pitPlayer);
        }
    }

    @Override
    public String getPrefix(Player player) {
        return "&6&lHIGH";
    }


    @Override
    public ItemStack getBaseDisplayStack(Player player) {
        return new AItemStackBuilder(Material.GOLD_BOOTS)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, PitPlayer pitPlayer) {
        loreBuilder.addLore(
                "&7触发时:",
                "&a\u25a0 &7永久获得 &e速度 I&7",
                "&a\u25a0 &7从击杀中获得 &6+" + getGoldIncrease() + "% 金币",
                "&a\u25a0 &7对机器人造成 &c+" + getDamageIncrease() + "% 伤害",
                "",
                "&7但是:",
                "&c\u25a0 &7每次击杀超过 200 次会减少治愈效果",
                "&c\u25a0 &7从击杀中获得的 &bXP&7 减少 50%",
                "",
                "&7死亡时:",
                "&e\u25a0 &7还会获得自己的赏金"
        );
    }

    @Override
    public String getSummary() {
        return getCapsDisplayName() + "&7 是一个 Megastreak，给予你增加的 &6金币、永久的 &e速度 I&7、更多对机器人的伤害，并在死亡时获得你的赏金，但在击杀超过 200 次后治疗效果减少，并且从击杀中获得的 &bXP&7 减少";
    }

    public static int getGoldIncrease() {
        return 110;
    }

    public static int getDamageIncrease() {
        return 25;
    }
}
