package me.wisdom.thepit.controllers;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.darkzone.DarkzoneBalancing;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.items.mystics.TaintedScythe;
import me.wisdom.thepit.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TaintedManager implements Listener {
    public static List<Player> players = new ArrayList<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isAttackerRealPlayer() || attackEvent.getWrapperEvent().hasAttackInfo() ||
                attackEvent.getFireball() != null) return;

        ItemStack held = attackEvent.getAttackerPlayer().getItemInHand();
        PitItem pitItem = ItemFactory.getItem(TaintedScythe.class);
        if(!pitItem.isThisItem(held)) return;
        double multiplier = Misc.isCritical(attackEvent.getAttackerPlayer()) ? 1.5 : 1;
        attackEvent.getWrapperEvent().getSpigotEvent().setDamage(DarkzoneBalancing.SCYTHE_DAMAGE * multiplier);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if(players.contains(event.getPlayer())) return;
        if(!Bukkit.getOnlinePlayers().contains(event.getPlayer())) return;
        players.add(event.getPlayer());

        new BukkitRunnable() {
            @Override
            public void run() {
                players.remove(event.getPlayer());
            }
        }.runTaskLater(Thepit.INSTANCE, 40);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if(players.contains((Player) event.getPlayer())) event.setCancelled(true);
    }
}
