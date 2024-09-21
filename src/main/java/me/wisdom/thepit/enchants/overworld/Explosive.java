package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.Cooldown;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.SpawnManager;
import me.wisdom.thepit.controllers.objects.Non;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.megastreaks.RNGesus;
import me.wisdom.thepit.megastreaks.Uberstreak;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class Explosive extends PitEnchant {

    public Explosive() {
        super("Explosive", true, ApplyType.BOWS,
                "explosive", "explo", "ex", "explode");
    }

    @EventHandler
    public void onShoot(ProjectileHitEvent event) {
        if(!(event.getEntity() instanceof Arrow) || !(event.getEntity().getShooter() instanceof Player)) return;

        Arrow arrow = (Arrow) event.getEntity();
        Player shooter = (Player) arrow.getShooter();

        int enchantLvl = EnchantManager.getEnchantLevel(shooter, this);
        if(enchantLvl == 0) return;

        Cooldown cooldown = getCooldown(shooter, getCooldown(enchantLvl));
        if(cooldown.isOnCooldown()) return;
        else cooldown.restart();

        if(SpawnManager.isInSpawn(arrow.getLocation())) return;

        for(Entity entity : arrow.getNearbyEntities(getRange(enchantLvl), getRange(enchantLvl), getRange(enchantLvl))) {
            if(entity instanceof Player) {
                Player player = (Player) entity;
                Non non = NonManager.getNon(player);

                if(SpawnManager.isInSpawn(player)) continue;

                if(player != shooter) {

                    PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
                    if(NonManager.getNon(player) == null) {
                        if(pitPlayer.isOnMega()) {
                            if(pitPlayer.getMegastreak() instanceof Uberstreak || pitPlayer.getMegastreak() instanceof RNGesus) continue;
                        }
                        Vector force = player.getLocation().toVector().subtract(arrow.getLocation().toVector())
                                .setY(1).normalize().multiply(non == null ? 1.15 : 5);
                        player.setVelocity(force);
                    }
                }
            }
        }

        playSound(arrow.getLocation(), enchantLvl);
        arrow.getWorld().playEffect(arrow.getLocation(), getEffect(enchantLvl),
                getEffect(enchantLvl).getData(), 100);

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(shooter);
        pitPlayer.stats.explosive++;
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        String explosionPhrase = enchantLvl < 3 ? "POP" : "BOOM";
        return new PitLoreBuilder(
                "&7Arrows go " + explosionPhrase + "! (" + getCooldown(enchantLvl) / 20 + "s cooldown)"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that shoots " +
                "explosive arrows that knock nearby players away from their point of landing";
    }

    public double getRange(int enchantLvl) {

        switch(enchantLvl) {
            case 1:
            case 2:
                return 2.5;
            case 3:
                return 6;

        }
        return 0;
    }

    public void playSound(Location location, int enchantLvl) {

        switch(enchantLvl) {
            case 1:
                Sounds.EXPLOSIVE_1.play(location);
                break;
            case 2:
                Sounds.EXPLOSIVE_2.play(location);
                break;
            case 3:
                Sounds.EXPLOSIVE_3.play(location);
        }
    }

    public Effect getEffect(int enchantLvl) {

        switch(enchantLvl) {
            case 1:
                return Effect.EXPLOSION_LARGE;
            case 2:
            case 3:
                return Effect.EXPLOSION_HUGE;

        }
        return null;
    }

    public int getCooldown(int enchantLvl) {

        switch(enchantLvl) {
            case 1:
                return 100;
            case 2:
                return 60;
            case 3:
                return 100;

        }
        return 0;
    }

}
