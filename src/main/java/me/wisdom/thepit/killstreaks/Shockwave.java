package me.wisdom.thepit.killstreaks;

import me.wisdom.thepit.controllers.DamageManager;
import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.objects.Killstreak;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.megastreaks.RNGesus;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Shockwave extends Killstreak {
    public static Shockwave INSTANCE;

    public Shockwave() {
        super("Shockwave", "Shockwave", 40, 24);
        INSTANCE = this;
    }

    @Override
    public void proc(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(pitPlayer.getMegastreak() instanceof RNGesus && pitPlayer.getKills() >= RNGesus.INSTABILITY_THRESHOLD) {
            AOutput.error(player, "&c&lUNSTABLE!&7 Shockwave cannot be used in this reality");
            return;
        }

        for(int i = 0; i < 5; i++) {
            Location exploLoc = player.getLocation().clone().add(0, 1, 0);
            exploLoc.add(Math.random() * 10 - 5, 0, Math.random() * 10 - 5);
            player.getWorld().playEffect(exploLoc, Effect.EXPLOSION_HUGE, 1);
        }

        List<Entity> entityList = player.getNearbyEntities(4, 4, 4);
        List<Player> nonList = new ArrayList<>();
        for(Entity entity : entityList) {
            if(player.getWorld() == entity.getWorld() && entity instanceof Player && NonManager.getNon((Player) entity) != null &&
                    entity.getLocation().distance(player.getLocation()) < 4) nonList.add((Player) entity);
        }
        int count = 0;
        for(Player non : nonList) {
            if(non.getWorld() != player.getWorld()) continue;
            double distance = non.getLocation().distance(player.getLocation());
            if(distance < 2.5 && count < 15) {
                DamageManager.fakeKill(player, non);
                count++;
                if(distance < 2) {
                    DamageManager.fakeKill(player, non);
                    count++;
                }
            } else {
                non.setHealth(non.getHealth() / 2.0);
            }
        }

//		Sounds.SHOCKWAVE.play(player);
        player.playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 100);
    }

    @Override
    public void reset(Player player) {
    }

    @Override
    public ItemStack getDisplayStack(Player player) {
        AItemStackBuilder builder = new AItemStackBuilder(Material.MONSTER_EGG, 1, 60)
                .setName("&e" + displayName)
                .setLore(new ALoreBuilder(
                        "&7Every: &c" + killInterval + " kills",
                        "",
                        "&7Send out a shockwave, doing",
                        "&7massive damage to nearby bots."
                ));

        return builder.getItemStack();
    }

    @Override
    public String getSummary() {
        return "&eShockwave&7 is a killstreak that instantly kills bots near you every &c40 kills";
    }
}
