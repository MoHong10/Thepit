package me.wisdom.thepit.controllers;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enchants.tainted.chestplate.Persephone;
import me.wisdom.thepit.enchants.tainted.uncommon.Mending;
import me.wisdom.thepitapi.misc.ASound;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class ShieldManager implements Listener {
    public static final double ACTIVE_REGEN_AMOUNT = 0.05;

    public ShieldManager() {
        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    PitPlayer pitPlayer = PitPlayer.getPitPlayer(onlinePlayer);
                    if(pitPlayer.shield.isActive()) {
                        double activeRegenAmount = ACTIVE_REGEN_AMOUNT;
                        activeRegenAmount *= Mending.getIncreaseMultiplier(pitPlayer.player);
                        activeRegenAmount *= Persephone.getShieldRegenMultiplier(pitPlayer.player);
                        pitPlayer.shield.addShield(activeRegenAmount);
                    } else {
                        pitPlayer.shield.regenerateTick();
                        if(count % 2 == 0 && pitPlayer.shield.isUnlocked()) {
                            int reactivationTicks = pitPlayer.shield.getTicksUntilReactivation();
                            int initialTicks = pitPlayer.shield.getInitialTicksUntilReactivation();
                            float pitch = ((float) (initialTicks - reactivationTicks) / initialTicks) * 1.5F + 0.5F;
                            ASound.play(onlinePlayer, Sound.BLOCK_NOTE_BASS, 0.75F, pitch);
                        }
                    }
                    pitPlayer.updateXPBar();
                }
                count++;
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 1L);
    }
}
