package me.wisdom.thepit.enchants.tainted.scythe;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitEnchantSpell;
import me.wisdom.thepit.cosmetics.misc.kyrocosmetic.LeechParticle;
import me.wisdom.thepit.events.SpellUseEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Leech extends PitEnchantSpell {
    public static Leech INSTANCE;

    public Leech() {
        super("Leech",
                "leech");
        isTainted = true;
        INSTANCE = this;
    }

    @EventHandler
    public void onUse(SpellUseEvent event) {
        if(!isThisSpell(event.getSpell())) return;
        Player player = event.getPlayer();

        LeechParticle leechParticle = new LeechParticle(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                leechParticle.remove();
            }
        }.runTaskLater(Thepit.INSTANCE, getLifetimeSeconds(event.getSpellLevel()) * 20L);
        Sounds.LEECH.play(player);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Right-Clicking casts this spell for &b" + getManaCost(enchantLvl) + " mana&7, spawning a " +
                        "&cleech particle &7that steals enemies' health for " + getLifetimeSeconds(enchantLvl) + " second" +
                        (getLifetimeSeconds(enchantLvl) == 1 ? "" : "s")
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "spawns a particle that steals health from other opponents and gives it to you";
    }

    @Override
    public int getManaCost(int enchantLvl) {
        return 35;
    }

    @Override
    public int getCooldownTicks(int enchantLvl) {
        return 4;
    }

    public static int getLifetimeSeconds(int enchantLvl) {
        return enchantLvl * 4 + 4;
    }
}
