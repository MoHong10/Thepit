package me.wisdom.thepit.darkzone;

import me.wisdom.thepit.Thepit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public abstract class PitBossAbility implements Listener {
    private PitBoss pitBoss;
    private boolean enabled = true;
    private double routineWeight;

    public PitBossAbility() {
        Bukkit.getPluginManager().registerEvents(this, Thepit.INSTANCE);
    }

    public PitBossAbility(double routineWeight) {
        this();
        this.routineWeight = routineWeight;
    }

    //	Internal events (override to add functionality)
    public void onRoutineExecute() {}
    public boolean shouldExecuteRoutine() {
        return true;
    }
    public void onEnable() {}
    public void onDisable() {}

    public PitBossAbility pitBoss(PitBoss pitBoss) {
        this.pitBoss = pitBoss;
        return this;
    }

    public boolean isAssignedBoss(LivingEntity entity) {
        return getPitBoss().getBoss() == entity;
    }

    public void disable() {
        if(!enabled) return;
        enabled = false;
        HandlerList.unregisterAll(this);
        onDisable();
    }

    public List<Player> getViewers() {
        List<Player> viewers = new ArrayList<>();
        for(Entity entity : pitBoss.getBoss().getNearbyEntities(40, 40, 40)) {
            if(!(entity instanceof Player)) continue;
            Player player = Bukkit.getPlayer(entity.getUniqueId());
            if(player != null) viewers.add(player);
        }
        return viewers;
    }

    public PitBoss getPitBoss() {
        return pitBoss;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public double getRoutineWeight() {
        return routineWeight;
    }

    public boolean isNearToBoss(Player player) {
        return getPitBoss().getBoss().getWorld() == player.getWorld() && getPitBoss().getBoss().getLocation().distance(player.getLocation()) <= 50;
    }
}
