package me.wisdom.thepit.killstreaks;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.Killstreak;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class Monster extends Killstreak {

    public static Monster INSTANCE;

    public Monster() {
        super("Monster", "Monster", 40, 0);
        INSTANCE = this;
    }

    public static Map<Player, Integer> healthMap = new HashMap<>();

    @Override
    public void proc(Player player) {
        if(healthMap.containsKey(player) && healthMap.get(player) >= 4) return;
        if(healthMap.containsKey(player)) healthMap.put(player, healthMap.get(player) + 1);
        else healthMap.put(player, 1);
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        pitPlayer.updateMaxHealth();
    }

    @Override
    public void reset(Player player) {
        healthMap.remove(player);
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                pitPlayer.updateMaxHealth();
            }
        }.runTaskLater(Thepit.INSTANCE, 1L);
    }

    @Override
    public ItemStack getDisplayStack(Player player) {
        AItemStackBuilder builder = new AItemStackBuilder(Material.APPLE)
                .setName("&e" + displayName)
                .setLore(new ALoreBuilder(
                        "&7Every: &c" + killInterval + " kills",
                        "",
                        "&7Gain &c" + Misc.getHearts(1) + " &7max health (&c" + Misc.getHearts(4) + " &7max)"
                ));

        return builder.getItemStack();
    }

    @Override
    public String getSummary() {
        return "&eMonster&7 is a killstreak that increases your max hearts by &c" + Misc.getHearts(2) +
                " &7every &c40 kills";
    }
}
