package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.*;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Telebow extends PitEnchant {
    public static Telebow INSTANCE;
    public static List<Arrow> teleShots = new ArrayList<>();

    public Telebow() {
        super("Telebow", true, ApplyType.BOWS,
                "telebow", "tele");
        INSTANCE = this;
    }

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<Arrow> toRemove = new ArrayList<>();
                for(Arrow arrow : teleShots) {
                    if(arrow.isDead()) {
                        toRemove.add(arrow);
                        continue;
                    }
                    for(int j = 0; j < 10; j++)
                        arrow.getWorld().playEffect(arrow.getLocation(), Effect.POTION_SWIRL, 0, 30);
                }
                toRemove.forEach(teleShots::remove);
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 1L);
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isAttackerPlayer()) return;
        if(!canApply(attackEvent)) return;
        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;
        if(attackEvent.getArrow() == null) return;

        Cooldown cooldown = getCooldown(attackEvent.getAttackerPlayer(), getCooldown(enchantLvl) * 20);
        cooldown.addModifier(Cooldown.CooldownModifier.TELEBOW);
        if(CooldownManager.hasModifier(attackEvent.getAttackerPlayer(), Cooldown.CooldownModifier.TELEBOW)) {
            AOutput.error(attackEvent.getAttackerPlayer(), "&c&lPINNED! Your telebow cooldown cannot be reduced right now!");
            Sounds.PIN_DOWN.play(attackEvent.getAttackerPlayer());
            return;
        } else {
            cooldown.reduceCooldown(40);
        }

        if(cooldown.isOnCooldown()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    ActionBarManager.sendActionBar(attackEvent.getAttackerPlayer(), "&eTelebow: &c" + cooldown.getTicksLeft() / 20 + "s cooldown!");
                }
            }.runTaskLater(Thepit.INSTANCE, 1L);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    ActionBarManager.sendActionBar(attackEvent.getAttackerPlayer(), "&eTelebow: &aReady!");
                }
            }.runTaskLater(Thepit.INSTANCE, 1L);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBowShoot(EntityShootBowEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getProjectile() instanceof Arrow)) return;

        Player player = ((Player) event.getEntity()).getPlayer();
        Arrow arrow = (Arrow) event.getProjectile();

        int enchantLvl = EnchantManager.getEnchantLevel(player, this);
        if(enchantLvl == 0 || !player.isSneaking()) return;

        Cooldown cooldown = getCooldown(player, getCooldown(enchantLvl) * 20);
        cooldown.addModifier(Cooldown.CooldownModifier.TELEBOW);
        if(cooldown.isOnCooldown()) {
            if(player.isSneaking())
                ActionBarManager.sendActionBar(player, "&eTelebow: &c" + cooldown.getTicksLeft() / 20 + "&cs cooldown!");
            return;
        }
        if(cooldown.isOnCooldown()) return;
        else cooldown.restart();

        if(player.isSneaking() && !SpawnManager.isInSpawn(player)) teleShots.add(arrow);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if(!(event.getEntity() instanceof Arrow) || !(event.getEntity().getShooter() instanceof Player)) return;
        Player player = (Player) event.getEntity().getShooter();

        if(teleShots.size() == 0) return;
        for(Arrow teleShot : teleShots) {
            Arrow teleArrow = (Arrow) event.getEntity();
            if(!teleShot.equals(event.getEntity()) || !teleArrow.equals(teleShot)) continue;

            Location teleportLoc = teleArrow.getLocation().clone();
            teleportLoc.setYaw(-teleArrow.getLocation().getYaw());
            teleportLoc.setPitch(-teleArrow.getLocation().getPitch());

            if(Thepit.status.isOverworld() && MapManager.currentMap.world == teleportLoc.getWorld()) {
                Location midTeleportLoc = teleportLoc.clone();
                midTeleportLoc.setY(MapManager.currentMap.getY());
                if(midTeleportLoc.getWorld() == teleportLoc.getWorld()) {
                    double distance = MapManager.currentMap.getMid().distance(midTeleportLoc);
                    if(distance < 12) {
                        AOutput.error(player, "You are not allowed to telebow into mid");
                        teleShots.remove(teleShot);
                        return;
                    }
                }
            }

            if(SpawnManager.isInSpawn(teleportLoc)) {
                AOutput.error(player, "You are not allowed to telebow into spawn");
                teleShots.remove(teleShot);
                return;
            }

            player.teleport(teleportLoc);
            Sounds.TELEBOW.play(teleArrow.getLocation());

            teleShots.remove(teleShot);

            PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
            pitPlayer.stats.telebow++;
            return;
        }
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Sneak to shoot a teleportation arrow (" + getCooldown(enchantLvl) + "s cooldown, -2s per bow hit)"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that allows you " +
                "to shoot an arrow that teleports you to landing (this is done by sneaking when shooting)";
    }

    public static int getCooldown(int enchantLvl) {

        switch(enchantLvl) {
            case 1:
                return 90;
            case 2:
                return 45;
            case 3:
                return 25;
            case 4:
                return 10;
            case 5:
                return 3;
            case 6:
                return 1;
        }

        return 0;
    }
}
