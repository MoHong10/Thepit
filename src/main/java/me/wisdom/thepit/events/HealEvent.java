package me.wisdom.thepit.events;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

public class HealEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public LivingEntity entity;
    public boolean isPlayer;
    public Player player;
    private final double initialHeal;
    public HealType healType;
    public int max;
    public List<Double> multipliers = new ArrayList<>();

    //	Optional/Nullable
    public PitEnchant pitEnchant;

    public HealEvent(LivingEntity entity, double initialHeal, HealType healType, int max) {
        this.entity = entity;
        this.isPlayer = entity instanceof Player;
        this.player = isPlayer ? (Player) entity : null;
        this.initialHeal = initialHeal;
        this.healType = healType;
        this.max = max;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public double getFinalHeal() {

        double finalHeal = initialHeal;
        for(Double multiplier : multipliers) finalHeal *= multiplier;
        return finalHeal;
    }

    public double getEffectiveHeal() {
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        if(healType != HealType.HEALTH && nmsPlayer.getAbsorptionHearts() < max)
            return Math.min(getFinalHeal(), max - nmsPlayer.getAbsorptionHearts());
        return Math.min(getFinalHeal(), player.getMaxHealth() - player.getHealth());
    }

    public enum HealType {
        HEALTH,
        ABSORPTION
    }

    public Player getPlayer() {
        return player;
    }
}
