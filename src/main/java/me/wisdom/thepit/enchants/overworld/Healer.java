package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.battlepass.quests.AttackBotsWithHealerQuest;
import me.wisdom.thepit.controllers.Cooldown;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.Effect;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Healer extends PitEnchant {

    public Healer() {
        super("Healer", true, ApplyType.SWORDS,
                "healer", "heal");
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isAttackerPlayer() || !attackEvent.isDefenderPlayer()) return;
        if(!canApply(attackEvent)) return;
        PitPlayer pitAttacker = attackEvent.getAttackerPitPlayer();
        PitPlayer pitDefender = attackEvent.getDefenderPitPlayer();

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;
        if(attackEvent.isFakeHit()) return;

        Cooldown cooldown = getCooldown(attackEvent.getAttackerPlayer(), 20);
        if(cooldown.isOnCooldown()) return;
        else cooldown.restart();

        attackEvent.multipliers.add(0d);
        pitAttacker.heal(0.5 + (0.5 * enchantLvl));
        pitDefender.heal(getHealing(enchantLvl));
        AttackBotsWithHealerQuest.INSTANCE.healPlayer(pitAttacker, getHealing(enchantLvl));

        attackEvent.getDefender().getWorld().spigot().playEffect(attackEvent.getDefender().getLocation().add(0, 1, 0),
                Effect.HAPPY_VILLAGER, 0, 0, (float) 0.5, (float) 0.5, (float) 0.5, (float) 0.01, 20, 50);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Your hits &aheal &7you for &c" + Misc.getHearts(0.5 + (0.5 * enchantLvl)) +
                        "&7 and them for &c" + Misc.getHearts(getHealing(enchantLvl)) + " &7(1s cooldown)"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that heals both " +
                "you and your opponent. Quite silly if you ask me";
    }

    public double getHealing(int enchantLvl) {
        return enchantLvl * 2;
    }
}
