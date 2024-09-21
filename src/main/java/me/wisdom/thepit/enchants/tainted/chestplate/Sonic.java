package me.wisdom.thepit.enchants.tainted.chestplate;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.ManaRegenEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public
class Sonic extends PitEnchant {
    public static Sonic INSTANCE;
    public static List<Player> sonicDisabledList = new ArrayList<>();

    public Sonic() {
        super("Sonic", true, ApplyType.CHESTPLATES,
                "sonic", "fast");
        isTainted = true;
        INSTANCE = this;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAttack(AttackEvent.Apply attackEvent) {
        int attackerEnchantLevel = attackEvent.getAttackerEnchantLevel(this);
        int defenderEnchantLevel = attackEvent.getDefenderEnchantLevel(this);

        if(attackerEnchantLevel != 0) {
            sonicDisabledList.add(attackEvent.getAttackerPlayer());
            new BukkitRunnable() {
                @Override
                public void run() {
                    sonicDisabledList.remove(attackEvent.getAttackerPlayer());
                    attackEvent.getAttackerPitPlayer().updateWalkingSpeed();
                }
            }.runTaskLater(Thepit.INSTANCE, getDisableCooldownSeconds(attackerEnchantLevel) * 20L);
            attackEvent.getAttackerPitPlayer().updateWalkingSpeed();
        }

        if(defenderEnchantLevel != 0) {
            sonicDisabledList.add(attackEvent.getDefenderPlayer());
            new BukkitRunnable() {
                @Override
                public void run() {
                    sonicDisabledList.remove(attackEvent.getDefenderPlayer());
                    attackEvent.getDefenderPitPlayer().updateWalkingSpeed();
                }
            }.runTaskLater(Thepit.INSTANCE, getDisableCooldownSeconds(defenderEnchantLevel) * 20L);
            attackEvent.getDefenderPitPlayer().updateWalkingSpeed();
        }
    }

    @EventHandler
    public void onManaRegen(ManaRegenEvent event) {
        Player player = event.getPlayer();
        int enchantLvl = EnchantManager.getEnchantLevel(player, this);
        if(enchantLvl == 0) return;
        event.multipliers.add(Misc.getReductionMultiplier(getManaReduction(enchantLvl)));
    }

    public static float getWalkSpeedIncrease(PitPlayer pitPlayer) {
        if(!INSTANCE.isEnabled() || !pitPlayer.hasManaUnlocked() || sonicDisabledList.contains(pitPlayer.player)) return 0;

        int enchantLvl = EnchantManager.getEnchantLevel(pitPlayer.player, INSTANCE);
        if(enchantLvl == 0) return 0;

        return getWalkSpeedIncrease(enchantLvl);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Move &e" + getWalkSpeedIncrease(enchantLvl) + "% &7faster. When attacking or being attacked, " +
                        "this enchant disables for " + getDisableCooldownSeconds(enchantLvl) +
                        " second" + Misc.s(getDisableCooldownSeconds(enchantLvl)) +
                        ". When worn, regain mana &b" + getManaReduction(enchantLvl) + "% &7slower"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "substantially increases your movement speed";
    }

    public static int getDisableCooldownSeconds(int enchantLvl) {
        return 3;
    }

    public static int getWalkSpeedIncrease(int enchantLvl) {
        return 100;
    }

    public static int getManaReduction(int enchantLvl) {
        return Math.max(130 - enchantLvl * 30, 0);
    }
}
