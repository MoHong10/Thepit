package me.wisdom.thepit.cosmetics.killeffectsbot;

import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepit.enums.KillModifier;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class OnlyExe extends PitCosmetic {

    public OnlyExe() {
        super("&c&lOnly &6Exe", "onlyexe", CosmeticType.BOT_KILL_EFFECT);
        preventKillSound = true;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(NonManager.getNon(killEvent.getDead()) == null || !isEnabled(killEvent.getKillerPitPlayer()) ||
                killEvent.hasKillModifier(KillModifier.EXECUTION)) return;
        Sounds.EXE.play(killEvent.getKillerPlayer());
        killEvent.getKillerPlayer().playEffect(killEvent.getDeadPlayer().getLocation().add(0, 1, 0), Effect.STEP_SOUND, 152);
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.GOLD_SWORD)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "",
                        "&dRARE! &9Executioner",
                        "&7The classic executioner effect will",
                        "&7only play whenever you kill bots!"
                ))
                .getItemStack();
        return itemStack;
    }
}
