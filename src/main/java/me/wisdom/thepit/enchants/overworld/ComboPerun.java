package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.HitCounter;
import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockIgniteEvent;

import java.util.List;

public class ComboPerun extends PitEnchant {
    public static ComboPerun INSTANCE;

    public ComboPerun() {
        super("Combo: Perun's Wrath", true, ApplyType.MELEE,
                "perun", "lightning");
        INSTANCE = this;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isAttackerPlayer()) return;
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;
        if(attackEvent.isFakeHit()) return;

        int regLvl = attackEvent.getAttackerEnchantLevel(Regularity.INSTANCE);
        if(Regularity.isRegHit(attackEvent.getDefender()) && Regularity.skipIncrement(regLvl)) return;

        PitPlayer pitPlayer = attackEvent.getAttackerPitPlayer();
        HitCounter.incrementCounter(pitPlayer.player, this);
        if(!HitCounter.hasReachedThreshold(pitPlayer.player, this, enchantLvl == 3 ? 4 : getStrikes(enchantLvl)))
            return;

        pitPlayer.stats.perun++;

        if(enchantLvl == 3) {
            int damage = 2;
            if(!(attackEvent.getDefender().getEquipment().getHelmet() == null) && attackEvent.getDefender().getEquipment().getHelmet().getType() == Material.DIAMOND_HELMET) {
                damage += 1;
            }
            if(!(attackEvent.getDefender().getEquipment().getChestplate() == null) && attackEvent.getDefender().getEquipment().getChestplate().getType() == Material.DIAMOND_CHESTPLATE) {
                damage += 1;
            }
            if(!(attackEvent.getDefender().getEquipment().getLeggings() == null) && attackEvent.getDefender().getEquipment().getLeggings().getType() == Material.DIAMOND_LEGGINGS) {
                damage += 1;
            }
            if(!(attackEvent.getDefender().getEquipment().getBoots() == null) && attackEvent.getDefender().getEquipment().getBoots().getType() == Material.DIAMOND_BOOTS) {
                damage += 1;
            }

            attackEvent.trueDamage += damage;
        } else {
            double damage = 2;
            if(NonManager.getNon(attackEvent.getDefender()) != null) damage += getTrueDamage(enchantLvl);

            attackEvent.trueDamage += damage;
        }

        Misc.strikeLightningForPlayers(attackEvent.getDefender().getLocation(), 10);
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent event) {
        if(event.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING) event.setCancelled(true);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {

        if(enchantLvl == 3) {
            return new PitLoreBuilder(
                    "&7Every &efourth &7hit strikes &elightning &7for &c" + Misc.getHearts(2) + " &7+ &c" +
                            Misc.getHearts(1) + " &7per &bdiamond piece &7on your victim. (Lightning deals true damage)"
            ).getLore();
        }

        return new PitLoreBuilder(
                "&7Every &e" + Misc.ordinalWords(getStrikes(enchantLvl)) + " &7hit strikes &elightning &7for &c" +
                        Misc.getHearts(2) + " &7+ &c" + Misc.getHearts(getTrueDamage(enchantLvl)) +
                        " &7if the victim is a non (Lightning deals true damage)"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that strikes " +
                "lightning every few strikes (the lightning does true damage)";
    }

    public double getTrueDamage(int enchantLvl) {
        return enchantLvl + 2;
    }

    public int getStrikes(int enchantLvl) {
        return Math.max(Misc.linearEnchant(enchantLvl, -0.5, 5), 1);
    }
}
