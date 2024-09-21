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
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class FightOrFlight extends Killstreak {
    public static FightOrFlight INSTANCE;
    public static List<LivingEntity> rewardPlayers = new ArrayList<>();

    public FightOrFlight() {
        super("Fight or Flight", "FightOrFlight", 7, 4);
        INSTANCE = this;
    }

    @EventHandler
    public void onHit(AttackEvent.Apply event) {
        if(rewardPlayers.contains(event.getAttacker())) event.increasePercent += 20;
    }

    @Override
    public void proc(Player player) {
        if(player.getHealth() < player.getMaxHealth() / 2) {
            Misc.applyPotionEffect(player, PotionEffectType.SPEED, 20 * 7, 2, true, false);
        } else {
            if(!rewardPlayers.contains(player)) rewardPlayers.add(player);

            new BukkitRunnable() {
                @Override
                public void run() {
                    rewardPlayers.remove(player);
                }
            }.runTaskLater(Thepit.INSTANCE, 7 * 20L);
        }
    }

    @Override
    public void reset(Player player) {
        rewardPlayers.remove(player);
    }

    @Override
    public ItemStack getDisplayStack(Player player) {
        AItemStackBuilder builder = new AItemStackBuilder(Material.FIREBALL)
                .setName("&e" + displayName)
                .setLore(new ALoreBuilder(
                        "&7Every: &c" + killInterval + " kills",
                        "",
                        "&7If below half &c\u2764&7:",
                        "&7Gain &eSpeed III &7for 7 seconds.",
                        "",
                        "&7Otherwise:",
                        "&7Deal &c+20% &7damage for 7 seconds."
                ));

        return builder.getItemStack();
    }

    @Override
    public String getSummary() {
        return "&cFight &7or &fFlight&7 is a killstreak that gives you &eSpeed&7 or &cDamage&7 for a short " +
                "time based on your &chealh &7every &c7 kills";
    }
}
