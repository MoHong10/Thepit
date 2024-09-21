package me.wisdom.thepit.controllers;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.Hopper;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.events.PitQuitEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class HopperManager implements Listener {
    public static List<Hopper> hopperList = new ArrayList<>();
    public static List<Hopper> toRemove = new ArrayList<>();

    public HopperManager() {
        SkinManager.loadSkin("PayForTruce");

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Hopper hopper : hopperList) {
//					if(!hopper.npc.isSpawned() && hopper.count > 20) toRemove.add(hopper);
                    hopper.tick();
                }
                for(Hopper hopper : toRemove) hopperList.remove(hopper);
                toRemove.clear();
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 1L);
    }

    public static Hopper callHopper(String name, Hopper.Type type, Player target) {
        Hopper hopper = new Hopper(name, type, target);
        hopperList.add(hopper);
        return hopper;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAttack(AttackEvent.Pre attackEvent) {
        if(!isHopper(attackEvent.getDefender()) || attackEvent.getWrapperEvent().hasAttackInfo()) return;
        attackEvent.setCancelled(true);
        attackEvent.getDefender().setNoDamageTicks(0);

        DamageManager.hitCooldownList.remove(attackEvent.getDefender());
        DamageManager.hopperCooldownList.remove(attackEvent.getDefender());
        DamageManager.createIndirectAttack(attackEvent.getAttacker(), attackEvent.getDefender(), attackEvent.getWrapperEvent().getDamage());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(isHopper(attackEvent.getDefender())) {
            Hopper hopper = getHopper(attackEvent.getDefenderPlayer());
            attackEvent.multipliers.add(hopper.type.damageMultiplier);
            if(attackEvent.getArrow() != null || attackEvent.getPet() != null) {
                attackEvent.multipliers.add(0D);
                attackEvent.trueDamage = 0;
            }
        }
        if(isHopper(attackEvent.getAttacker())) {
            Hopper hopper = getHopper(attackEvent.getAttackerPlayer());
            if(hopper.type != Hopper.Type.CHAIN) {
                PitPlayer pitHopper = attackEvent.getAttackerPitPlayer();
                double amount = 1;
                if(hopper.target != null && hopper.target != attackEvent.getDefender()) pitHopper.heal(amount / 2.0);
                pitHopper.heal(amount);
            }
        }
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        for(Hopper hopper : hopperList) {
            if(killEvent.getDead() != hopper.target) continue;
            hopper.remove();
        }

        if(isHopper(killEvent.getDead())) {
            Hopper hopper = HopperManager.getHopper(killEvent.getDeadPlayer());
            hopper.remove();
        }
    }

    @EventHandler
    public void onLeave(PitQuitEvent event) {
        for(Hopper hopper : hopperList) {
            if(event.getPlayer() != hopper.target) continue;
            hopper.remove();
        }
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        for(Hopper hopper : hopperList) {
            if(player != hopper.target) continue;
            hopper.hopper.teleport(hopper.target);
        }
    }

    public static boolean isHopper(LivingEntity checkPlayer) {
        if(!(checkPlayer instanceof Player)) return false;
        Player player = (Player) checkPlayer;
        return getHopper(player) != null;
    }

    public static Hopper getHopper(Player player) {
        for(Hopper hopper : hopperList) if(hopper.hopper == player) return hopper;
        return null;
    }
}
