package me.wisdom.thepit.killstreaks;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.Killstreak;
import me.wisdom.thepit.events.HealEvent;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Survivor extends Killstreak {
    public static Survivor INSTANCE;
    public static List<Player> rewardPlayers = new ArrayList<>();

    public Survivor() {
        super("Survivor", "Survivor", 15, 0);
        INSTANCE = this;
    }

    @EventHandler
    public void onHeal(HealEvent healEvent) {
        if(!rewardPlayers.contains(healEvent.player)) return;
        if(healEvent.healType == HealEvent.HealType.HEALTH) healEvent.multipliers.add(1.25D);
    }

    @Override
    public void proc(Player player) {
        if(!rewardPlayers.contains(player)) rewardPlayers.add(player);
        Sounds.SURVIVOR_HEAL.play(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                rewardPlayers.remove(player);
            }
        }.runTaskLater(Thepit.INSTANCE, 15 * 20L);
    }

    @Override
    public void reset(Player player) {
        rewardPlayers.remove(player);
    }

    @Override
    public ItemStack getDisplayStack(Player player) {
        AItemStackBuilder builder = new AItemStackBuilder(Material.GOLDEN_APPLE)
                .setName("&e" + displayName)
                .setLore(new ALoreBuilder(
                        "&7Every: &c" + killInterval + " kills",
                        "",
                        "&7Heal &e25% &7more &c\u2764",
                        "&7for 15 seconds."
                ));

        return builder.getItemStack();
    }

    @Override
    public String getSummary() {
        return "&eSurvivor&7 is a killstreak that increases your &chealing&7 for a short period of time every &c15 kills";
    }
}
