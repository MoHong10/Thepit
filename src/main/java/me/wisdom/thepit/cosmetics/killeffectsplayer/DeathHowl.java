package me.wisdom.thepit.cosmetics.killeffectsplayer;

import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class DeathHowl extends PitCosmetic {

    public DeathHowl() {
        super("&7Howl", "howl", CosmeticType.PLAYER_KILL_EFFECT);
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!PlayerManager.isRealPlayer(killEvent.getDeadPlayer()) || !isEnabled(killEvent.getKillerPitPlayer()) ||
                nearMid(killEvent.getDeadPlayer())) return;
        Sounds.DEATH_HOWL.play(killEvent.getDeadPlayer().getLocation(), SOUND_RANGE);
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.BONE)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7Devour your enemies and",
                        "&7howl like a werewolf!"
                ))
                .getItemStack();
        return itemStack;
    }
}
