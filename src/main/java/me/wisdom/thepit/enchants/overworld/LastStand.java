package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.controllers.Cooldown;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class LastStand extends PitEnchant {

    public LastStand() {
        super("Last Stand", false, ApplyType.PANTS,
                "laststand", "last", "last-stand", "resistance");
    }

    @EventHandler
    public void onAttack(AttackEvent.Post attackEvent) {
        if(!attackEvent.isDefenderPlayer()) return;
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
        if(enchantLvl == 0) return;

        if(attackEvent.getDefender().getHealth() - attackEvent.getFinalDamage() <= getProcHealth()) {
            Cooldown cooldown = getCooldown(attackEvent.getDefenderPlayer(), getCooldownSeconds(enchantLvl) * 20);
            if(cooldown.isOnCooldown()) return;
            else cooldown.restart();
            Sounds.LAST_STAND.play(attackEvent.getDefender());
            Misc.applyPotionEffect(attackEvent.getDefender(), PotionEffectType.DAMAGE_RESISTANCE, getSeconds(enchantLvl)
                    * 20, getAmplifier(enchantLvl), false, false);
        }
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Gain &9Resistance " + AUtil.toRoman(getAmplifier(enchantLvl) + 1) + " &7(" +
                        getSeconds(enchantLvl) + " &7seconds) &7when reaching &c" + Misc.getHearts(getProcHealth()) +
                        " &7(" + getCooldownSeconds(enchantLvl) + "s cooldown)"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that gives you " +
                "&9Resistance &7when you get low";
    }

    public int getCooldownSeconds(int enchantLvl) {
        return 10;
    }

    public int getProcHealth() {
        return 12;
    }

    public int getAmplifier(int enchantLvl) {
        return enchantLvl - 1;
    }

    public int getSeconds(int enchantLvl) {
        return Misc.linearEnchant(enchantLvl, 0.5, 3);
    }
}
