package me.wisdom.thepit.enchants.tainted.uncommon;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Adrenaline extends PitEnchant {
    public static Adrenaline INSTANCE;

    public Adrenaline() {
        super("Adrenaline", false, ApplyType.CHESTPLATES,
                "adrenaline");
        isUncommonEnchant = true;
        isTainted = true;
        INSTANCE = this;

        if(!isEnabled()) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    int enchantLvl = EnchantManager.getEnchantLevel(player, INSTANCE);
                    if(enchantLvl == 0) continue;

                    if(player.getHealth() / player.getMaxHealth() > getThresholdPercent(enchantLvl) / 100.0) continue;

                    Misc.applyPotionEffect(player, PotionEffectType.SPEED, 60,
                            getAmplifier(enchantLvl), true, false);
                    Sounds.ADRENALINE.play(player);
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 10L);
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        if(attackEvent.getAttacker().getHealth() / attackEvent.getAttacker().getMaxHealth() >
                getThresholdPercent(enchantLvl) / 100.0) return;

        attackEvent.increasePercent += getDamageIncrease(enchantLvl);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7When you have less than &c" + getThresholdPercent(enchantLvl) + "% &7of your &cmax hp&7, gain " +
                        "&eSpeed " + AUtil.toRoman(getAmplifier(enchantLvl) + 1) + " &7and deal &c+" +
                        getDamageIncrease(enchantLvl) + "% &7more damage"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "gives you &eSpeed &7and increases the damage you deal when you are low";
    }

    public static int getThresholdPercent(int enchantLvl) {
        return 60;
    }

    public static int getDamageIncrease(int enchantLvl) {
        return enchantLvl * 10 + 15;
    }

    public static int getAmplifier(int enchantLvl) {
        return enchantLvl - 1;
    }
}
