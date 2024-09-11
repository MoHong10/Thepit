package me.wisdom.thepit.megastreaks;

import me.wisdom.thepit.battlepass.quests.daily.DailyMegastreakQuest;
import me.wisdom.thepit.controllers.DamageManager;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.objects.Megastreak;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.items.misc.GoldPickup;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class HighStakes extends Megastreak {
    public static HighStakes INSTANCE;

    public HighStakes() {
        super("&2High Stakes", "highstakes", 50, 8, 50);
        INSTANCE = this;
    }

    public static void spawnIngot(Player player, double multiplier) {
        Location spawnLoc = player.getLocation().add(Misc.randomOffset(1), Misc.randomOffsetPositive(1), Misc.randomOffset(1));
        Item item = spawnLoc.getWorld().dropItem(spawnLoc, ItemFactory.getItem(GoldPickup.class).getItem(GoldPickup.getPickupGold()));
        item.setVelocity(new Vector(Misc.randomOffset(1), Misc.randomOffsetPositive(1.5), Misc.randomOffset(1)).multiply(multiplier));
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!hasMegastreak(killEvent.getKillerPlayer())) return;
        if(!killEvent.getKillerPitPlayer().isOnMega() || killEvent.getKillerPitPlayer().getKills() % getKillInterval() != 0) return;

        for(int i = 0; i < getIngotCount(); i++) spawnIngot(killEvent.getDeadPlayer(), 0.7);
    }

    @EventHandler
    public void kill(KillEvent killEvent) {
        if(!hasMegastreak(killEvent.getKillerPlayer())) return;
        PitPlayer pitPlayer = killEvent.getKillerPitPlayer();
        if(!pitPlayer.isOnMega()) return;
        killEvent.goldMultipliers.add(1 + (getGoldIncrease() / 100.0));
        killEvent.xpMultipliers.add(0.5);

        if(Math.random() > 1.0 / getChanceToKill()) return;
        for(int i = 0; i < getDeathIngotCount(); i++) spawnIngot(killEvent.getKillerPlayer(), 1);
        DamageManager.killPlayer(killEvent.getKillerPlayer());
    }

    @Override
    public void proc(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        Sounds.MEGA_GENERAL.play(player.getLocation());
        pitPlayer.stats.timesOnHighStakes++;
        DailyMegastreakQuest.INSTANCE.onMegastreakComplete(pitPlayer);
    }

    @Override
    public void reset(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(!pitPlayer.isOnMega()) return;
    }

    @Override
    public String getPrefix(Player player) {
        return "&2&lSTAKE";
    }

    @Override
    public ItemStack getBaseDisplayStack(Player player) {
        return new AItemStackBuilder(Material.DIAMOND)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, PitPlayer pitPlayer) {
        loreBuilder.addLore(
                "&7触发时:",
                "&a\u25a0 &7从击杀中获得 &6+" + getGoldIncrease() + "% 金币",
                "&a\u25a0 &7每 &c" + getKillInterval() + " 次击杀&7，会掉落",
                "   &6" + getIngotCount() + " 个金币锭&7。捡起它们",
                "   &7会给予 &c再生 " + AUtil.toRoman(GoldPickup.getRegenAmplifier() + 1) +
                        " &7(" + GoldPickup.getRegenSeconds() + "秒) 和 &6" + GoldPickup.getPickupGold() + "金币",
                "",
                "&7但是:",
                "&c\u25a0 &7每击杀机器人有 &f1 &7在 &f" + Formatter.commaFormat.format(getChanceToKill()) + " &7的几率会 &c死亡!",
                "&c\u25a0 &7从击杀中获得的 &bXP&7 减少 50%",
                "",
                "&7死亡时:",
                "&e\u25a0 &7如果因 &c运气不好 &7而死亡，将会爆炸",
                "   &7掉落大量 &6金币锭"
        );
    }

    @Override
    public String getSummary() {
        return getCapsDisplayName() + "&7它是一个 Megastreak";
    }

    public static int getGoldIncrease() {
        return 75;
    }

    public static int getIngotCount() {
        return 5;
    }

    public static int getDeathIngotCount() {
        return 20;
    }

    public static int getKillInterval() {
        return 17;
    }

    public static int getChanceToKill() {
        return 1000;
    }
}
