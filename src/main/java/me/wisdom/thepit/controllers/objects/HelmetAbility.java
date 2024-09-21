package me.wisdom.thepit.controllers.objects;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class HelmetAbility implements Listener {
    public static List<HelmetAbility> helmetAbilities = new ArrayList<>();
    public static List<UUID> toggledHelmets = new ArrayList<>();

    public Player player;
    public String name;
    public String refName;
    public boolean isTogglable;
    public boolean isActive = false;
    public int slot;
    public int cost;

    public HelmetAbility(Player player, String name, String refName, boolean isTogglable, int slot) {
        this.player = player;
        this.name = name;
        this.refName = refName;
        this.isTogglable = isTogglable;
        this.slot = slot;
        if(player != null) Thepit.INSTANCE.getServer().getPluginManager().registerEvents(this, Thepit.INSTANCE);
    }

    public void onActivate() {
    }

    public abstract boolean shouldActivate();

    public void onDeactivate() {
    }

    public void onProc() {
    }

    public abstract List<String> getDescription();

    public abstract ItemStack getDisplayStack();

    public static HelmetAbility getAbility(String refName) {
        for(HelmetAbility helmetAbility : helmetAbilities) {
            if(helmetAbility.refName.equals(refName)) return helmetAbility;
        }
        return null;
    }

    public static void registerHelmetAbility(HelmetAbility helmetAbility) {

        helmetAbilities.add(helmetAbility);
    }

    public Map<UUID, Cooldown> cooldowns = new HashMap<>();

    public Cooldown getCooldown(Player player, int time) {

        if(cooldowns.containsKey(player.getUniqueId())) {
            Cooldown cooldown = cooldowns.get(player.getUniqueId());
            cooldown.initialTime = time;
            return cooldown;
        }

        Cooldown cooldown = new Cooldown(player.getUniqueId(), time);
        cooldown.ticksLeft = 0;
        cooldowns.put(player.getUniqueId(), cooldown);
        return cooldown;
    }

    public boolean isActive(Player player) {
        return isActive(player, this);
    }

    public static boolean isActive(Player player, HelmetAbility ability) {
        return ability.isActive;
    }

    public void unload() {
        HandlerList.unregisterAll(this);
    }
}
