package me.wisdom.thepit.killstreaks;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.Killstreak;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Limiter extends Killstreak {
    public static Limiter INSTANCE;
    public List<LivingEntity> rewardPlayers = new ArrayList<>();

    public Limiter() {
        super("Limiter", "Limiter", 3, 0);
        INSTANCE = this;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHit(AttackEvent.Apply event) {
        if(rewardPlayers.contains(event.getDefender())) event.trueDamage = Math.min(event.trueDamage, 2);
    }

    @Override
    public void proc(Player player) {
        if(!rewardPlayers.contains(player)) rewardPlayers.add(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                rewardPlayers.remove(player);
            }
        }.runTaskLater(Thepit.INSTANCE, 3 * 20L);
    }

    @Override
    public void reset(Player player) {
        rewardPlayers.remove(player);
    }

    @Override
    public ItemStack getDisplayStack(Player player) {
        AItemStackBuilder builder = new AItemStackBuilder(Material.ANVIL)
                .setName("&e" + displayName)
                .setLore(new ALoreBuilder(
                        "&7Every: &c" + killInterval + " kills",
                        "",
                        "&7Limit the true damage you",
                        "&7can take per hit to &9" + Misc.getHearts(2),
                        "&7for 3 seconds"
                ));

        return builder.getItemStack();
    }

    @Override
    public String getSummary() {
        return "&eLimiter&7 is a killstreak that limits the amount of &9true damage&7 you can take every &c3 kills";
    }
}
