package me.wisdom.thepit.items.misc;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.ActionBarManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.MarketCategory;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.events.HealEvent;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.events.PitQuitEvent;
import me.wisdom.thepit.events.PlayerSpawnCommandEvent;
import me.wisdom.thepit.items.StaticPitItem;
import me.wisdom.thepit.items.TemporaryItem;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;

public class VeryYummyBread extends StaticPitItem implements TemporaryItem {
    public static Map<UUID, Integer> breadCooldownLength = new HashMap<>();
    public static Map<UUID, Integer> breadCooldown = new HashMap<>();
    public static DecimalFormat breadCooldownFormat = new DecimalFormat("0.0");

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Map.Entry<UUID, Integer> entry : new HashMap<>(breadCooldown).entrySet()) {
                    if(entry.getValue() <= 1) {
                        breadCooldown.remove(entry.getKey());
                    } else {
                        breadCooldown.put(entry.getKey(), entry.getValue() - 1);
                    }
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 1L);
    }

    public VeryYummyBread() {
        marketCategory = MarketCategory.MISC;
    }

    @Override
    public String getNBTID() {
        return "very-yummy-bread";
    }

    @Override
    public List<String> getRefNames() {
        return new ArrayList<>(Arrays.asList("veryyummybread"));
    }

    @Override
    public Material getMaterial() {
        return Material.BREAD;
    }

    @Override
    public String getName() {
        return "&6Very Yummy Bread";
    }

    @Override
    public List<String> getLore() {
        return new ALoreBuilder(
                "&7Heals &c" + Misc.getHearts(getHealing()),
                "&7Grants &6" + Misc.getHearts(getAbsorption()),
                "",
                "&cLost on death"
        ).getLore();
    }

    @EventHandler
    public void onEat(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if(!breadCooldown.containsKey(player.getUniqueId())) {
            player.setFoodLevel(19);
            return;
        }

        player.setFoodLevel(20);
        if(!isThisItem(itemStack)) return;

        event.setCancelled(true);
        int cooldownTicks = breadCooldown.get(player.getUniqueId());
        ActionBarManager.sendActionBar(player, "&6Bread: &c" + breadCooldownFormat.format(cooldownTicks / 20.0) + "s cooldown!");
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        player.setFoodLevel(19);

        if(!isThisItem(itemStack)) return;

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        pitPlayer.heal(12);
        pitPlayer.heal(6, HealEvent.HealType.ABSORPTION, 20);
        int newCooldown = breadCooldownLength.getOrDefault(player.getUniqueId(), 0) + 10;
        breadCooldownLength.put(player.getUniqueId(), newCooldown);
        breadCooldown.put(player.getUniqueId(), newCooldown);
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!killEvent.isDeadPlayer()) return;
        breadCooldownLength.remove(killEvent.getDeadPlayer().getUniqueId());
        breadCooldown.remove(killEvent.getDeadPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PitQuitEvent event) {
        Player player = event.getPlayer();
        breadCooldownLength.remove(player.getUniqueId());
        breadCooldown.remove(player.getUniqueId());
    }

    @EventHandler
    public void onSpawn(PlayerSpawnCommandEvent event) {
        Player player = event.getPlayer();
        breadCooldownLength.remove(player.getUniqueId());
        breadCooldown.remove(player.getUniqueId());
    }

    public int getHealing() {
        return 12;
    }

    public int getAbsorption() {
        return 4;
    }

    @Override
    public boolean isLegacyItem(ItemStack itemStack, NBTItem nbtItem) {
        return nbtItem.hasKey(NBTTag.IS_VERY_YUMMY_BREAD.getRef());
    }

    @Override
    public TemporaryItem.TemporaryType getTemporaryType() {
        return TemporaryItem.TemporaryType.LOST_ON_DEATH;
    }
}
