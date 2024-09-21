package me.wisdom.thepit.enchants.tainted.scythe;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.DamageManager;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitEnchantSpell;
import me.wisdom.thepit.events.SpellUseEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MeteorSpell extends PitEnchantSpell {

    public MeteorSpell() {
        super("Meteor",
                "meteor");
        isTainted = true;
    }

    @EventHandler
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

        double xOffset = Misc.randomOffset(10);
        double zOffset = Misc.randomOffset(10);
        int steps = 30;
        Location targetLocation = target.getLocation();
        Location effectLocation = targetLocation.clone().add(xOffset, 15, zOffset);
        Vector step = effectLocation.toVector().subtract(targetLocation.toVector()).multiply(-1.0 / steps);

        Map<PitEnchant, Integer> attackerEnchantMap = EnchantManager.getEnchantsOnPlayer(player);

        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                if(count == steps) {
                    cancel();
                    for(int i = 0; i < 10; i++) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Sounds.METEOR_2.play(effectLocation);
                            }
                        }.runTaskLater(Thepit.INSTANCE, new Random().nextInt(5));
                    }
                    for(int i = 0; i < 4; i++) effectLocation.getWorld().playEffect(effectLocation.clone()
                            .add(Misc.randomOffset(10), Misc.randomOffset(2), Misc.randomOffset(10)), Effect.EXPLOSION_HUGE, 1);

                    for(Entity entity : effectLocation.getWorld().getNearbyEntities(effectLocation, 3, 3, 3)) {
                        if(!Misc.isValidMobPlayerTarget(entity, player)) continue;
                        LivingEntity livingEntity = (LivingEntity) entity;

                        double distance = livingEntity.getLocation().distance(effectLocation);
                        if(distance > 3) continue;

                        DamageManager.createIndirectAttack(player, livingEntity, getDamage(event.getSpellLevel()),
                                attackerEnchantMap, null);
                    }
                } else {
                    effectLocation.getWorld().playEffect(effectLocation, Effect.EXPLOSION_LARGE, 1);
                    effectLocation.getWorld().playEffect(effectLocation, Effect.LARGE_SMOKE, 1);
                    effectLocation.getWorld().playEffect(effectLocation, Effect.PARTICLE_SMOKE, 1);
                    effectLocation.add(step);
                    if(count % 2 == 0) Sounds.METEOR.play(effectLocation);
                }

                count++;
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 1L);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Right-Clicking casts this spell for &b" + getManaCost(enchantLvl) + " mana&7, " +
                        "&7summoning a meteor that causes large damage to a single target (" + getCooldownSeconds(enchantLvl) + "s cooldown)"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "summons a meteor that deals a very high amount of damage";
    }

    @Override
    public int getManaCost(int enchantLvl) {
        return Math.max(72 - enchantLvl * 8, 0);
    }

    @Override
    public int getCooldownTicks(int enchantLvl) {
        return getCooldownSeconds(enchantLvl) * 20;
    }

    public static int getCooldownSeconds(int enchantLvl) {
        return 15;
    }

    public static int getDamage(int enchantLvl) {
        return 75;
    }
}
