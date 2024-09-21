package me.wisdom.thepit.helmetabilities;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.HelmetAbility;
import me.wisdom.thepit.controllers.objects.HelmetManager;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class HermitAbility extends HelmetAbility {
    public BukkitTask runnable;

    public HermitAbility(Player player) {

        super(player, "Hermit", "hermit", true, 14);
        cost = 1500;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!isActive || player != attackEvent.getDefender()) return;
        attackEvent.trueDamage = 0;
    }

    @Override
    public void onActivate() {
        runnable = new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                ItemStack goldenHelmet = HelmetManager.getHelmet(player);
                assert goldenHelmet != null;
                if(!HelmetManager.withdrawGold(player, goldenHelmet, cost)) {
                    AOutput.error(player, "&cNot enough gold!");
                    HelmetManager.deactivate(player);
                    Sounds.NO.play(player);
                } else {
                    Sounds.HELMET_TICK.play(player);
                    if(count++ % 2 == 0) {
                        Misc.applyPotionEffect(player, PotionEffectType.SLOW, 100, 0, true, false);
                        Misc.applyPotionEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 100, 0, true, false);
                    }
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 20L, 20);
        Sounds.HELMET_ACTIVATE.play(player);
        AOutput.send(player, "&6&lGOLDEN HELMET! &aActivated &9Hermit&7. (&6-1,500g&7 per second)");
    }

    @Override
    public boolean shouldActivate() {
        ItemStack goldenHelmet = HelmetManager.getHelmet(player);
        assert goldenHelmet != null;
        if(!HelmetManager.withdrawGold(player, goldenHelmet, cost * 20)) {
            AOutput.error(player, "&cNot enough gold!");
            Sounds.NO.play(player);
            return false;
        }
        return true;
    }

    @Override
    public void onDeactivate() {
        runnable.cancel();
        AOutput.send(player, "&6&lGOLDEN HELMET! &cDeactivated &9Hermit&c.");
    }

    @Override
    public void onProc() {
    }

    @Override
    public List<String> getDescription() {
        DecimalFormat formatter = new DecimalFormat("#,###.#");
        return Arrays.asList("&7Double-Sneak to toggle Hermit.", "&7Receive permanent resistance II,", "&7slowness I, and true damage immunity", "",
                "&7Cost: &6" + formatter.format(cost * 20L) + "g &7on activation", "&7Cost: &6" + formatter.format(cost) + "g &7per second");
    }

    @Override
    public ItemStack getDisplayStack() {
        AItemStackBuilder builder = new AItemStackBuilder(Material.BEDROCK);
        builder.setName("&e" + name);
        ALoreBuilder loreBuilder = new ALoreBuilder();
        loreBuilder.addLore(getDescription());
        builder.setLore(loreBuilder);

        return builder.getItemStack();
    }
}
