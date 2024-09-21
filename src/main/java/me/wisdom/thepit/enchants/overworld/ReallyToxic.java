package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.ChatTriggerManager;
import me.wisdom.thepit.controllers.HitCounter;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.HealEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReallyToxic extends PitEnchant {
    public static ReallyToxic INSTANCE;
    public static List<UUID> toxicNotifCooldown = new ArrayList<>();

    public ReallyToxic() {
        super("Really Toxic", false, ApplyType.PANTS,
                "reallytoxic", "really-toxic", "toxic", "rt");
        isUncommonEnchant = true;
        INSTANCE = this;
    }

    @EventHandler
    public void onHeal(HealEvent event) {
        int charge = HitCounter.getCharge(event.player, this);
        if(charge == 0) return;

        int reduction = Math.min(charge, getMaxReduction());
        event.multipliers.add(Misc.getReductionMultiplier(reduction));
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {

        if(attackEvent.isAttackerPlayer()) {
            int attackerCharge = HitCounter.getCharge(attackEvent.getAttackerPlayer(), this);
            if(attackerCharge != 0 && !toxicNotifCooldown.contains(attackEvent.getAttacker().getUniqueId())) {
                AOutput.send(attackEvent.getAttacker(), "&a&lTOXIC!&f You heal &a" + Math.min(attackerCharge, getMaxReduction()) + "% &aless");
                toxicNotifCooldown.add(attackEvent.getAttacker().getUniqueId());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        toxicNotifCooldown.remove(attackEvent.getAttacker().getUniqueId());
                    }
                }.runTaskLater(Thepit.INSTANCE, 40L);
            }
        }

        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        if(attackEvent.isDefenderPlayer()) {
            int charge = HitCounter.getCharge(attackEvent.getDefenderPlayer(), this);
            HitCounter.setCharge(attackEvent.getDefenderPlayer(), this, charge + getReductionPerHit(enchantLvl));
            ChatTriggerManager.sendToxicInfo(attackEvent.getDefenderPitPlayer());

            PitEnchant thisEnchant = this;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(!attackEvent.getDefenderPlayer().isOnline()) return;
                    int charge = HitCounter.getCharge(attackEvent.getDefenderPlayer(), thisEnchant);
                    HitCounter.setCharge(attackEvent.getDefenderPlayer(), thisEnchant, charge - getReductionPerHit(enchantLvl));
                    ChatTriggerManager.sendToxicInfo(attackEvent.getDefenderPitPlayer());
                }
            }.runTaskLater(Thepit.INSTANCE, getStackTime() * 20);
        }
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Strikes apply 1 stack of &aToxicity&7. Stacks last &4" + getStackTime() +
                        "s &7and reduce all healing by &a" + getReductionPerHit(enchantLvl) + "%&7, up to &a" +
                        getMaxReduction() + "%"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that temporarily " +
                "decreases the amount of healing your opponents receive when you hit them";
    }

    public static int getReductionPerHit(int enchantLvl) {
        return enchantLvl;
    }

    public static int getMaxReduction() {
        return 25;
    }

    public static long getStackTime() {
        return 5;
    }
}
