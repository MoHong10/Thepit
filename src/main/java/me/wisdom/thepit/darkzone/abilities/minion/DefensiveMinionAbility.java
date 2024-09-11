package me.wisdom.thepit.darkzone.abilities.minion;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.darkzone.SubLevelType;
import me.wisdom.thepit.events.AttackEvent;
import org.bukkit.event.EventHandler;

public class DefensiveMinionAbility extends MinionAbility {

    public int spawnAmount;
    public long cooldownTicks;
    public long lastSpawn = 0;

    public DefensiveMinionAbility(SubLevelType type, int spawnAmount, int maxMobs, long cooldownTicks) {
        super(type, maxMobs);

        this.spawnAmount = spawnAmount;
        this.cooldownTicks = cooldownTicks;
    }

    @EventHandler
    public void onHit(AttackEvent.Apply event) {
        if(event.getDefender() != getPitBoss().getBoss()) return;
        if(lastSpawn + cooldownTicks > Thepit.currentTick) return;
        lastSpawn = Thepit.currentTick;

        spawnMobs(getPitBoss().getBoss().getLocation().add(0, 2, 0), spawnAmount);
    }
}
