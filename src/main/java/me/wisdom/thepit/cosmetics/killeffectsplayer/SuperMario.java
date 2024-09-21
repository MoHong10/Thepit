package me.wisdom.thepit.cosmetics.killeffectsplayer;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class SuperMario extends PitCosmetic {

    public SuperMario() {
        super("&b&lS&e&lU&c&lP&a&lE&e&lR &c&lM&a&lA&e&lR&b&lI&e&lO", "supermario", CosmeticType.PLAYER_KILL_EFFECT);
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!PlayerManager.isRealPlayer(killEvent.getDeadPlayer()) || !isEnabled(killEvent.getKillerPitPlayer()) ||
                nearMid(killEvent.getDeadPlayer())) return;

        File file = new File("plugins/NoteBlockAPI/Effects/mariodeath.nbs");
        Song song = NBSDecoder.parse(file);
        PositionSongPlayer psp = new PositionSongPlayer(song);
        psp.setTargetLocation(killEvent.getDeadPlayer().getLocation());
        psp.setDistance(SOUND_RANGE);
        psp.setRepeatMode(RepeatMode.NO);
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) psp.addPlayer(onlinePlayer);
        psp.setAutoDestroy(true);
        psp.setPlaying(true);
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.HUGE_MUSHROOM_2)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7A &8&lSUPER&7 tune for a &8&lSUPER",
                        "&7kill!"
                ))
                .getItemStack();
        return itemStack;
    }
}
