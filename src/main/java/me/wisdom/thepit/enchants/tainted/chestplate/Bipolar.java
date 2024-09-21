package me.wisdom.thepit.enchants.tainted.chestplate;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.Cooldown;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.ManaRegenEvent;
import me.wisdom.thepit.events.PitQuitEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Bipolar extends PitEnchant {
    public static Bipolar INSTANCE;
    public static List<Player> vengefulPlayers = new ArrayList<>();

    public Bipolar() {
        super("Bipolar", true, ApplyType.CHESTPLATES,
                "bipolar", "polar");
        isTainted = true;
        INSTANCE = this;

        if(!isEnabled()) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    int enchantLvl = EnchantManager.getEnchantLevel(player, INSTANCE);
                    if(enchantLvl == 0) continue;
                    PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
                    if(!pitPlayer.hasManaUnlocked()) continue;
                    if(vengefulPlayers.contains(player)) {
                        Misc.applyPotionEffect(player, PotionEffectType.SPEED, 60,
                                getSpeedAmplifier(enchantLvl), true, false);
                    } else {
                        Misc.applyPotionEffect(player, PotionEffectType.REGENERATION, 60,
                                getRegenerationAmplifier(enchantLvl), true, false);
                    }
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 20L);
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isAttackerPlayer()) return;
        if(!canApply(attackEvent)) return;
        if(!attackEvent.getAttackerPitPlayer().hasManaUnlocked()) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        if(vengefulPlayers.contains(attackEvent.getAttackerPlayer())) {
            attackEvent.increasePercent += getDamageIncrease(enchantLvl);
        } else {
            attackEvent.multipliers.add(Misc.getReductionMultiplier(getDamageDecrease(enchantLvl)));
        }
    }

    @EventHandler
    public void onManaRegen(ManaRegenEvent event) {
        Player player = event.getPlayer();
        int enchantLvl = EnchantManager.getEnchantLevel(player, this);
        if(enchantLvl == 0) return;
        event.multipliers.add(Misc.getReductionMultiplier(getManaReduction(enchantLvl)));
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if(player.isSneaking() || player.isFlying()) return;

        int enchantLvl = EnchantManager.getEnchantLevel(player, this);
        if(enchantLvl == 0) return;

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(!pitPlayer.hasManaUnlocked()) return;

        Cooldown cooldown = getCooldown(player, 20 * 5);
        if(cooldown.isOnCooldown()) {
            Sounds.NO.play(player);
            return;
        }
        cooldown.restart();

        if(vengefulPlayers.contains(player)) {
            Misc.sendTitle(player, "&a&lPEACE", 20);
            Misc.sendSubTitle(player, "&7You take a deep breath and sigh", 20);
            Sounds.BIPOLAR_PEACE.play(player);
            vengefulPlayers.remove(player);
        } else {
            Misc.sendTitle(player, "&c&lVENGEANCE", 20);
            Misc.sendSubTitle(player, "&7A sudden rage fills your body", 20);
            Sounds.BIPOLAR_VENGEANCE.play(player);
            vengefulPlayers.add(player);
        }
    }

    @EventHandler
    public void onQuit(PitQuitEvent event) {
        Player player = event.getPlayer();
        vengefulPlayers.remove(player);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new ALoreBuilder(
                "&7Sneaking toggles between &cVengeful",
                "&7and &aPeaceful &7modes (5s cooldown):",
                "&c\u25a0 Vengeful&7: Deal &c+" + getDamageIncrease(enchantLvl) + "% &7damage,",
                "&7gain &eSpeed " + AUtil.toRoman(getSpeedAmplifier(enchantLvl) + 1),
                "&a\u25a0 Peaceful&7: Deal &9-" + getDamageDecrease(enchantLvl) + "% &7damage,",
                "&7gain &cRegeneration " + AUtil.toRoman(getRegenerationAmplifier(enchantLvl) + 1),
                "&7When worn, regain mana &b" + getManaReduction(enchantLvl) + "% &7slower"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "allows you to toggle between an offensive and a defensive mode while fighting";
    }

    public static int getManaReduction(int enchantLvl) {
        return 50;
    }

    public static int getDamageIncrease(int enchantLvl) {
        return enchantLvl * 5 + 5;
    }

    public static int getSpeedAmplifier(int enchantLvl) {
        return Misc.linearEnchant(enchantLvl, 0.5, 0);
    }

    public static int getDamageDecrease(int enchantLvl) {
        return Math.max(66 - enchantLvl * 11, 0);
    }

    public static int getRegenerationAmplifier(int enchantLvl) {
        return Misc.linearEnchant(enchantLvl, 0.5, -0.5);
    }
}
