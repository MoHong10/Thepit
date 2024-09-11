package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.Cooldown;
import me.wisdom.thepit.controllers.HitCounter;
import me.wisdom.thepit.controllers.PolarManager;
import me.wisdom.thepit.controllers.objects.AnticheatManager;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.List;

public class ComboStun extends PitEnchant {

    public ComboStun() {
        super("Combo: Stun", true, ApplyType.MELEE,
                "combostun", "stun", "combo-stun", "cstun");
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isAttackerPlayer()) return;
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;
        if(attackEvent.isFakeHit()) return;

        int regLvl = attackEvent.getAttackerEnchantLevel(Regularity.INSTANCE);
        if(Regularity.isRegHit(attackEvent.getDefender()) && Regularity.skipIncrement(regLvl)) return;

        Cooldown cooldown = getCooldown(attackEvent.getAttackerPlayer(), 8 * 20);
        if(cooldown.isOnCooldown()) return;

        PitPlayer pitPlayer = attackEvent.getAttackerPitPlayer();
        HitCounter.incrementCounter(pitPlayer.player, this);
        if(!HitCounter.hasReachedThreshold(pitPlayer.player, this, 5)) return;

        else cooldown.restart();
        int duration = (int) getDuration(enchantLvl) * 20;

        if(Thepit.anticheat instanceof PolarManager) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Thepit.anticheat.exemptPlayer(attackEvent.getDefenderPlayer(), duration * 500L, AnticheatManager.FlagType.KNOCKBACK, AnticheatManager.FlagType.SIMULATION);
                }
            }.runTaskLater(Thepit.INSTANCE, 10);
        }

        Misc.stunEntity(attackEvent.getDefender(), duration);
        Sounds.COMBO_STUN.play(attackEvent.getAttacker());

        pitPlayer.stats.stun += getDuration(enchantLvl);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return new PitLoreBuilder(
                "&7The &efifth &7strike on an enemy stuns them for " +
                        decimalFormat.format(getDuration(enchantLvl)) + " &7seconds &o(Can only be stunned every 8s)"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that stuns " +
                "your opponent every few strikes";
    }

    public double getDuration(int enchantLvl) {
        return enchantLvl * 0.4 + 0.8;
    }
}
