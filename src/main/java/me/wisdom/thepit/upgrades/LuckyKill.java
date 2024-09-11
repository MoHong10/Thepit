package me.wisdom.thepit.upgrades;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.TieredRenownUpgrade;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.megastreaks.RNGesus;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class LuckyKill extends TieredRenownUpgrade {
    public static LuckyKill INSTANCE;

    public LuckyKill() {
        super("Lucky Kill", "LUCKY_KILL", 5);
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!UpgradeManager.hasUpgrade(killEvent.getKillerPlayer(), this) ||
                NonManager.getNon(killEvent.getDead()) == null) return;

        if(killEvent.getKillerPitPlayer().getMegastreak() instanceof RNGesus &&
                killEvent.getKillerPitPlayer().getKills() >= RNGesus.INSTABILITY_THRESHOLD) return;

        double chance = 0.01 * UpgradeManager.getTier(killEvent.getKillerPlayer(), this);
        boolean isLuckyKill = Math.random() < chance;
        if(!isLuckyKill) return;

        killEvent.xpMultipliers.add(3.0);
        killEvent.maxXPMultipliers.add(3.0);
        killEvent.goldMultipliers.add(3.0);
        killEvent.goldCap *= 3.0;
        AOutput.send(killEvent.getKiller(), "&d&l幸运击杀!&7 奖励三倍!");

        File file = new File("plugins/NoteBlockAPI/Effects/LuckyKill.nbs");
        Song song = NBSDecoder.parse(file);
        RadioSongPlayer rsp = new RadioSongPlayer(song);
        rsp.setRepeatMode(RepeatMode.NO);
        rsp.addPlayer(killEvent.getKillerPlayer());
        rsp.setAutoDestroy(true);
        rsp.setPlaying(true);
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.NAME_TAG)
                .getItemStack();
    }

    @Override
    public String getCurrentEffect(int tier) {
        return "&f" + tier + "% 几率";
    }

    @Override
    public String getEffectPerTier() {
        return "&7获得 &f+1% 几率 &7在击杀机器人时使其变成 &d幸运击杀&7，三倍所有击杀奖励和上限";
    }

    @Override
    public String getSummary() {
        return "&d幸运击杀&7 是一个 &e声望&7 升级，给你一个小几率 &d三倍&7 机器人击杀奖励";
    }

    @Override
    public List<Integer> getTierCosts() {
        return Arrays.asList(10, 20, 30, 40);
    }
}
