package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.Cooldown;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Peroxide extends PitEnchant {

    public Peroxide() {
        super("Peroxide", false, ApplyType.PANTS,
                "pero", "peroxide", "regeneration", "regen");
        isUncommonEnchant = true;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isDefenderPlayer()) return;
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
        if(enchantLvl == 0) return;

        Cooldown cooldown = getCooldown(attackEvent.getDefenderPlayer(), 51);
        if(cooldown.isOnCooldown()) return;
        else cooldown.restart();

        Misc.applyPotionEffect(attackEvent.getDefender(), PotionEffectType.REGENERATION, getDuration(enchantLvl),
                getAmplifier(enchantLvl), false, false);

    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Gain &cRegen " + AUtil.toRoman(getAmplifier(enchantLvl) + 1) + " &7(" +
                        getDuration(enchantLvl) / 20 + "&7s) when hit"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that gives " +
                "you &cRegeneration &7when you are hit";
    }

    public int getAmplifier(int enchantLvl) {
        return Misc.linearEnchant(enchantLvl, 0.5, 0);
    }

    public int getDuration(int enchantLvl) {
        return Misc.linearEnchant(enchantLvl, 0.5, 1.5) * 50 + 49;
    }
}
