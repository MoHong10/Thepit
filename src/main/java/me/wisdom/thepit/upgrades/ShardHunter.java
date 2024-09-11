package me.wisdom.thepit.upgrades;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.controllers.objects.TieredRenownUpgrade;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.inventories.ShardHunterPanel;
import me.wisdom.thepit.items.misc.AncientGemShard;
import me.wisdom.thepit.megastreaks.Uberstreak;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class ShardHunter extends TieredRenownUpgrade {
    public static ShardHunter INSTANCE;

    public ShardHunter() {
        super("Shardhunter", "SHARDHUNTER", 28, ShardHunterPanel.class);
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!killEvent.isKillerPlayer() || NonManager.getNon(killEvent.getDead()) == null) return;
        if(!UpgradeManager.hasUpgrade(killEvent.getKillerPlayer(), this)) return;

        int tier = UpgradeManager.getTier(killEvent.getKillerPlayer(), this);
        if(tier == 0) return;

        double chance = 0.00005 * tier;

        PitPlayer pitKiller = killEvent.getKillerPitPlayer();
        if(pitKiller.isOnMega() && pitKiller.getMegastreak() instanceof Uberstreak)
            chance *= Uberstreak.SHARD_MULTIPLIER;

        boolean givesShard = Math.random() < chance;

        if(!givesShard) return;
        AUtil.giveItemSafely(killEvent.getKillerPlayer(), ItemFactory.getItem(AncientGemShard.class).getItem(1), true);
        AOutput.send(killEvent.getKiller(), "&d&l宝石碎片！&7 从击杀 " + killEvent.getDeadPlayer().getDisplayName() + " 获得！");

        File file = new File("plugins/NoteBlockAPI/Effects/ShardHunter.nbs");
        Song song = NBSDecoder.parse(file);
        RadioSongPlayer radioPlayer = new RadioSongPlayer(song);
        radioPlayer.setRepeatMode(RepeatMode.NO);
        radioPlayer.addPlayer(killEvent.getKillerPlayer());
        radioPlayer.setAutoDestroy(true);
        radioPlayer.setPlaying(true);
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.EMERALD)
                .getItemStack();
    }

    @Override
    public String getCurrentEffect(int tier) {
        DecimalFormat decimalFormat = new DecimalFormat("0.####");
        return "&f" + decimalFormat.format(tier * 0.005) + "% 几率";
    }

    @Override
    public String getEffectPerTier() {
        return "&7获得 &f+0.005% 几率 &7在击杀机器人时获得一个 &a宝石碎片&7，用于制作 &a完全合法的宝石";
    }

    @Override
    public String getSummary() {
        return "&a碎片猎人&7 是一个 &e声望&7 升级，给你一个小几率在击杀机器人时获得 &a古代宝石碎片&7，" +
                "&a碎片&7 可以用来制作一个 &a宝石&7，使你能够获得九个 &3珠宝&7 物品";
    }

    @Override
    public List<Integer> getTierCosts() {
        return Arrays.asList(10, 13, 16, 19, 22, 25, 30, 35, 40, 50);
    }
}
