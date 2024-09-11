package me.wisdom.thepit.upgrades;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.TieredRenownUpgrade;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DoubleDeath extends TieredRenownUpgrade {
    public static DoubleDeath INSTANCE;

    public DoubleDeath() {
        super("Double-Death", "DOUBLE_DEATH", 9);
        INSTANCE = this;
    }

    public boolean isDoubleDeath(Player player) {
        if(!UpgradeManager.hasUpgrade(player, this)) return false;

        int tier = UpgradeManager.getTier(player, this);
        if(tier == 0) return false;

        double chance = 0.01 * (tier * 1);

        boolean isDouble = Math.random() < chance;

        if(isDouble) {
            AOutput.send(player, "&d&l双倍死亡！&7 Megastreak 死亡奖励翻倍！");

            File file = new File("plugins/NoteBlockAPI/Effects/DoubleDeath.nbs");
            Song song = NBSDecoder.parse(file);
            RadioSongPlayer rsp = new RadioSongPlayer(song);
            rsp.setRepeatMode(RepeatMode.NO);
            rsp.addPlayer(player);
            rsp.setAutoDestroy(true);
            rsp.setPlaying(true);
        }

        return isDouble;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.SKULL_ITEM)
                .getItemStack();
    }

    @Override
    public String getCurrentEffect(int tier) {
        return "&d" + (tier * 1) + "% 概率";
    }

    @Override
    public String getEffectPerTier() {
        return "&7获得 &d+5% 几率 &7来双倍 Megastreak 死亡奖励";
    }

    @Override
    public String getSummary() {
        return "&d双倍死亡&7 给你一个机会在 &cMegastreak&7 上获得双倍死亡奖励";
    }

    @Override
    public List<Integer> getTierCosts() {
        return Arrays.asList(5, 10, 20, 40, 80);
    }
}
