package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Booboo extends PitEnchant {
    public static Booboo INSTANCE;
    public static int counter = 0;

    public Booboo() {
        super("Boo-boo", false, ApplyType.PANTS,
                "booboo", "boo", "bb", "boo-boo");
        isUncommonEnchant = true;
        INSTANCE = this;

        if(!isEnabled()) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    int level = EnchantManager.getEnchantLevel(player, INSTANCE);
                    PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
                    if(ComboVenom.isVenomed(player)) continue;

                    if(level == 1 && counter % 5 == 0) {
                        pitPlayer.heal(2);
                    } else if(level == 2 && counter % 4 == 0) {
                        pitPlayer.heal(2);
                    } else if(level == 3 && counter % 3 == 0) {
                        pitPlayer.heal(2);
                    } else if(level == 4 && counter % 2 == 0) {
                        pitPlayer.heal(2);
                    } else if(level > 4) {
                        pitPlayer.heal(2);
                    }
                }

                counter++;
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 20L);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Passively regain &c" + Misc.getHearts(2) + " &7every " +
                        getSeconds(enchantLvl) + " &7seconds"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that " +
                "passively heals you while wearing it";
    }

    public int getSeconds(int enchantLvl) {

        return Math.max(5 - enchantLvl, 1);
    }
}
