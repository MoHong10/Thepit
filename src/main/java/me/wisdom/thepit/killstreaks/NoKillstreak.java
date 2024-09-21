package me.wisdom.thepit.killstreaks;

import me.wisdom.thepit.controllers.objects.Killstreak;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NoKillstreak extends Killstreak {

    public static NoKillstreak INSTANCE;

    public NoKillstreak() {
        super("No killstreak", "NoKillstreak", -1, 0);
        INSTANCE = this;
    }

    @Override
    public void proc(Player player) {

    }

    @Override
    public void reset(Player player) {

    }

    @Override
    public ItemStack getDisplayStack(Player player) {
        AItemStackBuilder builder = new AItemStackBuilder(Material.GOLD_BLOCK)
                .setName("&c" + displayName)
                .setLore(new ALoreBuilder(
                        "&7Wanna free up this slot for",
                        "&7some reason?"
                ));

        return builder.getItemStack();
    }

    @Override
    public String getSummary() {
        return null;
    }
}
