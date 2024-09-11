package me.wisdom.thepit.enchants.tainted.chestplate;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.DamageManager;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inferno extends PitEnchant {
    public static Inferno INSTANCE;
    public static Map<LivingEntity, BukkitTask> fireDamageMap = new HashMap<>();

    public Inferno() {
        super("Inferno", true, ApplyType.CHESTPLATES,
                "inferno");
        isTainted = true;
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!fireDamageMap.containsKey(killEvent.getDead())) return;
        fireDamageMap.remove(killEvent.getDead()).cancel();
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isAttackerPlayer()) return;
        if(!canApply(attackEvent)) return;
        if(attackEvent.getAttacker() == attackEvent.getDefender() || attackEvent.getArrow() != null) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        if(attackEvent.getDefender().getFireTicks() > 0 || fireDamageMap.containsKey(attackEvent.getDefender())) return;

        if(!attackEvent.getAttackerPitPlayer().useManaForSpell(getManaCost(enchantLvl))) {
            Sounds.NO.play(attackEvent.getAttackerPlayer());
            return;
        }

        attackEvent.getDefender().setFireTicks(20 * getFireSeconds(enchantLvl));
        BukkitTask runnable = new BukkitRunnable() {
            int count = 1;
            final int total = getFireSeconds(enchantLvl);
            @Override
            public void run() {
                if(count == total) {
                    cancel();
                    fireDamageMap.remove(attackEvent.getDefender());
                    return;
                }
                DamageManager.createIndirectAttack(null, attackEvent.getDefender(), getDamage(enchantLvl));
                count++;
            }
        }.runTaskTimer(Thepit.INSTANCE, 20L, 20);
        fireDamageMap.put(attackEvent.getDefender(), runnable);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Strikes set your enemies &6ablaze &7for " + getFireSeconds(enchantLvl) + " seconds but costs &b" +
                        getManaCost(enchantLvl) + " mana"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "sets your opponents &6ablaze";
    }

    public static int getDamage(int enchantLvl) {
        return 8;
    }

    public static int getManaCost(int enchantLvl) {
        return 14;
    }

    public static int getFireSeconds(int enchantLvl) {
        return enchantLvl + 2;
    }
}
