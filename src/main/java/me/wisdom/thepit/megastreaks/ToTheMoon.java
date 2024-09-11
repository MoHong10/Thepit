package me.wisdom.thepit.megastreaks;

import me.wisdom.thepit.battlepass.quests.daily.DailyMegastreakQuest;
import me.wisdom.thepit.controllers.HopperManager;
import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.objects.Hopper;
import me.wisdom.thepit.controllers.objects.Megastreak;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.KillEvent;
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

import java.util.ArrayList;
import java.util.List;

public class ToTheMoon extends Megastreak {
    public static ToTheMoon INSTANCE;
    public static List<Player> hopperCallList = new ArrayList<>();

    public ToTheMoon() {
        super("&bTo the Moon", "tothemoon", 100, 30, 60);
        INSTANCE = this;
    }

    @EventHandler
    public void onHit(AttackEvent.Apply attackEvent) {
        if(!hasMegastreak(attackEvent.getDefenderPlayer())) return;
        PitPlayer pitPlayer = attackEvent.getDefenderPitPlayer();
        if(!pitPlayer.isOnMega()) return;

        if(pitPlayer.getKills() > 200) {
            double increase = 3 * ((pitPlayer.getKills() - 200) / 20);
            if(NonManager.getNon(attackEvent.getAttacker()) == null) {
                attackEvent.increasePercent += increase;
            } else attackEvent.increasePercent += increase * 5;
        }
        if(pitPlayer.getKills() > 400) {
            if(NonManager.getNon(attackEvent.getAttacker()) == null) {
                attackEvent.increase += 0.2 * ((pitPlayer.getKills() - 400) / 100);
            } else attackEvent.increase += 1.0 * ((pitPlayer.getKills() - 400) / 100);
        }
        if(pitPlayer.getKills() > 700) {
            attackEvent.veryTrueDamage += 0.2 * ((pitPlayer.getKills() - 700) / 10);
        }
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!hasMegastreak(killEvent.getKillerPlayer())) return;
        PitPlayer pitPlayer = killEvent.getKillerPitPlayer();
        if(!pitPlayer.isOnMega()) return;

        killEvent.xpMultipliers.add(1 + (getXPIncrease() / 100.0));
        killEvent.xpCap += getMaxXPIncrease();
        killEvent.xpCap += (pitPlayer.getKills() - 100) * 1.0;
        killEvent.goldMultipliers.add(0.5);

        if(pitPlayer.getKills() > 1500 && !hopperCallList.contains(killEvent.getKillerPlayer())) {
            HopperManager.callHopper("PayForTruce", Hopper.Type.VENOM, killEvent.getKillerPlayer());
            hopperCallList.add(killEvent.getKillerPlayer());
        }
    }

    @Override
    public void proc(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        Sounds.MEGA_GENERAL.play(pitPlayer.player.getLocation());
        pitPlayer.stats.timesOnMoon++;
        DailyMegastreakQuest.INSTANCE.onMegastreakComplete(pitPlayer);
    }

    @Override
    public void reset(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        hopperCallList.remove(player);

        if(!pitPlayer.isOnMega()) return;

        if(pitPlayer.getKills() >= 700) {
            int capIncrease = 5;
            if(DoubleDeath.INSTANCE.isDoubleDeath(pitPlayer.player)) capIncrease *= 2;
            capIncrease = Math.min(capIncrease, 50 - pitPlayer.moonBonus);
            if(capIncrease > 0) {
                pitPlayer.moonBonus += capIncrease;
                AOutput.send(pitPlayer.player, getCapsDisplayName() + "!&7 获得了 &b+" + capIncrease +
                        " 最大 XP &7直到你晋级! (&b" + pitPlayer.moonBonus + "&7/&b50&7)");
            }
        }
    }

    @Override
    public String getPrefix(Player player) {
        return "&b&lMOON";
    }

    @Override
    public ItemStack getBaseDisplayStack(Player player) {
        return new AItemStackBuilder(Material.ENDER_STONE)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, PitPlayer pitPlayer) {
        loreBuilder.addLore(
                "&7触发时:",
                "&a\u25a0 &7从击杀中获得 &b+" + getXPIncrease() + "% XP",
                "&a\u25a0 &7从击杀中获得 &b+" + getMaxXPIncrease() + " 最大 XP",
                "&a\u25a0 &7每击杀获得 &b+1 最大 XP",
                "",
                "&7但:",
                "&c\u25a0 &7从200击杀开始，每20击杀受到 &c+3%",
                "   &7的伤害增幅。（来自机器人的伤害5倍）",
                "&c\u25a0 &7从400击杀开始，每100击杀受到 &c+" + Misc.getHearts(0.2),
                "   &7的伤害增幅。（来自机器人的伤害5倍）",
                "&c\u25a0 &7从700击杀开始，每10击杀受到 &c+" + Misc.getHearts(0.2),
                "   &7非常真实的伤害。",
                "&c\u25a0 &7从击杀中获得 &c-50% &7的金币",
                "",
                "&7死亡时:",
                "&e\u25a0 &7获得一个永久的 &b+5 最大 XP",
                "&7直到你晋级（&b" + pitPlayer.moonBonus + "&7/&b50&7），前提是",
                "&7你的连杀数至少为700"
        );
    }

    @Override
    public String getSummary() {
        return getCapsDisplayName() + "&7 使你获得增加的 &bXP&7 和 &bXP上限&7，二者都随击杀增加，但会根据你的连杀数承受" +
                "&c伤害&7、&9真实伤害&7 和 &c非常&9真实伤害&7，同时在很高的连杀数下生成一个 &5增益器&7。";
    }

    public static int getXPIncrease() {
        return 120;
    }

    public static int getMaxXPIncrease() {
        return 300;
    }
}
