package me.wisdom.thepit.darkzone.abilities;

import me.wisdom.thepit.darkzone.PitBossAbility;
import me.wisdom.thepit.events.AttackEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

public class LaunchAbility extends PitBossAbility {

    int intensity;

    public LaunchAbility(int intensity) {
        super();
        this.intensity = intensity;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!isAssignedBoss(attackEvent.getAttacker())) return;
        attackEvent.getDefender().setVelocity(
                attackEvent.getDefender().getVelocity().add(new Vector(0, 10 * intensity, 0)
                ));

    }



}
