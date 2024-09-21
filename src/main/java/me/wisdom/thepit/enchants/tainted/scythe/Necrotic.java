package me.wisdom.thepit.enchants.tainted.scythe;

import me.wisdom.thepit.controllers.objects.PitEnchantSpell;
import me.wisdom.thepit.darkzone.DarkzoneBalancing;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.SpellUseEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;

public class Necrotic extends PitEnchantSpell {
    public static Necrotic INSTANCE;

    public Necrotic() {
        super("Necrotic",
                "necrotic", "necro");
        isTainted = true;
        INSTANCE = this;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(AttackEvent.Pre attackEvent) {
        if(!(attackEvent.getFireball() instanceof WitherSkull)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        if(attackEvent.getAttacker() == attackEvent.getDefender()) {
            attackEvent.setCancelled(true);
            attackEvent.getAttackerEnchantMap().clear();
            attackEvent.getDefenderEnchantMap().clear();
            return;
        }

        attackEvent.getWrapperEvent().getSpigotEvent().setDamage(DarkzoneBalancing.SCYTHE_DAMAGE * 3);
    }

    @EventHandler
    public void onUse(SpellUseEvent event) {
        if(!isThisSpell(event.getSpell())) return;
        Player player = event.getPlayer();

        player.launchProjectile(WitherSkull.class);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Right-Clicking casts this spell for &b" + getManaCost(enchantLvl) + " mana&7, " +
                        "shooting a &8wither skull&7 that was so kindly donated by a now-headless friend"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "shoots a &8wither skull &7in the direction that you are looking";
    }

    @Override
    public int getManaCost(int enchantLvl) {
        return Math.max(26 - enchantLvl * 4, 0);
    }

    @Override
    public int getCooldownTicks(int enchantLvl) {
        return 4;
    }
}
