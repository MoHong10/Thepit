package me.wisdom.thepit.helmetabilities;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.SpawnManager;
import me.wisdom.thepit.controllers.objects.HelmetAbility;
import me.wisdom.thepit.controllers.objects.HelmetManager;
import me.wisdom.thepit.controllers.objects.Non;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.HealEvent;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.events.PitQuitEvent;
import me.wisdom.thepit.megastreaks.RNGesus;
import me.wisdom.thepit.megastreaks.Uberstreak;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PhoenixAbility extends HelmetAbility {
    public static List<UUID> alreadyActivatedList = new ArrayList<>();
    public static int cost = 40_000;

    public PhoenixAbility(Player player) {

        super(player, "Phoenix", "phoenix", false, 13);
    }

    @EventHandler
    public static void onKill(KillEvent killEvent) {
        if(killEvent.isDeadPlayer()) alreadyActivatedList.remove(killEvent.getDeadPlayer().getUniqueId());

        if(!killEvent.isKillerPlayer() || !killEvent.isDeadPlayer() ||
                !Bukkit.getOnlinePlayers().contains(killEvent.getKillerPlayer()) || !Bukkit.getOnlinePlayers().contains(killEvent.getDeadPlayer()))
            return;
        alreadyActivatedList.remove(killEvent.getKillerPlayer().getUniqueId());
    }

    @EventHandler
    public static void onLogout(PitQuitEvent event) {
        alreadyActivatedList.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public static void onHeal(HealEvent healEvent) {
        if(!alreadyActivatedList.contains(healEvent.player.getUniqueId())) return;
        healEvent.multipliers.add(0D);
    }

    @Override
    public void onProc() {
        ItemStack goldenHelmet = HelmetManager.getHelmet(player);

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(pitPlayer.getMegastreak() instanceof RNGesus) {
            AOutput.error(player, "&c&lERROR!&7 You cannot do this while &e&lRNGESUS&7 is equipped");
            Sounds.NO.play(player);
            return;
        }

        if(alreadyActivatedList.contains(player.getUniqueId())) {
            AOutput.error(player, "&cAbility has already been used!");
            Sounds.NO.play(player);
            return;
        }

        assert goldenHelmet != null;
        if(!HelmetManager.withdrawGold(player, goldenHelmet, cost)) {
            AOutput.error(player, "&cNot enough gold!");
            Sounds.NO.play(player);
            return;
        }

        Misc.applyPotionEffect(player, PotionEffectType.INCREASE_DAMAGE, 200, 0, true, false);

        pitPlayer.heal(player.getMaxHealth());
        pitPlayer.heal(player.getMaxHealth() * 2, HealEvent.HealType.ABSORPTION, (int) player.getMaxHealth() * 2);
        alreadyActivatedList.add(player.getUniqueId());
        if(!SpawnManager.isInSpawn(player)) {
            for(Entity entity : player.getNearbyEntities(5, 5, 5)) {
                if(!(entity instanceof Player)) continue;
                Player target = (Player) entity;
                Non non = NonManager.getNon(target);

                if(target == player) continue;
                PitPlayer pitTarget = PitPlayer.getPitPlayer(target);
                if(non == null) {
                    if(pitTarget.getMegastreak() instanceof Uberstreak && pitTarget.isOnMega()) continue;
                    if(SpawnManager.isInSpawn(target)) continue;
                    Vector force = target.getLocation().toVector().subtract(player.getLocation().toVector())
                            .setY(1).normalize().multiply(1.15);
                    target.setVelocity(force);
                }
            }
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        AOutput.send(player, "&6&lGOLDEN HELMET!&7 Used &9Phoenix&7! (&6-" + decimalFormat.format(cost) + "g&7)");
        Sounds.PHOENIX.play(player.getLocation(), 15);

        World world = player.getWorld();
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if(count++ == 5) {
                    cancel();
                    return;
                }

                for(int i = 0; i < 50; i++) {
                    double x = player.getLocation().getX() + Math.random() * 20 - 10;
                    double y = player.getLocation().getY() + Math.random() * 12 - 2;
                    double z = player.getLocation().getZ() + Math.random() * 20 - 10;

                    Location particleLoc = new Location(world, x, y, z);
                    double distance = particleLoc.distance(player.getLocation());
                    if(Math.min(20 - distance, 10) > Math.random() * 20) continue;

                    world.playEffect(particleLoc, Effect.LAVA_POP, 1);
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 1);
    }

    @Override
    public List<String> getDescription() {
        DecimalFormat formatter = new DecimalFormat("#,###.#");
        return Arrays.asList("&7Double-Sneak to rebirth:",
                "&a\u25a0 &7Heal to &cfull HP",
                "&a\u25a0 &cStrength I &7(10s)",
                "&a\u25a0 &7Gain &6absorption &7equal to",
                "&72x your max hp",
                "&c\u25a0 &7You cannot heal until you die,",
                "&7spawn, or get a player kill",
                "", "&7Cost: &6" + formatter.format(cost) + "g");
    }

    @Override
    public ItemStack getDisplayStack() {
        AItemStackBuilder builder = new AItemStackBuilder(Material.BLAZE_POWDER);
        builder.setName("&e" + name);
        ALoreBuilder loreBuilder = new ALoreBuilder();
        loreBuilder.addLore(getDescription());
        builder.setLore(loreBuilder);

        return builder.getItemStack();
    }

    @Override
    public void onActivate() {
    }

    @Override
    public boolean shouldActivate() {
        return false;
    }

    @Override
    public void onDeactivate() {
    }
}
