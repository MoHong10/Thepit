package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.SpawnManager;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.VolleyShootEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Volley extends PitEnchant {

    public Volley() {
        super("Volley", true, ApplyType.BOWS,
                "volley");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBowShoot(EntityShootBowEvent event) {
        if(event instanceof VolleyShootEvent) return;

        if(!(event.getEntity() instanceof Player) || !(event.getProjectile() instanceof Arrow)) return;
        Player player = ((Player) event.getEntity()).getPlayer();
        Arrow arrow = (Arrow) event.getProjectile();

        int enchantLvl = EnchantManager.getEnchantLevel(player, this);
        if(enchantLvl == 0) return;

        new BukkitRunnable() {
            int count = 0;
            final double arrowSpeed = arrow.getVelocity().length();

            @Override
            public void run() {
                if(++count == getArrows(enchantLvl)) {
                    cancel();
                    return;
                }

                if(SpawnManager.isInSpawn(player)) return;

                Arrow volleyArrow = player.launchProjectile(Arrow.class);
                volleyArrow.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(arrowSpeed));

                VolleyShootEvent volleyShootEvent = new VolleyShootEvent(event.getEntity(), event.getBow(), volleyArrow, event.getForce());
                Thepit.INSTANCE.getServer().getPluginManager().callEvent(volleyShootEvent);
                if(volleyShootEvent.isCancelled()) volleyArrow.remove();

                PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
                pitPlayer.stats.volley++;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Sounds.VOLLEY.play(player);
                    }
                }.runTaskLater(Thepit.INSTANCE, 1L);
            }
        }.runTaskTimer(Thepit.INSTANCE, 2L, 2L);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Shoot &f" + getArrows(enchantLvl) + " arrows &7at once"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that shoots multiple " +
                "arrows each time you release your bow";
    }

    public int getArrows(int enchantLvl) {
        return enchantLvl + 1;
    }
}
