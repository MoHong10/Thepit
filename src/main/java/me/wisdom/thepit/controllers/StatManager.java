package me.wisdom.thepit.controllers;

import de.myzelyam.api.vanish.VanishAPI;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.battlepass.quests.HoursPlayedQuest;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enchants.overworld.Regularity;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.HealEvent;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.megastreaks.NoMegastreak;
import me.wisdom.thepit.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class StatManager implements Listener {
    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
                    if(AFKManager.AFKPlayers.contains(player)) continue;
                    if(VanishAPI.isInvisible(player)) continue;
                    pitPlayer.stats.minutesPlayed++;
                    HoursPlayedQuest.INSTANCE.progressTime(pitPlayer);
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, Misc.getRunnableOffset(1), 20 * 60L);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMessage(AsyncPlayerChatEvent event) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(event.getPlayer());
        pitPlayer.stats.chatMessages++;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onShoot(EntityShootBowEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer((Player) event.getEntity());
        pitPlayer.stats.arrowShots++;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHeal(HealEvent event) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(event.getPlayer());
        if(event.healType == HealEvent.HealType.HEALTH) {
            pitPlayer.stats.healthRegained += event.getEffectiveHeal();
        } else {
            pitPlayer.stats.absorptionGained += event.getEffectiveHeal();
        }
    }

    //		TODO: UPDATE
    @EventHandler(priority = EventPriority.MONITOR)
    public void onAttack(AttackEvent.Post attackEvent) {
        if(!attackEvent.isAttackerPlayer() || !attackEvent.isDefenderPlayer()) return;
        PitPlayer pitAttacker = attackEvent.getAttackerPitPlayer();
        PitPlayer pitDefender = attackEvent.getDefenderPitPlayer();

        if(attackEvent.getPet() == null) {
            if(attackEvent.getArrow() == null) pitAttacker.stats.swordHits++;
            else pitAttacker.stats.arrowHits++;
        }
        pitAttacker.stats.damageDealt += attackEvent.getFinalDamage();
        pitAttacker.stats.trueDamageDealt += attackEvent.getApplyEvent().trueDamage + attackEvent.getApplyEvent().veryTrueDamage;
        pitAttacker.stats.trueDamageTaken += attackEvent.getApplyEvent().selfTrueDamage + attackEvent.getApplyEvent().selfVeryTrueDamage;

        if(Regularity.isRegHit(attackEvent.getDefender())) pitAttacker.stats.regularity++;

        pitDefender.stats.damageTaken += attackEvent.getFinalDamage();
        pitDefender.stats.trueDamageTaken += attackEvent.getApplyEvent().trueDamage + attackEvent.getApplyEvent().veryTrueDamage;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHit(KillEvent killEvent) {
        PitPlayer pitKiller = killEvent.getKillerPitPlayer();
        PitPlayer pitDead = killEvent.getDeadPitPlayer();

        if(pitKiller != null) {
            if(HopperManager.isHopper(killEvent.getDead())) {
                pitKiller.stats.hopperKills++;
            } else if(PlayerManager.isRealPlayer(killEvent.getDeadPlayer())) {
                pitKiller.stats.playerKills++;
            } else {
                pitKiller.stats.botKills++;
            }

            pitKiller.stats.totalGold += killEvent.getFinalGold();
        }

        if(pitDead != null) {
            pitDead.stats.deaths++;
            if(!(pitDead.getMegastreak() instanceof NoMegastreak) && pitDead.getKills() > pitDead.stats.highestStreak)
                pitDead.stats.highestStreak = pitDead.getKills();
        }
    }
}
