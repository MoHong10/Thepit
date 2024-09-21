package me.wisdom.thepit.enchants.tainted.scythe;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.DamageManager;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitEnchantSpell;
import me.wisdom.thepit.cosmetics.particles.FireworkSparkParticle;
import me.wisdom.thepit.darkzone.DarkzoneBalancing;
import me.wisdom.thepit.events.SpellUseEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.MutableInteger;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ElectricShock extends PitEnchantSpell {
    public static ElectricShock INSTANCE;

    public ElectricShock() {
        super("Electric Shock",
                "electricshock", "shock", "electric");
        isTainted = true;
        INSTANCE = this;
    }

    @EventHandler(ignoreCancelled = true)
    public void onUse(SpellUseEvent event) {
        if(!isThisSpell(event.getSpell())) return;
        Player player = event.getPlayer();

//		pick initial target
        Location testLocation = player.getLocation();
        LivingEntity target = null;
        for(int i = 0; i < 40; i++) {
            LivingEntity closestEntity = Misc.getMobPlayerClosest(testLocation, 1, player);
            if(closestEntity == null) {
                testLocation.add(player.getLocation().getDirection().multiply(0.5));
                continue;
            }
            target = closestEntity;
            break;
        }

        if(target == null) {
            event.setCancelled(true);
            return;
        }

        List<Entity> excluded = new ArrayList<>();
        excluded.add(player);
        excluded.add(target);
        Map<PitEnchant, Integer> attackerEnchantMap = EnchantManager.getEnchantsOnPlayer(player);
        chain(player, attackerEnchantMap, player, target, new MutableInteger(0), getMaxBounces(event.getSpellLevel()), excluded);
        Sounds.ELECTRIC_SHOCK.play(player);
    }

    private static void chain(Player player, Map<PitEnchant, Integer> attackerEnchantMap, LivingEntity startingEntity,
                              LivingEntity endingEntity, MutableInteger currentBounce, int maxBounces, List<Entity> excluded) {
        Location startLocation = startingEntity.getLocation().add(0, startingEntity.getEyeHeight(), 0);
        Location endLocation = endingEntity.getLocation().add(0, endingEntity.getEyeHeight(), 0);
        double distance = startLocation.distance(endLocation);
        if(distance > getBounceRange() && currentBounce.getValue() != 0) return;
        int steps = (int) Math.ceil(distance * 4);
        double stepSize = distance / steps;
        Vector stepVector = endLocation.toVector().subtract(startLocation.toVector()).normalize().multiply(stepSize);

        Location drawLocation = startLocation.clone();
        for(int i = 0; i <= steps; i++) {
            if(i >= 2 || currentBounce.getValue() != 0) {
                if(currentBounce.getValue() != 0 && Math.random() < 0.07) littleSpark(drawLocation.clone());
                drawEffect(drawLocation);
            }
            drawLocation.add(stepVector);
        }

        double damageMultiplier = 1 + 0.5 * ((maxBounces - currentBounce.getValue()) / (double) maxBounces * 3);
        DamageManager.createIndirectAttack(player, endingEntity, DarkzoneBalancing.SCYTHE_DAMAGE * damageMultiplier,
                attackerEnchantMap, null);

        for(LivingEntity possibleTarget : getPossibleTargets(endLocation, excluded)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(currentBounce.getValue() >= maxBounces) return;
                    currentBounce.increment();
                    chain(player, attackerEnchantMap, endingEntity, possibleTarget, currentBounce, maxBounces, excluded);
                }
            }.runTaskLater(Thepit.INSTANCE, new Random().nextInt(9));
        }
    }

    private static List<LivingEntity> getPossibleTargets(Location location, List<Entity> excluded) {
        List<LivingEntity> possibleTargets = new ArrayList<>();
        for(Entity entity : location.getWorld().getNearbyEntities(location, getBounceRange(), getBounceRange(), getBounceRange())) {
            if(!Misc.isValidMobPlayerTarget(entity) || excluded.contains(entity) ||
                    entity.getLocation().distance(location) > getBounceRange()) continue;
            LivingEntity livingEntity = (LivingEntity) entity;
            possibleTargets.add(livingEntity);
            excluded.add(livingEntity);
        }
        return possibleTargets;
    }

    private static void littleSpark(Location location) {
        Vector vector = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize();
        int length = new Random().nextInt(5) + 3;
        for(int i = 0; i < length; i++) {
            location.add(vector.clone().multiply(0.25));
            drawEffect(location);
        }
    }

    private static void drawEffect(Location location) {
        for(Entity nearbyEntity : location.getWorld().getNearbyEntities(location, 25, 25, 25)) {
            if(!(nearbyEntity instanceof Player)) continue;
            EntityPlayer entityPlayer = ((CraftPlayer) nearbyEntity).getHandle();
            new FireworkSparkParticle().display(entityPlayer, location);
        }
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Right-Clicking while looking at a mob casts this spell for &b" + getManaCost(enchantLvl) +
                        " mana&7, shooting an electric beam that chains between mobs up to &e" +
                        getMaxBounces(enchantLvl) + "[]time" + (getMaxBounces(enchantLvl) == 1 ? "" : "s")
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "shoots an electric beam that can chain between targets";
    }

    @Override
    public int getManaCost(int enchantLvl) {
        return 25;
    }

    @Override
    public int getCooldownTicks(int enchantLvl) {
        return 20;
    }

    public static int getMaxBounces(int enchantLvl) {
        return enchantLvl + 1;
    }

    public static double getBounceRange() {
        return 12;
    }
}
