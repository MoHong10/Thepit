package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.Cooldown;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.megastreaks.RNGesus;
import me.wisdom.thepit.megastreaks.Uberstreak;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class Pullbow extends PitEnchant {

    public Pullbow() {
        super("Pullbow", true, ApplyType.BOWS,
                "pullbow", "pull");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onAttack(AttackEvent.Pre attackEvent) {
        if(!attackEvent.isAttackerPlayer()) return;
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        if(attackEvent.getAttacker() == attackEvent.getDefender()) return;

        Cooldown cooldown = getCooldown(attackEvent.getAttackerPlayer(), 20 * 8);
        if(cooldown.isOnCooldown()) return;
        else cooldown.restart();

        if(attackEvent.isDefenderPlayer()) {
            PitPlayer pitDefender = attackEvent.getDefenderPitPlayer();
            if(pitDefender.isOnMega()) {
                if(pitDefender.getMegastreak() instanceof Uberstreak || pitDefender.getMegastreak() instanceof RNGesus) return;
            }
        }

        Vector distanceVector = attackEvent.getAttacker().getLocation().subtract(attackEvent.getDefender().getLocation()).toVector().setY(0);
        double distance = Math.min(distanceVector.length(), getCapDistance(enchantLvl));
        Vector horizontalVelocity = distanceVector.clone().normalize().multiply(distance * 0.16);
        double yComponent = Math.min(distance * 0.02 + 0.23, 0.65);
        Vector finalVelocity = horizontalVelocity.clone().setY(yComponent);

        attackEvent.setCancelled(true);
        attackEvent.getArrow().remove();
        attackEvent.getDefender().damage(0);
        new BukkitRunnable() {
            @Override
            public void run() {
                attackEvent.getDefender().setVelocity(finalVelocity);
            }
        }.runTaskLater(Thepit.INSTANCE, 1L);

        Sounds.PULLBOW.play(attackEvent.getAttackerPlayer());
        PitPlayer pitAttacker = PitPlayer.getPitPlayer(attackEvent.getAttackerPlayer());
        pitAttacker.stats.pullbow++;
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Hitting a player pulls them to you (8s cooldown). Effect caps at &e" + getCapDistance(enchantLvl) +
                        " block" + (getCapDistance(enchantLvl) == 1 ? "" : "s") + " &7of distance"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that pulls " +
                "players to you when you shoot them";
    }

    public static int getCapDistance(int enchantLvl) {
        return enchantLvl * 5 + 10;
    }
}
