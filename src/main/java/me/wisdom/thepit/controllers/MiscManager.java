package me.wisdom.thepit.controllers;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enchants.overworld.BulletTime;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.storage.EnderchestGUI;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUIManager;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDate;
import java.util.UUID;

public class MiscManager implements Listener {

    public MiscManager() {
        ItemStack back = new AItemStackBuilder(Material.ARROW)
                .setName("&a返回")
                .setLore(new ALoreBuilder(
                        "&7点击以打开",
                        "&7上一屏"
                )).getItemStack();
        ItemStack previousPage = new AItemStackBuilder(Material.ARROW)
                .setName("&a上一页")
                .setLore(new ALoreBuilder(
                        "&7点击以前往",
                        "&7上一页"
                )).getItemStack();
        ItemStack nextPage = new AItemStackBuilder(Material.ARROW)
                .setName("&a下一页")
                .setLore(new ALoreBuilder(
                        "&7点击以前往",
                        "&7下一页"
                )).getItemStack();
        AGUIManager.setDefaultItemStacks(back, previousPage, nextPage);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if(block.getType() != Material.ENDER_CHEST) return;
        event.setCancelled(true);

        if(ShutdownManager.enderchestDisabled | pitPlayer.darkzoneTutorial.isActive()) {
            AOutput.error(player, "&c&l错误!&7 你现在无法打开末影箱!");
            return;
        }

        EnderchestGUI gui = new EnderchestGUI(player, player.getUniqueId());
        gui.open();
        Sounds.ENDERCHEST_OPEN.play(player);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Fireball)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent event) {
        BlockIgniteEvent.IgniteCause cause = event.getCause();
        if(cause == BlockIgniteEvent.IgniteCause.FIREBALL || cause == BlockIgniteEvent.IgniteCause.EXPLOSION)
            event.setCancelled(true);
    }

    @EventHandler
    public void onIgnite(BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAttack(AttackEvent.Pre attackEvent) {
        if(!PlayerManager.isRealPlayer(attackEvent.getAttackerPlayer()) || !PlayerManager.isRealPlayer(attackEvent.getDefenderPlayer())) return;
        if(LocalDate.now().isAfter(LocalDate.parse("2023-03-10"))) return;
        Player attacker = attackEvent.getAttackerPlayer();
        Player defender = attackEvent.getDefenderPlayer();
//		if(!Misc.isKyro(defender.getUniqueId()) || !attacker.getUniqueId().equals(UUID.fromString("ee660496-3cf1-458a-94fb-e11764c18663"))) return;
        if(!defender.getUniqueId().equals(UUID.fromString("ee660496-3cf1-458a-94fb-e11764c18663"))) return;
        attackEvent.getDefenderEnchantMap().remove(BulletTime.INSTANCE);

//		attackEvent.selfVeryTrueDamage += 100;
//		AOutput.send(attacker, "&9&lCOPE!");
    }
}
