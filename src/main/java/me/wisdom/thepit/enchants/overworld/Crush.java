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

public class Crush extends PitEnchant {

    public Crush() {
        super("Crush", false, ApplyType.MELEE,
                "crush");
        isUncommonEnchant = true;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isAttackerPlayer()) return;
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        Cooldown cooldown = getCooldown(attackEvent.getAttackerPlayer(), 2 * 20);
        if(cooldown.isOnCooldown()) return;
        else cooldown.restart();

        Misc.applyPotionEffect(attackEvent.getDefender(), PotionEffectType.WEAKNESS, getDuration(enchantLvl), enchantLvl + 3, true, false);
        Sounds.CRUSH.play(attackEvent.getAttacker());
        Sounds.CRUSH.play(attackEvent.getDefender());
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Strikes apply &cWeakness " + AUtil.toRoman(enchantLvl + 4) + " &7(lasts " +
                        (getDuration(enchantLvl) / 20D) + "s, 2s cooldown)"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that briefly " +
                "applies a high level of &cWeakness &7to your opponent on strike";
    }

    public int getDuration(int enchantLvl) {

        return enchantLvl * 6 + 2;
    }
}
