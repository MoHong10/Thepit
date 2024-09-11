package me.wisdom.thepit.megastreaks;

import me.wisdom.thepit.battlepass.quests.daily.DailyMegastreakQuest;
import me.wisdom.thepit.commands.FPSCommand;
import me.wisdom.thepit.controllers.DamageManager;
import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepit.controllers.objects.Megastreak;
import me.wisdom.thepit.controllers.objects.Non;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.upgrades.HandOfGreed;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prosperity extends Megastreak {
    public static Prosperity INSTANCE;
    public static Map<Player, List<Player>> hiddenBotMap = new HashMap<>();

    public Prosperity() {
        super("&eProsperity", "prosperity", 50, 35, 50);
        INSTANCE = this;
    }

    @EventHandler
    public void onAttack(AttackEvent.Pre attackEvent) {
        if(!PlayerManager.isRealPlayer(attackEvent.getAttackerPlayer()) || !PlayerManager.isRealPlayer(attackEvent.getDefenderPlayer()) ||
                attackEvent.getAttacker() == attackEvent.getDefender()) return;
        if(hasMegastreak(attackEvent.getAttackerPlayer()) && attackEvent.getAttackerPitPlayer().getKills() >= 1000) attackEvent.setCancelled(true);
    }

    @EventHandler
    public void onAttack2(AttackEvent.Pre attackEvent) {
        if(!attackEvent.isAttackerRealPlayer() || !attackEvent.isDefenderPlayer() ||
                !hiddenBotMap.containsKey(attackEvent.getAttackerPlayer())) return;
        List<Player> hiddenBotList = hiddenBotMap.get(attackEvent.getAttackerPlayer());
        if(!hiddenBotList.contains(attackEvent.getDefenderPlayer())) return;
        attackEvent.setCancelled(true);
        AOutput.error(attackEvent.getAttackerPlayer(), "&c&l错误!&7 你不能攻击你看不见的玩家!");
    }

    @EventHandler
    public void kill(KillEvent killEvent) {
        if(!hasMegastreak(killEvent.getKillerPlayer()) || !killEvent.isDeadPlayer()) return;
        PitPlayer pitPlayer = killEvent.getKillerPitPlayer();
        if(!pitPlayer.isOnMega()) return;
        killEvent.goldMultipliers.add(1 + (getGoldIncrease() / 100.0));
        killEvent.xpMultipliers.add(0.5);

        if(pitPlayer.getKills() < 1000) return;
        hiddenBotMap.putIfAbsent(pitPlayer.player, new ArrayList<>());
        List<Player> hiddenBotList = hiddenBotMap.get(pitPlayer.player);
        hiddenBotList.add(killEvent.getDeadPlayer());
        killEvent.getKillerPlayer().hidePlayer(killEvent.getDeadPlayer());
        killEvent.goldMultipliers.add((double) getFinalGoldMultiplier());
        killEvent.goldCap *= getFinalGoldMultiplier();

        boolean allNonsHidden = true;
        for(Non non : NonManager.nons) {
            if(hiddenBotList.contains(non.non)) continue;
            allNonsHidden = false;
            break;
        }
        if(allNonsHidden) DamageManager.killPlayer(killEvent.getKillerPlayer());
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

        Sounds.MEGA_GENERAL.play(player.getLocation());
        pitPlayer.stats.timesOnProsperity++;
        DailyMegastreakQuest.INSTANCE.onMegastreakComplete(pitPlayer);
    }

    @Override
    public void reset(Player player) {
        hiddenBotMap.remove(player);
        if(FPSCommand.fpsActivePlayers.contains(player)) return;
        for(Non non : NonManager.nons) player.showPlayer(non.non);
    }

    @Override
    public String getPrefix(Player player) {
        return "&e&lPROSP";
    }

    @Override
    public ItemStack getBaseDisplayStack(Player player) {
        return new AItemStackBuilder(Material.SPECKLED_MELON)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, PitPlayer pitPlayer) {
        int prosperityBonus = HandOfGreed.getGoldIncrease(pitPlayer.player);
        loreBuilder.addLore(
                "&7触发时:",
                "&a\u25a0 &7从击杀中获得 &6+" + getGoldIncrease() + "% 的金币",
                "&a\u25a0 &7对机器人造成 &c+" + getDamageIncrease() + "% 伤害"
        );
        if(prosperityBonus != 0) loreBuilder.addLore(
                "&a\u25a0 &e" + HandOfGreed.INSTANCE.name + "&7: 从击杀中获得 &6精确的 +&6" + Formatter.commaFormat.format(prosperityBonus) + "g",
                "   &7（忽略加成和",
                "   &7金币上限）"
        );
        loreBuilder.addLore(
                "",
                "&7但是:",
                "&c\u25a0 &7如果中间没有机器人，&c死亡！",
                "&c\u25a0 &7从击杀中获得 &c-50% &7经验",
                "",
                "&7达到 1,000 次击杀时:",
                "&a\u25a0 &7击杀时获得的金币和金币上限",
                "   &7增加 &6" + getFinalGoldMultiplier() + "倍",
                "&c\u25a0 &7击杀机器人后不会重新生成"
        );
    }

    @Override
    public String getSummary() {
        return getCapsDisplayName() + "&7 是一个 Megastreak";
    }

    public static int getGoldIncrease() {
        return 100;
    }

    public static int getFinalGoldMultiplier() {
        return 5;
    }

    public static int getDamageIncrease() {
        return 33;
    }
}
