package me.wisdom.thepit.megastreaks;

import me.wisdom.thepit.controllers.objects.Megastreak;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NoMegastreak extends Megastreak {
    public static NoMegastreak INSTANCE;

    public NoMegastreak() {
        super("&7No Megastreak", "nomegastreak", Integer.MAX_VALUE, 0, 0);
        INSTANCE = this;
    }

    @Override
    public String getPrefix(Player player) {
        return null;
    }

    @Override
    public ItemStack getBaseDisplayStack(Player player) {
        return new AItemStackBuilder(Material.REDSTONE_BLOCK)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, PitPlayer pitPlayer) {
        loreBuilder.addLongLine(
                "&7在没有奖励加成和减益效果的情况下继续高连击"
        );
    }

    @Override
    public String getSummary() {
        return null;
    }
}
